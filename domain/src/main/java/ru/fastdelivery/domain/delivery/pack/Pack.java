package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 */
public record Pack(Weight weight, Dimension length, Dimension width, Dimension height) {

    public Pack(Weight weight) {
        this(weight, Dimension.zero(), Dimension.zero(), Dimension.zero());
    }

    public Pack(Dimension length, Dimension width, Dimension height) {
        this(Weight.zero(), length, width, height);
    }

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));
    private static final Dimension maxDimension = new Dimension(BigInteger.valueOf(1500));

    public Pack {
        if (weight.greaterThan(maxWeight) || length.greaterThan(maxDimension) || width.greaterThan(maxDimension) || height.greaterThan(maxDimension)) {
            throw new IllegalArgumentException("Package weight can't be more than " + maxWeight + " and dimensions " + maxDimension);
        }

        if (length.add(width).add(height).greaterThan(maxDimension)) {
            throw new IllegalArgumentException("Package all dimensions can't be more than " + maxDimension);
        }
    }

    public Dimension volume() {
        BigInteger length = Dimension.roundToNearest50(this.length.value());
        BigInteger width = Dimension.roundToNearest50(this.width.value());
        BigInteger height = Dimension.roundToNearest50(this.height.value());

        return new Dimension(length.multiply(width).multiply(height));
    }
}
