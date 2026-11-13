package view.components;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import view.theme.Icons;
import view.theme.Theme;
import view.theme.Themed;

import java.util.function.Consumer;

public class TopMenuBar extends JMenuBar implements Themed{
    private final JMenu factoryMenu = new JMenu("Factory");
    private final JMenu filMenu = new JMenu("File");
    private final JMenu simulationMenu = new JMenu("Simulation");
    private final JMenu helpMenu = new JMenu("Help");
    private final JMenu themeMenu = new JMenu("Theme");

    private final JMenuItem newGameItem = new JMenuItem("New game");
    private final JMenuItem exitItem = new JMenuItem("Exit");
    private final JMenuItem playItem = new JMenuItem("Play");
    private final JMenuItem pausItem = new JMenuItem("Pause");
    private final JMenuItem fastFrowardItem = new JMenuItem("Fast Forward");
    private final JMenuItem aboutItem = new JMenuItem("About");

    private final JRadioButtonMenuItem macItem = new JRadioButtonMenuItem("Mac");
    private final JRadioButtonMenuItem gameBoyItem = new JRadioButtonMenuItem("Game Boy");
    private final JRadioButtonMenuItem snesItem = new JRadioButtonMenuItem("SNES", true);
    private final JRadioButtonMenuItem retro8Item = new JRadioButtonMenuItem("8-bit");

    private final RetroButton pauseButton = new RetroButton(null, Icons.PAUSE);
    private final RetroButton playButton = new RetroButton(null, Icons.PLAY);
    private final RetroButton fastForwardButton = new RetroButton(null, Icons.FAST_FORWARD);
    private final JLabel dayLabel = new JLabel();
    private final JLabel moneyLabel = new JLabel();

    public TopMenuBar(){
        factoryMenu.setIcon(Icons.FACTORY);

        filMenu.add(newGameItem);
        filMenu.addSeparator();
        filMenu.add(exitItem);

        simulationMenu.add(playItem);
        simulationMenu.add(pausItem);
        simulationMenu.add(fastFrowardItem);

        helpMenu.add(aboutItem);

        ButtonGroup themeGroup = new ButtonGroup();
        for (JRadioButtonMenuItem item : new JRadioButtonMenuItem[] {macItem, gameBoyItem, snesItem, retro8Item}){
            themeGroup.add(item);
            themeMenu.add(item);
        }

        add(factoryMenu);
        add(filMenu);
        add(simulationMenu);
        add(themeMenu);
        add(helpMenu);

        add(Box.createHorizontalGlue());
        add(pauseButton);
        add(playButton);
        add(fastForwardButton);
        add(Box.createRigidArea(new Dimension(16, 0)));
        add(dayLabel);
        add(Box.createRigidArea(new Dimension(16, 0)));
        add(moneyLabel);
        add(Box.createRigidArea(new Dimension(8, 0)));

        setDay(1);
        setMoney(0);
    }

    // ------------------------------------------------------------------
    // Data
    // ------------------------------------------------------------------
    public void setDay(int day){
        dayLabel.setText("Day " + day);
    }

    public void setMoney(int money){
        moneyLabel.setText(String.format("$ %,d", money));
    }

    // ------------------------------------------------------------------
    // Events: simulation controls
    // ------------------------------------------------------------------
    public void onPlay(Runnable action){
        playButton.addActionListener(e -> action.run());
        playItem.addActionListener(e -> action.run());
    }

    public void onPause(Runnable action){
        pauseButton.addActionListener(e -> action.run());
        pausItem.addActionListener(e -> action.run());
    }

    public void onFastForward(Runnable action){
        fastForwardButton.addActionListener(e -> action.run());
        fastFrowardItem.addActionListener(e -> action.run());
    }

    // ------------------------------------------------------------------
    // Events: menus
    // ------------------------------------------------------------------
    public void onNewGame(Runnable action){
        newGameItem.addActionListener(e -> action.run());
    }

    public void onExit(Runnable action){
        exitItem.addActionListener(e -> action.run());
    }

    public void onAbout(Runnable action){
        aboutItem.addActionListener(e -> action.run());
    }

    public void onThemesSelected(Consumer<Theme> acton){
        macItem.addActionListener(e -> acton.accept(Theme.MAC));
        gameBoyItem.addActionListener(e -> acton.accept(Theme.GAME_BOY));
        snesItem.addActionListener(e -> acton.accept(Theme.SNES));
        retro8Item.addActionListener(e -> acton.accept(Theme.RETRO8));
    }

    // ------------------------------------------------------------------
    // Theme
    // ------------------------------------------------------------------
    @Override
    public void applyTheme(Theme theme){
        setBackground(theme.paper());
        for(JMenu menu : new JMenu[] {factoryMenu, filMenu, simulationMenu, themeMenu, helpMenu}) {
            menu.setForeground(theme.ink());
            menu.setFont(theme.uiFont());
        }

        dayLabel.setForeground(theme.ink());
        dayLabel.setFont(theme.uiFont());
        moneyLabel.setForeground(theme.ink());
        moneyLabel.setFont(theme.numberFont());
        pauseButton.applyTheme(theme);
        playButton.applyTheme(theme);
        fastForwardButton.applyTheme(theme);
        repaint();
    }
}
