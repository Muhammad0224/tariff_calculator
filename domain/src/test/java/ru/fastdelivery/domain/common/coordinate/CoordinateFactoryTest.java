package ru.fastdelivery.domain.common.coordinate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.currency.CurrencyPropertiesProvider;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoordinateFactoryTest {

    CoordinatePropertiesProvider mockProvider = mock(CoordinatePropertiesProvider.class);
    CoordinateFactory factory = new CoordinateFactory(mockProvider);

    @Test
    @DisplayName("Широта или долгота NULL -> исключение")
    void whenCodeIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, null));
    }

    @Test
    @DisplayName("Координаты вне диапазона -> исключение")
    void whenCoordinatesOutOfRange_thenThrowException() {
        when(mockProvider.isAvailableCoordinate(BigDecimal.valueOf(25.986756), BigDecimal.valueOf(67.7867767))).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(BigDecimal.valueOf(25.986756), BigDecimal.valueOf(67.7867767)));
    }

    @Test
    @DisplayName("Координаты корректно -> новый объект")
    void whenCoordinatesAreCorrect_thenObjectCreated() {
        when(mockProvider.isAvailableCoordinate(BigDecimal.valueOf(57.986756), BigDecimal.valueOf(67.7867767))).thenReturn(true);

        assertThat(factory.create(BigDecimal.valueOf(57.986756), BigDecimal.valueOf(67.7867767))).isNotNull();
    }
}