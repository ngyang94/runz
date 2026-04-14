package com.ng.runz.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ng.runz.dto.CoordinateDto;
import com.ng.runz.validation.ValidateLocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record CreateRunRequest(
        @ValidateLocalDate
        @NotNull
        @JsonProperty("start_time")
        LocalDateTime startTime,
        @NotNull
        @ValidateLocalDate
        @JsonProperty("end_time")
        LocalDateTime endTime,
        @NotNull
        @Positive
        Double miles,
        @Valid
        @JsonProperty("start_point")
        CoordinateDto startPoint,
        @Valid
        @JsonProperty("end_point")
        CoordinateDto endPoint
) {
}
