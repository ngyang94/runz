package com.ng.runz.mapper.impl;

import com.ng.runz.dto.RunDto;
import com.ng.runz.mapper.Mapper;
import com.ng.runz.model.Runs;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RunMapper implements Mapper<Runs, RunDto> {

    private final ModelMapper modelMapper;

    public RunMapper() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public RunDto mapTo(Runs runs) {
        return modelMapper.map(runs, RunDto.class);
    }

    @Override
    public Runs mapFrom(RunDto runDto) {
        return modelMapper.map(runDto, Runs.class);
    }
}
