package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import presenter.IPresenter;


public class View implements IView {
    // Presenter
    private IPresenter presenter;

    private final List<Viewer> viewers = new ArrayList<>();
    private final List<DataPanel> dataPanels = new ArrayList<>();
    private final List<ControlPanel> controlPanels = new ArrayList<>();

    private int lastWidth, lastHeight;

    private final JFrame frame = new JFrame("Custom Layout");
    private final Workspace workspace = new Workspace(this::createEditor);
    private final Timer renderTimer = new Timer(16, e -> render());

    public View(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(workspace);
    }


    private JComponent createEditor(EditorType type){
        switch (type) {
            case VIEWER:{
                Viewer viewer = new Viewer();
                viewers.add(viewer);
                return viewer;
            }
            case CONTROL: {
                ControlPanel controls = new ControlPanel();
                controls.setPresenter(presenter);
                controlPanels.add(controls);
                return controls;
            }
            default:{
                DataPanel data = new DataPanel();
                data.setPresenter(presenter);
                data.showValues(lastWidth, lastHeight);
                dataPanels.add(data);
                return data;
            }
        }
    }
    // ------------------------------------------------------------------
    // Presenter
    // ------------------------------------------------------------------
    @Override 
    public void setPresenter(IPresenter presenter){ this.presenter = presenter; }
    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------
    @Override 
    public void startView(){
        SwingUtilities.invokeLater(() -> {
            Area controls = workspace.createArea(EditorType.CONTROL);
            Area data = workspace.createArea(EditorType.DATA);
            Area viewer = workspace.createArea(EditorType.VIEWER);
            JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, controls, data);
            JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, viewer);
            root.setResizeWeight(0.0);
            workspace.setRoot(root);
            
            frame.setSize(1280, 720);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            renderTimer.start();
        });
    }

    private void render(){
        viewers.removeIf(v ->  !v.isDisplayable());
        for (Viewer viewer : viewers){
            viewer.updateRender(presenter.getImage());
        }
    }
    // ------------------------------------------------------------------
    // Info
    // ------------------------------------------------------------------
    @Override
    public void showValues(int width, int height) {
        SwingUtilities.invokeLater(() -> {
            lastWidth = width;
            lastHeight = height;
            dataPanels.removeIf(p -> !p.isDisplayable());
            for (DataPanel data : dataPanels) {
                data.showValues(width, height);
            }
        });
    }

    @Override
    public void showMessage(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame, message));
    }
}