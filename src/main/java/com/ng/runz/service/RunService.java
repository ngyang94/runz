package com.ng.runz.service;

import com.ng.runz.dto.RunDto;
import com.ng.runz.dto.UserDto;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;

import java.util.List;
import java.util.Optional;

public interface RunService {

    RunDto createNewRunRecord(RunDto runDto, UserDto userDto);

    public List<RunDto> getAllByUserId(Long userId);
    public Optional<RunDto> getByRunId(Long runId);
}
