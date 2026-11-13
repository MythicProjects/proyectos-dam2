package model;

public class Wallet {
    private int money;
    private double multiplier = 1.0;

    public Wallet(int initialMoney){
        this.money = initialMoney;
    }

    // ------------------------------------------------------------------
    // Transactions
    // ------------------------------------------------------------------
    // --- consumer thread calls this function
    public synchronized void earn(int salePrice){
        money += (int) Math.round(salePrice * multiplier);
    }

    // --- EDT (player) calls this function
    public synchronized boolean spend(int amount){
        if (money < amount){
            return false;
        }
        money -= amount;
        return true;
    }

    // ------------------------------------------------------------------
    // State
    // ------------------------------------------------------------------
    public synchronized int getMoney(){ return money; }
    public synchronized void setMultiplier(double multiplier) { this.multiplier = multiplier; }

    // --- EDT (new game) calls this function
    public synchronized void reset(int initialMoney){
        money = initialMoney;
        multiplier = 1.0;
    }
}
