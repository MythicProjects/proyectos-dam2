package view.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import view.dto.ActionsVM;
import view.dto.ConsumerSlotVM;
import view.dto.HistogramVM;
import view.dto.ProducerSlotVM;
import view.dto.ProductVM;
import view.dto.PromoVM;
import view.dto.StatsVM;
import view.theme.Patterns;
import view.theme.Theme;
import view.theme.Themed;

public class DesktopPanel extends JPanel implements Themed{
    private final ResourcePanel resource = new ResourcePanel();
    private final RetroWindow resourceWindow = new RetroWindow("Resource", resource);

    private final ProducersPanel producers = new ProducersPanel();
    private final RetroWindow producersWindow = new RetroWindow("Producers", producers);

    private final ConsumersPanel consumers = new ConsumersPanel();
    private final RetroWindow consumersWindow = new RetroWindow("Consumers", consumers);

    private final ProductsPanel products = new ProductsPanel();
    private final RetroWindow productsWindow = new RetroWindow("Products", products);

    private final ActionsPanel actions = new ActionsPanel();
    private final RetroWindow actionsWindow = new RetroWindow("Actions", actions);

    private final PromoPanel promo = new PromoPanel();
    private final RetroWindow promoWindow = new RetroWindow("Marketing", promo);

    private final StatsPanel statsPanel = new StatsPanel();

    private Theme theme = Theme.SNES;

    public DesktopPanel() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        add(resourceWindow,  cell(0, 0, 1, 2, 0.3, 0.6));
        add(statsPanel,      cell(1, 0, 1, 1, 0.4, 0.0));
        add(producersWindow, cell(1, 1, 1, 1, 0.4, 0.6));
        add(consumersWindow, cell(2, 0, 1, 2, 0.3, 0.6));
        add(productsWindow,  cell(0, 2, 1, 1, 0.3, 0.4));
        add(actionsWindow,   cell(1, 2, 1, 1, 0.4, 0.4));
        add(promoWindow,     cell(2, 2, 1, 1, 0.3, 0.4));

        for (JPanel panel : new JPanel[] {resourceWindow, producersWindow, consumersWindow, productsWindow, actionsWindow, promoWindow}) {
            panel.setPreferredSize(new Dimension(0,0));
        }

        statsPanel.setPreferredSize(new Dimension(0, 70));
        statsPanel.setOpaque(false);
    }

    // ------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------
    private static GridBagConstraints cell(int x, int y, int width, int height, double weightX, double weightY){
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.weightx = weightX;
        c.weighty = weightY;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(6, 6, 6, 6);

        return c;
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setProducers(List<ProducerSlotVM> slots){ producers.setSlots(slots); }
    public void setConsumers(List<ConsumerSlotVM> slots, int queue){ consumers.setConsumers(slots, queue); }
    public void setProducts(List<ProductVM> list) { products.setProducts(list); }
    public void setActions(ActionsVM vm) { actions.setActions(vm); }
    public void setPromo(PromoVM vm) { promo.setPromo(vm); }
    public void setStats(StatsVM vm) { statsPanel.setStats(vm); }
    public void setResource(int stock, double ratePerSec, HistogramVM histogram, List<ProductVM> list) {
        resource.setStock(stock, ratePerSec);
        resource.setHistogram(histogram, list);
    }

    // ------------------------------------------------------------------
    // Events
    // ------------------------------------------------------------------
    public void onBuyProduct(IntConsumer action) { products.onBuyProduct(action); }
    public void onNewProducer(Runnable action) { actions.onNewProducer(action); }
    public void onUpgradeProducer(IntConsumer action) { actions.onUpgradeProducer(action); }
    public void onNewProduct(Runnable action) { actions.onNewProduct(action); }
    public void onPromo(Runnable action) { promo.onPromo(action); }

    // ------------------------------------------------------------------
    // Painting
    // ------------------------------------------------------------------
    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(Patterns.checker(theme.deskA(), theme.deskB(), 2));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        this.theme = theme;
        statsPanel.applyTheme(theme);
        for (RetroWindow window : new RetroWindow[] {resourceWindow, producersWindow,
                consumersWindow, productsWindow, actionsWindow, promoWindow}) {
            window.applyTheme(theme);
        }

        repaint();
    }
}
