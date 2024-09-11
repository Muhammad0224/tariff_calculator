package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final VolumePriceProvider volumePriceProvider = mock(VolumePriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider, volumePriceProvider);

    @Test
    @DisplayName("Расчет стоимости доставки -> успешно")
    void whenCalculatePrice_thenReturnWeightPrice() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerM3 = new Price(BigDecimal.valueOf(150), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerM3()).thenReturn(pricePerM3);

        var shipment = new Shipment(List.of(new Pack(
                new Weight(BigInteger.valueOf(1200)),
                new Dimension(BigInteger.valueOf(200)),
                new Dimension(BigInteger.valueOf(250)),
                new Dimension(BigInteger.valueOf(250))
        )),
                new CurrencyFactory(code -> true).create("RUB"));
        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки -> успешно")
    void whenCalculatePrice_thenReturnVolumePrice() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerM3 = new Price(BigDecimal.valueOf(5000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(volumePriceProvider.costPerM3()).thenReturn(pricePerM3);

        var shipment = new Shipment(List.of(new Pack(
                new Weight(BigInteger.valueOf(1200)),
                new Dimension(BigInteger.valueOf(200)),
                new Dimension(BigInteger.valueOf(500)),
                new Dimension(BigInteger.valueOf(450))
        )),
                new CurrencyFactory(code -> true).create("RUB"));
        var expectedPrice = new Price(BigDecimal.valueOf(225), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }

    @ParameterizedTest
    @ValueSource(longs = {100, 200})
    @DisplayName("Расчет стоимости доставки с дистанции (дистанция меньше чем 450) -> успешно")
    void whenDistanceLowerThanMinimum_thenReturnBasePrice(long distance) {
        Price basePrice = new Price(BigDecimal.valueOf(1500), currency);

        assertThat(tariffCalculateUseCase.calculateDistancePrice(basePrice, BigDecimal.valueOf(distance))).isEqualTo(basePrice);
    }

    @ParameterizedTest
    @ValueSource(longs = {500, 750})
    @DisplayName("Расчет стоимости доставки с дистанции (дистанция больше чем 450) -> успешно")
    void whenDistanceHigherThanMinimum_thenReturnCalculatedPrice(long distance) {
        Price basePrice = new Price(BigDecimal.valueOf(1500), currency);

        var expectedPrice = new Price(basePrice.amount().multiply(BigDecimal.valueOf(distance / 450)).setScale(2, RoundingMode.UP), currency);

        assertThat(tariffCalculateUseCase.calculateDistancePrice(basePrice, BigDecimal.valueOf(distance))).isEqualTo(expectedPrice);
    }
}