package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;
    private final VolumePriceProvider volumePriceProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesM3 = shipment.volumeAllPackages().m3();
        var minimalPrice = weightPriceProvider.minimalPrice();

        Price weightPrice = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);

        Price volumePrice = volumePriceProvider
                .costPerM3()
                .multiply(volumeAllPackagesM3)
                .max(minimalPrice);

        var basePrice = weightPrice.max(volumePrice);

        BigDecimal distance = shipment.calculateDistance();

        return calculateDistancePrice(basePrice, distance);
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }

    public Price calculateDistancePrice(Price basePrice, BigDecimal distance) {
        if (distance.longValue() > 450) {
            BigDecimal price = basePrice.amount().multiply(BigDecimal.valueOf(distance.longValue() / 450)).setScale(2, RoundingMode.UP);
            return new Price(price, basePrice.currency());
        }
        return basePrice;
    }
}
