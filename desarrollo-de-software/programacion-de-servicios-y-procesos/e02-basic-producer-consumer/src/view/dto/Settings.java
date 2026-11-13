package view.dto;

// What the user typed in the settings panel
public record Settings(
        int producers,
        int consumers,
        int resourceTypes,
        int minProduceMs,
        int maxProduceMs,
        int minConsumeMs,
        int maxConsumeMs,
        int produceAmount,
        int consumeAmount,
        int minStock,
        int maxStock,
        boolean stockControl,
        boolean synchronize,
        boolean waitNotify) {
}
