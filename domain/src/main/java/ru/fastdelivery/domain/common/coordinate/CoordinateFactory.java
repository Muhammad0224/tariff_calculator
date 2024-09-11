package ru.fastdelivery.domain.common.coordinate;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CoordinateFactory {

    private final CoordinatePropertiesProvider coordinatePropertiesProvider;

    public Coordinate create(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null || !coordinatePropertiesProvider.isAvailableCoordinate(latitude, longitude)) {
            throw new IllegalArgumentException("Coordinates are not available");
        }
        return new Coordinate(latitude, longitude);
    }
}
