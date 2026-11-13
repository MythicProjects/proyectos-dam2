package presenter;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import contract.IModel;
import contract.IPresenter;
import contract.IView;
import model.Consumer;
import model.Producer;
import model.Product;
import view.dto.ActionsVM;
import view.dto.ConsumerSlotVM;
import view.dto.GameSnapshot;
import view.dto.HistogramVM;
import view.dto.ProducerSlotVM;
import view.dto.ProductVM;
import view.dto.PromoVM;
import view.dto.StatsVM;

public class Presenter implements IPresenter {
    private static final int REFRESH_MS = 16;
    private static final int HISTOGRAM_COLUMNS = 32;

    private final IModel model;
    private final IView view;
    private final Timer timer;

    public Presenter(IModel model, IView view) {
        this.model = model;
        this.view = view;
        this.timer = new Timer(REFRESH_MS, e -> view.render(buildSnapshot()));
        view.setPresenter(this);
    }

    // ------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------
    @Override
    public void start() {
        model.start();
        view.render(buildSnapshot());
        view.showView();
        timer.start();
    }

    @Override
    public void stop(){
        timer.stop();
        model.stop();
    }

    @Override
    public void onNewGame() {
        model.newGame();
        view.render(buildSnapshot());
    }

    // ------------------------------------------------------------------
    // View -> Model: simulation controls
    // ------------------------------------------------------------------
    @Override
    public void onPlay(){
        model.play();
    }

    @Override
    public void onPause(){
        model.pause();
    }

    @Override
    public void onFastForward(){
        model.fastForward();
    }

    // ------------------------------------------------------------------
    // View -> Model: player actions
    // ------------------------------------------------------------------
    @Override
    public void onBuyProduct(int productId){
        model.orderProduct(productId);
    }

    @Override
    public void onNewProducer() {
        model.newProducer();
    }

    @Override
    public void onUpgradeProducer(int slot) {
        model.upgradeProducer(slot);
    }

    @Override
    public void onNewProduct() {
        model.unlockNextProduct();
    }

    @Override
    public void onPromo() {
        model.startPromo();
    }

    // ------------------------------------------------------------------
    // Model -> View
    // ------------------------------------------------------------------
    // --- Swing timer (EDT) calls this function every REFRESH_MS
    private GameSnapshot buildSnapshot(){
        List<Product> products = model.getProducts();
        double production = model.getProductionPerSec();
        double consumption = model.getConsumptionPerSec();

        return new GameSnapshot(
            model.getMoney(),
            model.getDay(),
            model.getResource().getStock(),
            new StatsVM(production - consumption, production, consumption),
            buildHistogram(products.size()),
            buildProducers(),
            buildConsumers(),
            model.getQueue(),
            buildProducts(products),
            buildActions(products),
            buildPromo());
    }

    private List<ProducerSlotVM> buildProducers() {
        List<ProducerSlotVM> list = new ArrayList<>();
        List<Producer> producers = model.getProducers();
        for (int i = 0; i < producers.size(); i++) {
            Producer p = producers.get(i);
            if( p == null ){
                list.add(new ProducerSlotVM(i, false, -1, 0, 0, 0f));
            } else {
                list.add(new ProducerSlotVM(i, true, p.getProductId(), p.getLevel(), p.getCycleSeconds(), p.getProgress()));
            }
        }
        return list;
    }

    private List<ConsumerSlotVM> buildConsumers() {
        List<ConsumerSlotVM> list = new ArrayList<>();
        List<Consumer> consumers = model.getConsumers();
        for (int i = 0; i < consumers.size(); i++) {
            Consumer c = consumers.get(i);
            if(c == null){
                list.add(new ConsumerSlotVM(i, false, -1, 0f));
            } else {
                list.add(new ConsumerSlotVM(i, c.isBusy(), c.getProductId(), c.getProgress()));
            }
        }
        return list;
    }

    private List<ProductVM> buildProducts(List<Product> products){
        List<ProductVM> list = new ArrayList<>();
        for (Product p : products) {
            list.add(new ProductVM(p.getId(), p.getName(), p.getCost(), p.isUnlocked()));
        }
        return list;
    }

    private ActionsVM buildActions(List<Product> products) {
        int upgradeSlot = -1;
        int lowestLevel = Integer.MAX_VALUE;
        boolean freeSlot = false;
        List<Producer> producers = model.getProducers();
        for (int i = 0; i < producers.size(); i++) {
            Producer p = producers.get(i);
            if (p == null) {
                freeSlot = true;
            } else if (p.getLevel() < lowestLevel) {
                lowestLevel = p.getLevel();
                upgradeSlot = i;
            }
        }
        int upgradePrice = upgradeSlot >= 0 ? model.getUpgradePrice(upgradeSlot) : 0;

        Product next = null;
        for (Product p : products) {
            if (!p.isUnlocked()) {
                next = p;
                break;
            }
        }

        return new ActionsVM(
                model.getNewProducerPrice(), freeSlot,
                upgradePrice, upgradeSlot,
                next == null ? -1 : next.getId(),
                next == null ? "" : next.getName(),
                model.getUnlockPrice());
    }

    private PromoVM buildPromo(){
        int secondsLeft = model.getPromoSecondsLeft();
        boolean active = secondsLeft > 0;
        return new PromoVM(model.getPromoPrice(), !active,
            active ? secondsLeft : model.getPromoDuration());
    }

    // ------------------------------------------------------------------
    // Histogram
    // ------------------------------------------------------------------
    private HistogramVM buildHistogram(int producCount){
        int[] units = model.getResource().getUnits();
        int[] counts = model.getResource().getCountByProduct();

        int[] percents = new int[producCount];
        for (int i = 0; i < producCount && i < counts.length; i++) {
            percents[i] = units.length == 0 ? 0 : Math.round(counts[i] * 100f / units.length);
        }

        int[] heights = bellHeights(units.length, HISTOGRAM_COLUMNS);
        int[][] columns = new int[HISTOGRAM_COLUMNS][];
        int next = 0;
        for (int c = 0; c < HISTOGRAM_COLUMNS; c++) {
            columns[c] = new int[heights[c]];
            for (int r = 0; r < heights[c]; r++) {
                columns[c][r] = units[next++];
            }
        }
        return new HistogramVM(columns, percents);
    }

    private static int[] bellHeights(int total, int columns){
        double center = (columns - 1) / 2.0;
        double sigma = columns / 6.0;
        double[] weights = new double[columns];
        double sum = 0;
        for (int c = 0; c < columns; c++) {
            double z = (c - center) / sigma;
            weights[c] = Math.exp(-z * z / 2);
            sum += weights[c];
        }

        int[] heights = new int[columns];
        double[] remainders = new double[columns];
        int assigned = 0;
        for (int c = 0; c < columns; c++) {
            double exact = total * weights[c] / sum;
            heights[c] = (int) exact;
            remainders[c] = exact - heights[c];
            assigned += heights[c];
        }

        while (assigned < total) {
            int best = 0;
            for (int c = 1; c < columns; c++) {
                if (remainders[c] > remainders[best]) best = c;
            }
            heights[best]++;
            remainders[best] = -1;
            assigned++;
        }
        return heights;
    }
}
