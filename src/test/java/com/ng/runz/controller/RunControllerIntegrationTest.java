package com.ng.runz.controller;

import com.ng.runz.config.JwtFilter;
import com.ng.runz.model.Coordinate;
import com.ng.runz.model.Runs;
import com.ng.runz.model.Users;
import com.ng.runz.repository.RunRepository;
import com.ng.runz.repository.UserRepository;
import com.ng.runz.service.JwtUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Import({JwtFilter.class,JwtUtilService.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RunControllerIntegrationTest {

    private String jwtToken;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RunRepository runRepository;

    @Autowired
    private JwtUtilService jwtUtilService;

    @BeforeEach
    void setup(){
        if((long) runRepository.findAll().size() ==0){

            Users user = new Users("test","test@email.com","password");
            Users userCreated =userRepository.save(user);
            Runs run = new Runs(
                    LocalDateTime.parse("2026-04-15T12:00:00"),
                    LocalDateTime.parse("2026-04-15T12:30:00"),
                    12.0,
                    new Coordinate(1.123,123.123),
                    new Coordinate(1.123,123.223));
            run.setUser(userCreated);
            runRepository.save(run);

            jwtToken = jwtUtilService.generateJwtToken(user);
        }

    }

    @Test
    public void testThatGetAllRunReturn200StatusCode() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/run").header("Authorization","Bearer "+jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatGetAllRunReturnCorrectResult() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/run").header("Authorization","Bearer "+jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].start_time").value("2026-04-15T12:00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].end_time").value("2026-04-15T12:30:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].miles").value(12.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].start_point.longitude").value("1.123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].start_point.latitude").value("123.123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].end_point.longitude").value("1.123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].end_point.latitude").value("123.223"));
    }

}