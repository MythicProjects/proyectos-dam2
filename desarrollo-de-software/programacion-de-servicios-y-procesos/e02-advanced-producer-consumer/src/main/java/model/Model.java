package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import contract.IModel;

public class Model implements IModel{
    // ------------------------------------------------------------------
    // Game rules
    // ------------------------------------------------------------------
    private static final int SLOTS = 8;
    private static final int SEATS = 32;
    private static final int MAX_QUEUE = 20;
    private static final int CAPACITY = 200;
    private static final int INITIAL_MONEY = 100;

    private static final double PRODUCER_CYCLE_SECONDS = 3.0;
    private static final double MIN_EAT_SECONDS = 2.0;
    private static final double MAX_EAT_SECONDS = 5.0;
    private static final double ARRIVAL_SECONDS = 2.0;
    private static final double DAY_GROWTH = 0.10;
    private static final double DAY_SECONDS = 10.0;

    private static final int PRODUCER_PRICE = 100;
    private static final int UPGRADE_PRICE = 50;
    private static final int UNLOCK_PRICE = 200;
    private static final int PROMO_PRICE = 300;
    private static final int PROMO_SECONDS = 30;
    private static final double PROMO_MULTIPLIER = 1.5;

    private static final long TICK_MS = 100;
    private static final long JOIN_MS = 500;

    // ------------------------------------------------------------------
    // Shared state
    // ------------------------------------------------------------------
    private final List<Product> products = List.of(
            new Product(0, "Bread", 5, 12, true),
            new Product(1, "Milk", 8, 18, false),
            new Product(2, "Cheese", 15, 32, false),
            new Product(3, "Apple pie", 30, 60, false),
            new Product(4, "Sushi", 60, 120, false),
            new Product(5, "Truffle", 120, 250, false));

    private final Clock clock = new Clock();
    private final Wallet wallet = new Wallet(INITIAL_MONEY);
    private final OrderList orders = new OrderList();
    private final Resource resource = new Resource(CAPACITY, products.size());

    private final Producer[] slots = new Producer[SLOTS];
    private final Consumer[] seats = new Consumer[SEATS];
    private final Random random = new Random();

    private Thread world;

    private volatile int day = 1;
    private volatile int queue;
    private volatile double promoSecondsLeft;
    private volatile double productionPerSec;
    private volatile double consumptionPerSec;
    private volatile int producerCount;

    // ------------------------------------------------------------------
    // Simulation
    // ------------------------------------------------------------------
    @Override
    public void start(){
        openProducer(0);
        world = new Thread(this::worldLoop, "World");
        world.setDaemon(true);
        world.start();
    }

    @Override
    public void stop(){
        if (world != null) world.interrupt();
        for (Producer producer : slots) {
            if (producer != null) producer.interrupt();
        }

        synchronized(seats){
            for (Consumer consumer : seats) {
                if (consumer != null) consumer.interrupt();
            }
        }
    }

    @Override
    public void newGame() {
        stop();
        awaitThreads();

        orders.clear();
        resource.clear();
        wallet.reset(INITIAL_MONEY);
        for (Product product : products) {
            product.reset();
        }
        Arrays.fill(slots, null);
        synchronized (seats) {
            Arrays.fill(seats, null);
        }

        day = 1;
        queue = 0;
        promoSecondsLeft = 0;
        productionPerSec = 0;
        consumptionPerSec = 0;
        producerCount = 0;

        clock.play();
        start();
    }

