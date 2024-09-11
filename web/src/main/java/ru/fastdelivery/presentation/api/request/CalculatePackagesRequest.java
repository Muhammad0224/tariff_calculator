package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(
        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4056.45, \"length\": 700, \"width\": 350, \"height\": 579}]")
        @NotNull
        @NotEmpty
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull
        String currencyCode,

        @Schema(description = "Координаты пункта получения", example = "{\"latitude\": 73.398660, \"longitude\": 55.027532}")
        @NotNull
        CoordinateRequest destination,

        @Schema(description = "Координаты пункта отправления", example = "{\"latitude\": 73.398660, \"longitude\": 55.027532}")
        @NotNull
        CoordinateRequest departure
) {
}
