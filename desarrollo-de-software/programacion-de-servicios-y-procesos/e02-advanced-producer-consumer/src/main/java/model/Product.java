package model;

public class Product {
    private final int id;
    private final String name;
    private final int cost;
    private final int salePrice;
    private final boolean unlockedAtStart;
    private volatile boolean unlocked;

    public Product(int id, String name, int cost, int salePrice, boolean unlocked){
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.salePrice = salePrice;
        this.unlockedAtStart = unlocked;
        this.unlocked = unlocked;
    }

    // ------------------------------------------------------------------
    // Getters
    // ------------------------------------------------------------------
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getSalePrice() { return salePrice; }
    public boolean isUnlocked() { return unlocked; }

    // ------------------------------------------------------------------
    // Unlock
    // ------------------------------------------------------------------
    // --- EDT (player) calls this function
    public void unlock() { unlocked = true; }

    // --- EDT (new game) calls this function
    public void reset() { unlocked = unlockedAtStart; }
}
