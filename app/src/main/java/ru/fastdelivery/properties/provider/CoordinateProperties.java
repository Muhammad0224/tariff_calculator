package ru.fastdelivery.properties.provider;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.fastdelivery.domain.common.coordinate.CoordinatePropertiesProvider;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties("coordinates")
@Setter
public class CoordinateProperties implements CoordinatePropertiesProvider {

    private RangeCoordinate rangeLatitude;
    private RangeCoordinate rangeLongitude;

    @Override
    public boolean isAvailableCoordinate(BigDecimal latitude, BigDecimal longitude) {
        return (BigDecimal.valueOf(rangeLatitude.min()).compareTo(latitude) < 0 && BigDecimal.valueOf(rangeLatitude.max()).compareTo(latitude) > 0) &&
                (BigDecimal.valueOf(rangeLongitude.min()).compareTo(longitude) < 0 && BigDecimal.valueOf(rangeLongitude.max()).compareTo(longitude) > 0);
    }

    public record RangeCoordinate(long min, long max) {}
}
