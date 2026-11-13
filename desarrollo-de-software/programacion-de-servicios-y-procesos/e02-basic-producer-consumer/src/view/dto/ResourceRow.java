package view.dto;

// One row of the table: the state of one resource type
public record ResourceRow(
        int id,
        int stock,
        long expected,
        long produced,
        long consumed) {
}
