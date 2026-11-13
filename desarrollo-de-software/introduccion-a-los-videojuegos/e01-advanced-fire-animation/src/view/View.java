package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import presenter.IPresenter;

public class View implements IView, Runnable {
    // Presenter
    private IPresenter presenter;

    // Panels
    // -- Viewer
    private final Viewer viewer;
    private static final Dimension VIEWER_SIZE = new Dimension(960, 540);

    // -- Data Panel
    private final DataPanel dataPanel;
    // -- Control Panel
    private final ControlPanel controlPanel;

    private final JFrame frame;

    // Thread
    private Thread thread;
    private volatile boolean running = false;

    public View(){
        viewer = new Viewer();
        controlPanel = new ControlPanel();
        dataPanel = new DataPanel();

        JPanel lefJPanel = new JPanel(new BorderLayout());
        lefJPanel.add(controlPanel, BorderLayout.NORTH);
        lefJPanel.add(dataPanel, BorderLayout.SOUTH);

        frame = new JFrame("Fire Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(lefJPanel, BorderLayout.WEST);
        frame.add(viewer, BorderLayout.CENTER);
    }
    // ------------------------------------------------------------------
    // Presenter
    // ------------------------------------------------------------------
    @Override 
    public void setPresenter(IPresenter presenter){
        this.presenter = presenter;
        controlPanel.setPresenter(presenter);
        dataPanel.setPresenter(presenter);
    }

    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    @Override 
    public void startView(){
        viewer.setPreferredSize(VIEWER_SIZE);
        frame.pack();
        frame.setVisible(true);

        running = true;
        if (thread != null) return;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    // ---------------------- Thread
    @Override
    public void run() {      
        while (running) {
            viewer.updateRender(presenter.getImage());
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // restaura el flag
                running = false;
            }
        }
    }
    // ------------------------------------------------------------------
    // Info
    // ------------------------------------------------------------------
    @Override
    public void showValues(int width, int height, int sparks, int coolPoints, int randomization) {
        dataPanel.showValues(width, height, sparks, coolPoints, randomization);
    }
    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, message));
    }
}