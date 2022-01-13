package com.workmotion.employeemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.employeemanagement.dto.EmployeeDto;
import com.workmotion.employeemanagement.dto.StatusUpdateDto;
import com.workmotion.employeemanagement.enums.ResponseStatus;
import com.workmotion.employeemanagement.enums.Status;
import com.workmotion.employeemanagement.model.Employee;
import com.workmotion.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl service;

    @InjectMocks
    private EmployeeController controller;

    private Employee employee1, employee2;
    private EmployeeDto employeeDto1, employeeDto2;
    private StatusUpdateDto statusUpdateDto;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        employee1 = new Employee(1L, "fikret", "permanent", "01.05.1993",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), Status.ADDED);
        employee2 = new Employee(2L, "kaya", "parttime", "02.06.1994",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), Status.ADDED);

        employeeDto1 = new EmployeeDto("fikret", "permanent", "01.05.1993");
        employeeDto2 = new EmployeeDto("kaya", "parttime", "02.06.1994");

        statusUpdateDto = new StatusUpdateDto(1L, Status.IN_CHECK);
    }

    @AfterEach
    public void tearDown() {
        employee1 = employee2 = null;
    }

    @Test
    public void createEmployee() throws Exception {
        when(service.addEmployee(any())).thenReturn(employee1);
        mockMvc.perform(post("/create").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(employeeDto1))).
                andExpect(status().isOk());
        verify(service,times(1)).addEmployee(any());
    }

    @Test
    public void createEmployeeError() throws Exception {
        when(service.addEmployee(any())).thenReturn(null);
        mockMvc.perform(post("/create").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(employeeDto1))).
                andExpect(status().isBadRequest());
        verify(service,times(1)).addEmployee(any());
    }

    @Test
    public void updateEmployeeStatus() throws Exception {
        when(service.updateStatus(any())).thenReturn(ResponseStatus.SUCCESS);
        mockMvc.perform(post("/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(statusUpdateDto))).
                andExpect(status().isOk());
        verify(service,times(1)).updateStatus(any());
    }

    @Test
    public void updateEmployeeStatusNoEmployee() throws Exception {
        statusUpdateDto.setEmployeeId(3L);

        when(service.updateStatus(any())).thenReturn(ResponseStatus.NO_EMPLOYEE);
        mockMvc.perform(post("/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(statusUpdateDto))).
                andExpect(status().isBadRequest());
        verify(service,times(1)).updateStatus(any());
    }

    @Test
    public void updateEmployeeStatusWrongRequest() throws Exception {
        statusUpdateDto.setEmployeeId(3L);

        when(service.updateStatus(any())).thenReturn(ResponseStatus.WRONG_REQUEST);
        mockMvc.perform(post("/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(statusUpdateDto))).
                andExpect(status().isBadRequest());
        verify(service,times(1)).updateStatus(any());
    }

    @Test
    public void updateEmployeeStatusOtherError() throws Exception {
        statusUpdateDto.setEmployeeId(3L);

        when(service.updateStatus(any())).thenReturn(ResponseStatus.OTHER);
        mockMvc.perform(post("/update").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(statusUpdateDto))).
                andExpect(status().isBadRequest());
        verify(service,times(1)).updateStatus(any());
    }

    @Test
    public void fetchEmployee() throws Exception {
        when(service.fetchEmployee(employee1.getId())).thenReturn(employee1);
        mockMvc.perform(get("/fetch/1")
                        .content(asJsonString(employee1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void fetchEmployeeNotFound() throws Exception {
        mockMvc.perform(get("/fetch/3"))
                .andExpect(status().isBadRequest());
    }


    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
