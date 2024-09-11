package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.coordinate.Coordinate;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages,
        Coordinate destination,
        Coordinate departure,
        Currency currency
) {
    public Shipment(List<Pack> packages, Currency currency) {
        this(packages, new Coordinate(BigDecimal.ZERO, BigDecimal.ZERO), new Coordinate(BigDecimal.ZERO, BigDecimal.ZERO), currency);
    }

    public Shipment(Coordinate destination, Coordinate departure) {
        this(new ArrayList<>(), destination, departure, null);
    }

    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }

    public Dimension volumeAllPackages() {
        return packages.stream()
                .map(Pack::volume)
                .reduce(Dimension.zero(), Dimension::add);
    }

    public BigDecimal calculateDistance() {
        double rad = 6372795;

        double departureLatitude = departure.latitude().doubleValue();
        double departureLongitude = departure.longitude().doubleValue();
        double destinationLatitude = destination.latitude().doubleValue();
        double destinationLongitude = destination.longitude().doubleValue();

        double lat1 = Math.toRadians(departureLatitude);
        double lat2 = Math.toRadians(destinationLatitude);
        double long1 = Math.toRadians(departureLongitude);
        double long2 = Math.toRadians(destinationLongitude);

        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;
        double ad = Math.atan2(y, x);
        double dist = ad * rad / 1000;

        return BigDecimal.valueOf(dist).setScale(0, RoundingMode.UP); // kilometer
    }
}
