package com.ng.runz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoordinateDto {
    @NotNull
    @Digits(integer = 4,fraction = 8)
    Double longitude;
    @NotNull
    @Digits(integer = 4,fraction = 8)
    Double latitude;
}
