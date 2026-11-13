package view.dto;

public record ActionsVM(
        int newProducerPrice,
        boolean canAddProducer,
        int upgradePrice,
        int upgradeSlot,
        int nextProductId,
        String nextProductName,
        int nextProductPrice) {
}
