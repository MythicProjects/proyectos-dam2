package contract;

public interface IPresenter {
    // ------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------
    void start();
    void stop();
    void onNewGame();

    // ------------------------------------------------------------------
    // Simulation controls
    // ------------------------------------------------------------------
    // --- EDT calls these functions
    void onPlay();
    void onPause();
    void onFastForward();

    // ------------------------------------------------------------------
    // Player actions
    // ------------------------------------------------------------------
    // --- EDT calls these functions
    void onBuyProduct(int productId);
    void onNewProducer();
    void onUpgradeProducer(int slot);
    void onNewProduct();
    void onPromo();
}
