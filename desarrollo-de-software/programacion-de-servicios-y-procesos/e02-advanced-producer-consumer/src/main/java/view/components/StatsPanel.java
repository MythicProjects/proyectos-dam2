package view.components;

import java.awt.GridLayout;

import javax.swing.JPanel;

import view.dto.StatsVM;
import view.theme.Theme;
import view.theme.Themed;

public class StatsPanel extends JPanel implements Themed {
    private final StatBox resources = new StatBox("Resource/s");
    private final StatBox production = new StatBox("Production/s");
    private final StatBox consumption = new StatBox("Consumption/s");

    public StatsPanel() {
        super(new GridLayout(1, 3, 12, 0));
        setOpaque(false);
        add(resources);
        add(production);
        add(consumption);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setStats(StatsVM stats){
        resources.setValue(String.format("%.1f", stats.resourcePerSec()));
        production.setValue(String.format("%.1f", stats.productionPerSec()));
        consumption.setValue(String.format("%.1f", stats.consumptionPerSec()));
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme) {
        resources.applyTheme(theme);
        production.applyTheme(theme);
        consumption.applyTheme(theme);
    }
}
