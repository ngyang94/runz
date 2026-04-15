package com.ng.runz.service.impl;

import com.ng.runz.dto.CoordinateDto;
import com.ng.runz.dto.RunDto;
import com.ng.runz.dto.UserDto;
import com.ng.runz.mapper.Mapper;
import com.ng.runz.mapper.impl.CoordinateMapper;
import com.ng.runz.mapper.impl.RunMapper;
import com.ng.runz.mapper.impl.UsersMapper;
import com.ng.runz.model.Coordinate;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;
import com.ng.runz.repository.RunRepository;
import com.ng.runz.repository.RunRepositoryH2;
import com.ng.runz.service.RunService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RunServiceImpl implements RunService {

    private final RunRepository runRepository;
    private final Mapper<Runs, RunDto> runMapper;
    private final Mapper<Users, UserDto> userMapper;
    private final Mapper<Coordinate, CoordinateDto> coordinateMapper;
    public RunServiceImpl(RunRepository runRepository, RunMapper runMapper, UsersMapper userMapper, CoordinateMapper coordinateMapper){
        this.runRepository=runRepository;
        this.runMapper = runMapper;
        this.userMapper=userMapper;
        this.coordinateMapper=coordinateMapper;
    }

    @Override
    public RunDto createNewRunRecord(RunDto runDto, UserDto userDto) {
        Users user = userMapper.mapFrom(userDto);
        Runs run = runMapper.mapFrom(runDto);
        run.setUser(user);
        Runs runCreated = runRepository.save(run);
        return runMapper.mapTo(runCreated);
    }

    @Override
    public Page<RunDto> getAllByUserId(Long userId, Pageable pageable) {
        return runRepository.findAllByUserId(userId,pageable).map(run->{
            RunDto runDto = runMapper.mapTo(run);
            runDto.setStartPointDto(coordinateMapper.mapTo(run.getStartPoint()));
            runDto.setEndPointDto(coordinateMapper.mapTo(run.getEndPoint()));
            return runDto;
        });
    }

    @Override
    public Optional<RunDto> getByRunId(Long runId) {
        return runRepository.findById(runId).map(runMapper::mapTo);
    }
}
