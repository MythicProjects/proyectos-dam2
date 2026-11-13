package view.components;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.theme.Theme;
import view.theme.Themed;

public class StatBox extends JPanel implements Themed{
    private final RetroBorder border = new RetroBorder(2, 3);
    private final JLabel titleLabel;
    private final JLabel valueLabel = new JLabel("-", SwingConstants.CENTER);

    public StatBox(String title){
        super(new GridLayout(2, 1));
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(2, 5, 2, 6)));
        add(titleLabel);
        add(valueLabel);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setValue(String value) { valueLabel.setText(value); }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        border.applyTheme(theme);
        setBackground(theme.paper());
        titleLabel.setForeground(theme.muted());
        titleLabel.setFont(theme.uiFont());
        valueLabel.setForeground(theme.ink());
        valueLabel.setFont(theme.numberFont());
    }
}
