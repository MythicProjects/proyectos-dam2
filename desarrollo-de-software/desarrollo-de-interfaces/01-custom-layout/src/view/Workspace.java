package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.function.Function;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class Workspace extends JPanel{
    private final Function<EditorType, JComponent> factory;

    public Workspace(Function<EditorType, JComponent> factory){
        super(new BorderLayout());
        this.factory = factory;
    }

    public Area createArea(EditorType type){
        return new Area(type, this, factory);
    }

    public void setRoot(Component root){
        removeAll();
        add(root, BorderLayout.CENTER);
        refresh();
    }

    public void split(Area area, int orientation){
        Area newArea = createArea(area.getType());
        Container parent = area.getParent();
        JSplitPane split = new JSplitPane(orientation);
        replace(parent, area, split);
        split.setLeftComponent(area);
        split.setRightComponent(newArea);
        split.setResizeWeight(0.5);
        refresh();
        SwingUtilities.invokeLater(()-> split.setDividerLocation(0.5));
    }

    public void close(Area area){
        Container parent = area.getParent();
        if(!(parent instanceof JSplitPane)) return;

        JSplitPane split = (JSplitPane) parent;
        Component sibling = split.getLeftComponent() == area ?  split.getRightComponent() : split.getLeftComponent();
        split.remove(sibling);
        replace(split.getParent(), split, sibling);
        refresh();
    }

    private void replace(Container parent, Component oldChild, Component newChild){
        if(parent instanceof JSplitPane){
            JSplitPane split = (JSplitPane) parent;
            int location = split.getDividerLocation();
            if(split.getLeftComponent() == oldChild) split.setLeftComponent(newChild);
            else split.setRightComponent(newChild);
            split.setDividerLocation(location);
        }else {
            parent.remove(oldChild);
            parent.add(newChild, BorderLayout.CENTER);
        }
    }
    
    private void refresh(){
        revalidate();
        repaint();
    }
}