package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        assertThatThrownBy(() -> new Pack(weight))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenOneDimensionMoreThanMaxDimension_thenThrowException() {
        var length = new Dimension(BigInteger.valueOf(1600));
        var width = new Dimension(BigInteger.valueOf(580));
        var height = new Dimension(BigInteger.valueOf(400));
        assertThatThrownBy(() -> new Pack(length, width, height))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenAllDimensionsMoreThanMaxDimension_thenThrowException() {
        var length = new Dimension(BigInteger.valueOf(600));
        var width = new Dimension(BigInteger.valueOf(580));
        var height = new Dimension(BigInteger.valueOf(740));
        assertThatThrownBy(() -> new Pack(length, width, height))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)));
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    void whenDimensionsLessThanMaxDimension_thenObjectCreated() {
        var length = new Dimension(BigInteger.valueOf(320));
        var width = new Dimension(BigInteger.valueOf(256));
        var height = new Dimension(BigInteger.valueOf(400));

        var actual = new Pack(length, width, height);

        assertThat(actual.length()).isEqualTo(new Dimension(BigInteger.valueOf(320)));
        assertThat(actual.width()).isEqualTo(new Dimension(BigInteger.valueOf(256)));
        assertThat(actual.height()).isEqualTo(new Dimension(BigInteger.valueOf(400)));
    }
}