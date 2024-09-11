package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.coordinate.CoordinateFactory;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimension;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
@Slf4j
public class CalculateController {
    private final TariffCalculateUseCase tariffCalculateUseCase;
    private final CurrencyFactory currencyFactory;
    private final CoordinateFactory coordinateFactory;

    @PostMapping
    @Operation(summary = "Расчет стоимости по упаковкам груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    public CalculatePackagesResponse calculate(
            @Valid @RequestBody CalculatePackagesRequest request) {
            var packs = request.packages().stream()
                    .map(cargoPackage -> new Pack(
                            new Weight(cargoPackage.weight()),
                            new Dimension(cargoPackage.length()),
                            new Dimension(cargoPackage.width()),
                            new Dimension(cargoPackage.height())
                    ))
                    .toList();

            var shipment = new Shipment(
                    packs,
                    coordinateFactory.create(request.destination().latitude(), request.destination().longitude()),
                    coordinateFactory.create(request.departure().latitude(), request.departure().longitude()),
                    currencyFactory.create(request.currencyCode()));
            var calculatedPrice = tariffCalculateUseCase.calc(shipment);
            var minimalPrice = tariffCalculateUseCase.minimalPrice();

            log.info("Shipment price is {} {}", calculatedPrice.amount(), calculatedPrice.currency().getCode());
            return new CalculatePackagesResponse(calculatedPrice, minimalPrice);
    }
}

