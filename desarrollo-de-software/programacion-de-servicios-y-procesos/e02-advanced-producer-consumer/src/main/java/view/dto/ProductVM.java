package view.dto;

public record ProductVM(
    int id,
    String name,
    int price,
    boolean unlocked
) {
}
