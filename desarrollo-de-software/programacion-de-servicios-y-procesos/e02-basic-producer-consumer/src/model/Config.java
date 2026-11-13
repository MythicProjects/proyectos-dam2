package model;

// All the simulation parameters, chosen by the user before Start
public record Config(
        int producers,
        int consumers,
        int resourceTypes,
        int minProduceMs,
        int maxProduceMs,
        int minConsumeMs,
        int maxConsumeMs,
        int produceAmount,     // units added on each production
        int consumeAmount,     // units removed on each consumption
        int minStock,
        int maxStock,
        boolean stockControl,  // false = limits are ignored
        boolean synchronize,   // false = no synchronized, race conditions appear
        boolean waitNotify) {  // true = wait/notify, false = sleep and retry
}