    private void awaitThreads() {
        List<Thread> threads = new ArrayList<>();
        if (world != null) threads.add(world);
        for (Producer producer : slots) {
            if (producer != null) threads.add(producer);
        }
        synchronized (seats) {
            for (Consumer consumer : seats) {
                if (consumer != null) threads.add(consumer);
            }
        }

        for (Thread thread : threads) {
            try {
                thread.join(JOIN_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Override
    public void play() { clock.play(); }

    @Override
    public void pause() { clock.pause(); }

    @Override
    public void fastForward() { clock.fastForward(); }

    // ------------------------------------------------------------------
    // World thread: customers arrive, sit down, days pass, rates update
    // ------------------------------------------------------------------
    // --- world thread runs this function
    private void worldLoop(){
        double arrivalTimer = nextArrival();
        double dayTimer = 0;
        double rateTimer = 0;
        long lastProduced = 0;
        long lastConsumed = 0;

        try {
            while (!Thread.currentThread().isInterrupted()) {
                clock.awaitRunning();
                Thread.sleep(TICK_MS);
                double dt = TICK_MS / 1000.0 * clock.getSpeed();

                arrivalTimer -= dt;
                if (arrivalTimer <= 0){
                    if (queue < MAX_QUEUE) queue ++;
                    arrivalTimer = nextArrival();
                }

                seatConsumers();

                if (promoSecondsLeft > 0) {
                    promoSecondsLeft = Math.max(0, promoSecondsLeft - dt);
                    if(promoSecondsLeft == 0) wallet.setMultiplier(1.0);
                }

                dayTimer += dt;
                if (dayTimer >= DAY_SECONDS){
                    dayTimer -= DAY_SECONDS;
                    day++;
                }

                rateTimer += dt;

                if(rateTimer >= 1.0){
                    long produced = resource.getTotalProduced();
                    long consumed = resource.getTotalConsumed();

                    productionPerSec = (produced - lastProduced) / rateTimer;
                    consumptionPerSec = (consumed - lastConsumed) / rateTimer;
                    lastProduced = produced;
                    lastConsumed = consumed;
                    rateTimer = 0;
                }
            }
        } catch (InterruptedException e) {
        }
    }

    private double nextArrival() {
        double demand = producerCount * (1 + DAY_GROWTH * (day - 1));
        if (promoSecondsLeft > 0) demand *= PROMO_MULTIPLIER;
        double average = ARRIVAL_SECONDS / Math.max(1, demand);
        return average * (0.5 + random.nextDouble());
    }

    private void seatConsumers() {
        synchronized (seats){
            for (int i = 0; i < seats.length; i++) {
                if (seats[i] != null && !seats[i].isAlive()){
                    seats[i] = null;
                }
                if (seats[i] == null && queue > 0){
                    double eatSeconds = MIN_EAT_SECONDS + random.nextDouble() * (MAX_EAT_SECONDS - MIN_EAT_SECONDS);
                    seats[i] = new Consumer(i, eatSeconds, resource, products, wallet, clock);
                    seats[i].start();
                    queue--;
                }
            }
        }
    }

    // ------------------------------------------------------------------
    // Player commands
    // ------------------------------------------------------------------
    // --- EDT calls these functions
    @Override
    public boolean orderProduct(int productId){
        Product product = products.get(productId);
        if (!product.isUnlocked() || !wallet.spend(product.getCost())){
            return false;
        }

        orders.addOrder(productId);
        return true;
    }

    @Override
    public boolean newProducer() {
        int slot = firstClosedSlot();
        if (slot < 0 || !wallet.spend(getNewProducerPrice())) {
            return false;
        }

        openProducer(slot);
        return true;
    }

    @Override
    public boolean upgradeProducer(int slot) {
        if (slot < 0 || slot >= SLOTS || slots[slot] == null || !wallet.spend(getUpgradePrice(slot))) {
            return false;
        }
        slots[slot].upgrade();
        return true;
    }

    @Override
    public boolean unlockNextProduct() {
        Product next = nextLockedProduct();
        if (next == null || !wallet.spend(getUnlockPrice())) {
            return false;
        }

        next.unlock();
        return true;
    }

    @Override
    public boolean startPromo() {
        if (promoSecondsLeft > 0 || !wallet.spend(PROMO_PRICE)) {
            return false;
        }

        wallet.setMultiplier(PROMO_MULTIPLIER);
        promoSecondsLeft = PROMO_SECONDS;
        return true;
    }

    // ------------------------------------------------------------------
    // Slots and products helpers
    // ------------------------------------------------------------------
    private void openProducer(int slot) {
        slots[slot] = new Producer(slot, PRODUCER_CYCLE_SECONDS, orders, resource, clock);
        slots[slot].start();
        producerCount++;
    }

    private int firstClosedSlot() {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == null) return i;
        }
        return -1;
    }

    private int openProducers() {
        int count = 0;
        for (Producer producer : slots) {
            if (producer != null) count++;
        }
        return count;
    }

    private Product nextLockedProduct() {
        for (Product product : products) {
            if (!product.isUnlocked()) return product;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Game state
    // ------------------------------------------------------------------
    @Override
    public int getMoney() {
        return wallet.getMoney();
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public int getQueue() {
        return queue;
    }

    @Override
    public int getPendingOrders() {
        return orders.size();
    }

    @Override
    public double getProductionPerSec() {
        return productionPerSec;
    }

    @Override
    public double getConsumptionPerSec() {
        return consumptionPerSec;
    }

    // ------------------------------------------------------------------
    // Entities
    // ------------------------------------------------------------------
    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public List<Producer> getProducers() {
        return Arrays.asList(slots.clone());
    }

    @Override
    public List<Consumer> getConsumers() {
        synchronized (seats) {
            List<Consumer> list = new ArrayList<>(SEATS);
            for (Consumer consumer : seats) {
                list.add(consumer != null && consumer.isAlive() ? consumer : null);
            }
            return list;
        }
    }

    // ------------------------------------------------------------------
    // Prices
    // ------------------------------------------------------------------
    @Override
    public int getNewProducerPrice() {
        return PRODUCER_PRICE * openProducers();
    }

    @Override
    public int getUpgradePrice(int slot) {
        Producer producer = slots[slot];
        return producer == null ? 0 : UPGRADE_PRICE * producer.getLevel();
    }

    @Override
    public int getUnlockPrice() {
        Product next = nextLockedProduct();
        return next == null ? 0 : UNLOCK_PRICE * next.getId();
    }

    @Override
    public int getPromoPrice() {
        return PROMO_PRICE;
    }

    @Override
    public int getPromoSecondsLeft() {
        return (int) Math.ceil(promoSecondsLeft);
    }

    @Override
    public int getPromoDuration() {
        return PROMO_SECONDS;
    }
}
