package view.dto;

public record PromoVM(
    int price,
    boolean available,
    int durationSec
) {
}
