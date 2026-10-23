package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Area extends JPanel{
    private final JComboBox<EditorType> typeCombo = new JComboBox<>(EditorType.values());
    private final Function<EditorType, JComponent> factory;
    private JComponent content;

    public Area(EditorType type, Workspace workspace, Function<EditorType, JComponent> factory){
        super(new BorderLayout());
        this.factory = factory;

        JButton splitH = new JButton("|");
        JButton splitV = new JButton("—");
        JButton close = new JButton("×");

        splitH.addActionListener(e -> workspace.split(this, JSplitPane.HORIZONTAL_SPLIT));
        splitV.addActionListener(e -> workspace.split(this, JSplitPane.VERTICAL_SPLIT));
        close.addActionListener(e -> workspace.close(this));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 2));
        header.add(typeCombo);
        header.add(splitH);
        header.add(splitV);
        header.add(close);
        add(header, BorderLayout.PAGE_START);

        typeCombo.setSelectedItem(type);
        typeCombo.addActionListener(e -> setType((EditorType) typeCombo.getSelectedItem()));
        setType(type);
    }

    public EditorType getType(){
        return (EditorType) typeCombo.getSelectedItem();
    }

    private void setType(EditorType type){
        if(content != null) remove(content);

        content = factory.apply(type);
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
