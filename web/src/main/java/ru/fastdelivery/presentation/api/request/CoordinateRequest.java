package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CoordinateRequest(
        @Schema(description = "Широта", example = "73.398660")
        BigDecimal latitude,

        @Schema(description = "Долгота", example = "55.027532")
        BigDecimal longitude
) {
}
