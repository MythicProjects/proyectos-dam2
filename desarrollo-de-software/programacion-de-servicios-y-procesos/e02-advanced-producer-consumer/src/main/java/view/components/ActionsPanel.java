package view.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.IntConsumer;

import javax.swing.JPanel;

import view.dto.ActionsVM;
import view.theme.Icons;
import view.theme.Theme;
import view.theme.Themed;

public class ActionsPanel extends JPanel implements Themed {
    private final RetroButton newProducerButton = new RetroButton("New producer", Icons.PLUS);
    private final RetroButton upgradButton = new RetroButton("Upgrade", Icons.UPGRADE);
    private final RetroButton newProductButton = new RetroButton("New product", Icons.PLUS);

    private int upgradeSlot = -1;

    public ActionsPanel() {
        super(new GridBagLayout());

        add(newProducerButton, row(0));
        add(upgradButton, row(1));
        add(newProductButton, row(2));

        newProducerButton.setEnabled(false);
        upgradButton.setEnabled(false);
        newProductButton.setEnabled(false);
    }

    // ------------------------------------------------------------------
    // Layout
    // ------------------------------------------------------------------
    private static GridBagConstraints row(int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = y;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(3, 0, 3, 0);
        return c;
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setActions(ActionsVM actions){
        newProducerButton.setText(String.format("New producer $%,d", actions.newProducerPrice()));
        newProducerButton.setEnabled(actions.canAddProducer());

        upgradeSlot = actions.upgradeSlot();

        if(upgradeSlot >= 0){
            upgradButton.setText(String.format("Upgrade #%d $%,d", upgradeSlot + 1, actions.upgradePrice()));
            upgradButton.setEnabled(true);
        } else {
            upgradButton.setText("Upgrade");
            upgradButton.setEnabled(false);
        }

        if(actions.nextProductId() >= 0){
            newProductButton.setText(String.format("New product: %s $%,d", actions.nextProductName(), actions.nextProductPrice()));
            newProductButton.setEnabled(true);
        } else {
            newProductButton.setText("All products unlocked");
            newProductButton.setEnabled(false);
        }
    }

    // ------------------------------------------------------------------
    // Events
    // ------------------------------------------------------------------
    public void onNewProducer(Runnable action) { newProducerButton.addActionListener(e -> action.run()); }
    public void onUpgradeProducer(IntConsumer action) { upgradButton.addActionListener(e -> action.accept(upgradeSlot)); }
    public void onNewProduct(Runnable action) { newProductButton.addActionListener(e -> action.run()); }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        setBackground(theme.paper());
        newProducerButton.applyTheme(theme);
        upgradButton.applyTheme(theme);
        newProductButton.applyTheme(theme);
    }
}
