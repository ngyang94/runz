package com.ng.runz.controller;

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
import com.ng.runz.request.CreateRunRequest;
import com.ng.runz.response.ApiResponse;
import com.ng.runz.service.RunService;
import com.ng.runz.service.UserService;
import com.ng.runz.service.impl.RunServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("run")
public class RunController {

    private final RunService runService;
    private final UserService userService;
    private final Mapper<Users,UserDto> userMapper;
    public RunController(RunServiceImpl runServiceImpl, UserService userService, UsersMapper userMapper){
        this.runService=runServiceImpl;
        this.userService=userService;
        this.userMapper=userMapper;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRun(@Valid @RequestBody CreateRunRequest createRunRequest){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = null;
        if (authentication != null) {
            userDetails = (UserDetails) authentication.getPrincipal();
        }
        UserDto userDto = null;
        if (userDetails != null) {
            Users user = userService.getUserByUsername(userDetails.getUsername());
            userDto = userMapper.mapTo(user);
        }

        RunDto runDto = new RunDto(
                createRunRequest.startTime(),
                createRunRequest.endTime(),
                createRunRequest.miles(),
                createRunRequest.startPoint(),
                createRunRequest.endPoint()
        );

        runService.createNewRunRecord(runDto,userDto);
        return ResponseEntity.ok().body(new ApiResponse("success",null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRun(){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = null;
            if (authentication != null) {
                userDetails = (UserDetails) authentication.getPrincipal();
            }
            Users user = null;
            if (userDetails != null) {
                user = userService.getUserByUsername(userDetails.getUsername());
            }
            List<RunDto> runDtoList = null;
            if (user != null) {
                runDtoList = runService.getAllByUserId(user.getId());
            }


            return ResponseEntity.ok().body(new ApiResponse("success", runDtoList));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ApiResponse("error",e.getMessage()));
        }
    }

}
