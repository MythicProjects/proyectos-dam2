package view.dto;

public record StatsVM(
    double resourcePerSec,
    double productionPerSec,
    double consumptionPerSec
) {
}
