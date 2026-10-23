package view;

public enum EditorType {
    VIEWER("Viewer"), CONTROL("Control"), DATA("Data");

    private final String label;
    EditorType(String label){ this.label = label; }
    @Override public String toString() { return label; }
}
