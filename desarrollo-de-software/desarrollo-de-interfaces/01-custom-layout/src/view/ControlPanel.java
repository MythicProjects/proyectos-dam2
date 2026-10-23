package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import presenter.IPresenter;

public class ControlPanel extends JPanel{
    private IPresenter presenter;

    public ControlPanel(){
        setLayout(new BorderLayout(0,8)); // Configura como se colocara
        add(createActionButtons(), BorderLayout.NORTH);
    }
    public void setPresenter(IPresenter presenter){ this.presenter = presenter; }
    // ------------------------------------------------------------------
    // Buton creation
    // ------------------------------------------------------------------
    private JPanel createActionButtons(){
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");

        playButton.addActionListener(e -> presenter.play());
        pauseButton.addActionListener(e -> presenter.togglePause());
        stopButton.addActionListener(e -> presenter.stop());

        panel.add(playButton);
        panel.add(pauseButton);
        panel.add(stopButton);

        return panel;
    }
}
