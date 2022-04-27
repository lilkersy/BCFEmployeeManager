package com.example.organogram.web;

import com.example.organogram.model.Employee;
import com.example.organogram.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RandomBeansExtension.class)
public class TestEmployeeResource {
    private final static String BASE_URL = "/api/employees";
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "admin")
    public void testAddEmployee(@Random Employee employee) throws Exception {
        given(employeeService.addEmployee(any(Employee.class), isNull())).willReturn(employee);
        mvc.perform(post(BASE_URL + "/add")
                .content(new ObjectMapper().writeValueAsString(employee))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.id", notNullValue()));
    }
}
