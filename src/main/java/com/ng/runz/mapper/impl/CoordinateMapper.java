package com.ng.runz.mapper.impl;

import com.ng.runz.dto.CoordinateDto;
import com.ng.runz.mapper.Mapper;
import com.ng.runz.model.Coordinate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CoordinateMapper implements Mapper<Coordinate, CoordinateDto> {

    private final ModelMapper modelMapper;

    public CoordinateMapper() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public CoordinateDto mapTo(Coordinate coordinate) {
        return modelMapper.map(coordinate, CoordinateDto.class);
    }

    @Override
    public Coordinate mapFrom(CoordinateDto coordinateDto) {
        return modelMapper.map(coordinateDto, Coordinate.class);
    }
}
