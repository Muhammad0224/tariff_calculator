package ru.fastdelivery.domain.common.dimensions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DimensionTest {

    @Test
    @DisplayName("Попытка создать отрицательный измерение -> исключение")
    void whenMillimetresBelowZero_thenException() {
        var dimension = new BigInteger("-1");
        assertThatThrownBy(() -> new Dimension(dimension))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalsTypeWidth_same() {
        var dimension = new Dimension(new BigInteger("800"));
        var dimensionSame = new Dimension(new BigInteger("800"));

        assertThat(dimension)
                .isEqualTo(dimensionSame)
                .hasSameHashCodeAs(dimensionSame);
    }

    @Test
    void equalsNull_false() {
        var dimension = new Dimension(new BigInteger("70"));

        assertThat(dimension).isNotEqualTo(null);
    }

    @ParameterizedTest
    @CsvSource({ "1000, 1, -1",
            "199, 199, 0",
            "50, 999, 1" })
    void compareToTest(BigInteger low, BigInteger high, int expected) {
        var dimensionLow = new Dimension(low);
        var dimensionHigh = new Dimension(high);

        assertThat(dimensionLow.compareTo(dimensionHigh))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Добавление положительного измерение -> вес увеличился")
    void whenAddPositiveDimension_thenDimensionIsIncreased() {
        var dimensionBase = new Dimension(new BigInteger("1000"));
        var actual = dimensionBase.add(new Dimension(new BigInteger("3000")));

        assertThat(actual)
                .isEqualTo(new Dimension(new BigInteger("4000")));
    }

    @Test
    @DisplayName("Первый измерение больше второго -> true")
    void whenFirstDimensionGreaterThanSecond_thenTrue() {
        var dimensionBig = new Dimension(new BigInteger("1001"));
        var dimensionSmall = new Dimension(new BigInteger("1000"));

        assertThat(dimensionBig.greaterThan(dimensionSmall)).isTrue();
    }

    @Test
    @DisplayName("Запрос количество м3 -> получено корректное значение")
    void whenGetKilograms_thenReceiveM3() {
        var dimension = new Dimension(new BigInteger("2000500000"));

        var actual = dimension.m3();

        assertThat(actual).isEqualByComparingTo(new BigDecimal("2.0005"));
    }
}