package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.coordinate.Coordinate;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var packages = List.of(new Pack(weight1), new Pack(weight2));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    void whenSummarizingVolumeOfAllPackages_thenReturnSum() {
        var length1 = new Dimension(BigInteger.valueOf(150L));
        var width1 = new Dimension(BigInteger.valueOf(230L));
        var height1 = new Dimension(BigInteger.valueOf(300L));

        var length2 = new Dimension(BigInteger.valueOf(500L));
        var width2 = new Dimension(BigInteger.valueOf(340L));
        var height2 = new Dimension(BigInteger.valueOf(420L));

        var packages = List.of(new Pack(length1, width1, height1), new Pack(length2, width2, height2));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var volumeOfShipment = shipment.volumeAllPackages();

        assertThat(volumeOfShipment.value()).isEqualByComparingTo(BigInteger.valueOf(81_250_000));
    }

    @Test
    void testCalculateDistance() {

        var shipment = new Shipment(new Coordinate(BigDecimal.valueOf(69.336355), BigDecimal.valueOf(41.328096)), new Coordinate(BigDecimal.valueOf(69.279678), BigDecimal.valueOf(41.311439)));

        var distance = shipment.calculateDistance();

        assertThat(distance).isEqualByComparingTo(BigDecimal.valueOf(7));
    }
}