package ru.fastdelivery.domain.common.dimensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public record Dimension(BigInteger value) implements Comparable<Dimension>{

    public Dimension {
        if (isLessThanZero(value)) {
            throw new IllegalArgumentException("Dimension cannot be below Zero!");
        }
    }

    private static boolean isLessThanZero(BigInteger value) {
        return BigInteger.ZERO.compareTo(value) > 0;
    }

    public static Dimension zero() {
        return new Dimension(BigInteger.ZERO);
    }

    public BigDecimal m3() {
        return new BigDecimal(value)
                .divide(BigDecimal.valueOf(1_000_000_000 ), 100, RoundingMode.HALF_UP);
    }

    public boolean greaterThan(Dimension d) {
        return value().compareTo(d.value()) > 0;
    }

    public Dimension add(Dimension additionalDimension) {
        return new Dimension(this.value.add(additionalDimension.value));
    }

    public static BigInteger roundToNearest50(BigInteger value) {
        BigInteger fifty = new BigInteger("50");
        BigInteger twentyFive = new BigInteger("25");

        // value + 25, then divide by 50, and multiply by 50
        return value.add(twentyFive).divide(fifty).multiply(fifty);
    }

    @Override
    public int compareTo(Dimension o) {
        return o.value().compareTo(value());
    }
}
