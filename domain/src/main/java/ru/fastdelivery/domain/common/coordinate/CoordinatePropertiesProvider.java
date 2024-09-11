package ru.fastdelivery.domain.common.coordinate;

import java.math.BigDecimal;

public interface CoordinatePropertiesProvider {
    boolean isAvailableCoordinate(BigDecimal latitude, BigDecimal longitude);
}
