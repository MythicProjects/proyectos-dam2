package view.dto;

import java.util.List;

public record  GameSnapshot (
    int money,
    int day,
    int stock,
    StatsVM stats,
    HistogramVM histogram,
    List<ProducerSlotVM> producers,
    List<ConsumerSlotVM> consumers,
    int queue,
    List<ProductVM> products,
    ActionsVM actions,
    PromoVM promo
){
    // ------------------------------------------------------------------
    // Defensive copies
    // ------------------------------------------------------------------
    public GameSnapshot{
        producers = List.copyOf(producers);
        consumers = List.copyOf(consumers);
        products = List.copyOf(products);
    }
}
