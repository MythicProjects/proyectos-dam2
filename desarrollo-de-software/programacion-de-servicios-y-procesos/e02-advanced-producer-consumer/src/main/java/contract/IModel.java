package contract;

import java.util.List;

import model.Consumer;
import model.Producer;
import model.Product;
import model.Resource;

public interface IModel {
    // ------------------------------------------------------------------
    // Simulation
    // ------------------------------------------------------------------
    void start();
    void stop();
    void newGame();
    void play();
    void pause();
    void fastForward();

    // ------------------------------------------------------------------
    // Player commands
    // ------------------------------------------------------------------
    boolean orderProduct(int productId);
    boolean newProducer();
    boolean upgradeProducer(int slot);
    boolean unlockNextProduct();
    boolean startPromo();

    // ------------------------------------------------------------------
    // Game state
    // ------------------------------------------------------------------
    int getMoney();
    int getDay();
    int getQueue();
    int getPendingOrders();
    double getProductionPerSec();
    double getConsumptionPerSec();

    // ------------------------------------------------------------------
    // Entities
    // ------------------------------------------------------------------
    Resource getResource();
    List<Product> getProducts();
    List<Producer> getProducers();
    List<Consumer> getConsumers();

    // ------------------------------------------------------------------
    // Prices
    // ------------------------------------------------------------------
    int getNewProducerPrice();
    int getUpgradePrice(int slot);
    int getUnlockPrice();
    int getPromoPrice();
    int getPromoSecondsLeft();
    int getPromoDuration();
}
