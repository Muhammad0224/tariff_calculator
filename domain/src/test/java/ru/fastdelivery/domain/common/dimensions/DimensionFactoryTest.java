package ru.fastdelivery.domain.common.dimensions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DimensionFactoryTest {

    @ParameterizedTest(name = "Миллиметры = {arguments} -> объект создан")
    @ValueSource(longs = { 0, 1, 100, 10_000 })
    void whenMillimetresGreaterThanZero_thenObjectCreated(long amount) {
        var dimension = new Dimension(BigInteger.valueOf(amount));

        assertNotNull(dimension);
        assertThat(dimension.value()).isEqualByComparingTo(BigInteger.valueOf(amount));
    }

    @ParameterizedTest(name = "Стоимость = {arguments} -> исключение")
    @ValueSource(longs = { -1, -100, -10_000 })
    @DisplayName("Значение стоимости ниже 0.00 -> исключение")
    void whenMillimetresLessThanZero_thenThrowException(long amount) {
        assertThatThrownBy(() -> new Dimension(BigInteger.valueOf(amount)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}