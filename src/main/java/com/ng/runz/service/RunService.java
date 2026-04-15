package com.ng.runz.service;

import com.ng.runz.dto.RunDto;
import com.ng.runz.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RunService {

    RunDto createNewRunRecord(RunDto runDto, UserDto userDto);

    Page<RunDto> getAllByUserId(Long userId, Pageable pageable);
    Optional<RunDto> getByRunId(Long runId);
}
