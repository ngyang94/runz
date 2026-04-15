package com.ng.runz.service;

import com.ng.runz.dto.RunDto;
import com.ng.runz.dto.UserDto;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RunService {

    RunDto createNewRunRecord(RunDto runDto, UserDto userDto);

    public Page<RunDto> getAllByUserId(Long userId, Pageable pageable);
    public Optional<RunDto> getByRunId(Long runId);
}
