package view.dto;

public record ConsumerSlotVM(
    int index,
    boolean busy,
    int productID,
    float progress
) {
}
