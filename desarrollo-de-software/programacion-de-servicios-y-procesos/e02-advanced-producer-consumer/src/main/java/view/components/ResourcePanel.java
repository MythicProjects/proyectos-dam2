package view.components;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.dto.HistogramVM;
import view.dto.ProductVM;
import view.theme.Theme;
import view.theme.Themed;

public class ResourcePanel extends JPanel implements Themed{
    private final JPanel header = new JPanel(new BorderLayout());
    private final JLabel stockLabel = new JLabel();
    private final JLabel rateLabel = new JLabel();
    private final HistogramCanvas canvas = new HistogramCanvas();
    private final LegendPanel legend = new LegendPanel();

    public ResourcePanel() {
        super(new BorderLayout(0,6));

        header.add(stockLabel, BorderLayout.WEST);
        header.add(rateLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(legend, BorderLayout.SOUTH);

        setStock(0, 0);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setStock(int stock, double ratePerSec){
        stockLabel.setText(String.format("Stock %,d", stock));
        rateLabel.setText(String.format("%+.1f/s", ratePerSec));
    }

    public void setHistogram(HistogramVM histogram, List<ProductVM> products){
        canvas.setColumns(histogram.columns());
        legend.setEntries(products, histogram.percents());
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        setBackground(theme.paper());
        header.setBackground(theme.paper());
        stockLabel.setForeground(theme.ink());
        stockLabel.setFont(theme.numberFont());
        rateLabel.setForeground(theme.positive());
        rateLabel.setFont(theme.uiFont());
        canvas.applyTheme(theme);
        legend.applyTheme(theme);
    }
}
