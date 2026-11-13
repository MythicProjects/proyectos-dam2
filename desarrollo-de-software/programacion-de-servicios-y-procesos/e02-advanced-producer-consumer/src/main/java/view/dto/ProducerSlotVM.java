package view.dto;

public record ProducerSlotVM(
    int index,
    boolean open,
    int productID,
    int level,
    double cycleSeconds,
    float progress
) {
}
