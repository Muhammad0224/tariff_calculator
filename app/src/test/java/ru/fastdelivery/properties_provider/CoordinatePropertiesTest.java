package ru.fastdelivery.properties_provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.properties.provider.CoordinateProperties;
import ru.fastdelivery.properties.provider.CurrencyProperties;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoordinatePropertiesTest {

    @Test
    @DisplayName("Когда координаты находятся в диапазоне -> true")
    void whenCoordinatesAreInRange() {
        CoordinateProperties properties = new CoordinateProperties();
        properties.setRangeLatitude(new CoordinateProperties.RangeCoordinate(30, 60));
        properties.setRangeLongitude(new CoordinateProperties.RangeCoordinate(30, 87));

        assertTrue(properties.isAvailableCoordinate(BigDecimal.valueOf(45.9879), BigDecimal.valueOf(78.66655)));
    }

    @Test
    @DisplayName("Когда координаты находятся вне диапазоне -> false")
    void whenCoordinatesAreNotInRange() {
        CoordinateProperties properties = new CoordinateProperties();
        properties.setRangeLatitude(new CoordinateProperties.RangeCoordinate(30, 60));
        properties.setRangeLongitude(new CoordinateProperties.RangeCoordinate(30, 87));

        assertFalse(properties.isAvailableCoordinate(BigDecimal.valueOf(22.4555555), BigDecimal.valueOf(78.66655)));
    }
}