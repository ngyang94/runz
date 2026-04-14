package com.ng.runz.dto;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunDto {
    Long id;
    @JsonProperty("start_time")
    LocalDateTime startTime;
    @JsonProperty("end_time")
    LocalDateTime endTime;
    Double miles;

    @JsonProperty("start_point")
    CoordinateDto startPointDto;
    @JsonProperty("end_point")
    CoordinateDto endPointDto;

    public RunDto(LocalDateTime startTime, LocalDateTime endTime, Double miles, CoordinateDto startPointDto, CoordinateDto endPointDto) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.miles = miles;
        this.startPointDto = startPointDto;
        this.endPointDto = endPointDto;
    }
}
