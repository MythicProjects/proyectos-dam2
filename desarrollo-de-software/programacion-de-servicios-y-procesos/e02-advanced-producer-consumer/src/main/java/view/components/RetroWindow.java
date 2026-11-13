package view.components;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import view.theme.Theme;
import view.theme.Themed;

public class RetroWindow extends JPanel implements Themed {
    private final RetroBorder border = new RetroBorder(2, 3);
    private final TitleBar titleBar;
    private final JComponent content;

    public RetroWindow(String title, JComponent content){
        super(new BorderLayout());

        this.titleBar = new TitleBar(title);
        this.content = content;

        setBorder(border);
        content.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        add(titleBar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme) {
        border.applyTheme(theme);
        titleBar.applyTheme(theme);
        setBackground(theme.paper());
        content.setBackground(theme.paper());
        if(content instanceof Themed themed){
            themed.applyTheme(theme);
        }
        repaint();
    }
}
