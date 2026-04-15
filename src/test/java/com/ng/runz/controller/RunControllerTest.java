package com.ng.runz.controller;

import com.ng.runz.dto.CoordinateDto;
import com.ng.runz.dto.RoleDto;
import com.ng.runz.dto.RunDto;
import com.ng.runz.dto.UserDto;
import com.ng.runz.mapper.impl.UsersMapper;
import com.ng.runz.model.Coordinate;
import com.ng.runz.model.Role;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;
import com.ng.runz.repository.RunRepository;
import com.ng.runz.service.JwtUtilService;
import com.ng.runz.service.RunService;
import com.ng.runz.service.UserService;
import com.ng.runz.service.impl.RunServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.data.relational.core.sql.When;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@WebMvcTest(RunController.class)
@AutoConfigureMockMvc(addFilters = false)
class RunControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RunService runService;

    @MockitoBean
    private JwtUtilService jwtUtilService;

    @MockitoBean
    private RunServiceImpl runServiceImpl;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UsersMapper usersMapper;



    private List<RunDto> runDtos = new ArrayList<RunDto>();
    private UserDto userDto;

    @BeforeEach
    void setup(){
        userDto = new UserDto(
                1L,
                "test",
                "test@email.com",
                "password",
                new ArrayList<RoleDto>()
        );
        runDtos.add(new RunDto(
                LocalDateTime.parse("2026-04-15T12:00:00"),
                LocalDateTime.parse("2026-04-15T12:30:00"),
                12.0,
                new CoordinateDto(
                        1.567,
                        123.123
                ),
                new CoordinateDto(
                        1.567,
                        123.123
                )
        ));
    }

    @Test
    public void testThatGetAllRunReturnStatusCode200() throws Exception {
        Pageable pageable = PageRequest.of(1,20);
        Page<RunDto> runDtoPages= new PageImpl<>(runDtos,pageable,runDtos.size());
        when(runService.getAllByUserId(userDto.getId(),pageable)).thenReturn(runDtoPages);
        mockMvc.perform(MockMvcRequestBuilders.get("/run"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatCreateRunReturn201StatusCode() throws Exception {
        RunDto newRunDto = new RunDto(
                LocalDateTime.parse("2026-04-16T12:00:00"),
                LocalDateTime.parse("2026-04-16T12:30:00"),
                12.0,
                new CoordinateDto(
                        1.567,
                        123.123
                ),
                new CoordinateDto(
                        1.567,
                        123.123
                )
        );

        when(runService.createNewRunRecord(newRunDto,userDto)).thenReturn(newRunDto);
        String newRunDtoStr = objectMapper.writeValueAsString(newRunDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/run").contentType("application/json").content(newRunDtoStr)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }
}