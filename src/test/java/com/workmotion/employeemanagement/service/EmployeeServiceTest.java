package com.workmotion.employeemanagement.service;

import com.workmotion.employeemanagement.dto.EmployeeDto;
import com.workmotion.employeemanagement.dto.StatusUpdateDto;
import com.workmotion.employeemanagement.enums.ResponseStatus;
import com.workmotion.employeemanagement.enums.Status;
import com.workmotion.employeemanagement.model.Employee;
import com.workmotion.employeemanagement.repository.EmployeeRepository;
import com.workmotion.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Autowired
    @InjectMocks
    private EmployeeServiceImpl service;

    @Mock
    private EmployeeRepository repository;

    private Employee employee1, employee2;

    @BeforeEach
    public void setUp() {
        employee1 = new Employee(1L, "fikret", "permanent", "01.05.1993",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), Status.ADDED);
        employee2 = new Employee(2L, "kaya", "parttime", "02.06.1994",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), Status.ADDED);
    }

    @AfterEach
    public void tearDown() {
        employee1 = employee2 = null;
    }

    @Test
    public void createEmployee() {
        // given
        EmployeeDto employeeDto = new EmployeeDto("fikret", "permanent", "01.05.1993");

        when(repository.save(any())).thenReturn(employee1);
        Employee employee = service.addEmployee(employeeDto);
        assertEquals(employeeDto.getName(), employee.getName());
        verify(repository,times(1)).save(any());
    }

    @Test
    public void createEmployee2() {
        // given
        EmployeeDto employeeDto = new EmployeeDto("kaya", "parttime", "02.06.1994");

        when(repository.save(any())).thenReturn(employee2);
        Employee employee = service.addEmployee(employeeDto);
        assertEquals(employeeDto.getName(), employee.getName());
        verify(repository,times(1)).save(any());
    }

    @Test
    public void createEmployeeEmptyFields() {
        // given
        EmployeeDto employeeDto = new EmployeeDto("", "", "02.06.1994");

        // when
        Employee employee = service.addEmployee(employeeDto);

        // then
        then(employee).isNull();
    }

    @Test
    public void fetchEmployee() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(employee1));
        assertThat(service.fetchEmployee(employee1.getId())).isEqualTo(employee1);
    }

    @Test
    public void updateEmployeeStateToInCheck() {
        // given
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(1L, Status.IN_CHECK);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(employee1));
        Mockito.when(repository.save(any())).thenReturn(employee1);
        ResponseStatus responseStatus = service.updateStatus(statusUpdateDto);

        // then
        then(responseStatus).isNotNull();
        assertEquals(ResponseStatus.SUCCESS, responseStatus);
        verify(repository,times(1)).save(any());
    }

    @Test
    public void updateEmployeeStateToApproved() {
        // given
        StatusUpdateDto statusUpdateDtoIncheck = new StatusUpdateDto(1L, Status.IN_CHECK);
        StatusUpdateDto statusUpdateDtoApproved = new StatusUpdateDto(1L, Status.APPROVED);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(employee1));
        Mockito.when(repository.save(any())).thenReturn(employee1);
        service.updateStatus(statusUpdateDtoIncheck);
        ResponseStatus responseStatus = service.updateStatus(statusUpdateDtoApproved);

        // then
        then(responseStatus).isNotNull();
        assertEquals(ResponseStatus.SUCCESS, responseStatus);
        verify(repository,times(2)).save(any());
    }

    @Test
    public void updateEmployeeStateToActive() {
        // given
        StatusUpdateDto statusUpdateDtoIncheck = new StatusUpdateDto(1L, Status.IN_CHECK);
        StatusUpdateDto statusUpdateDtoApproved = new StatusUpdateDto(1L, Status.APPROVED);
        StatusUpdateDto statusUpdateDtoActive = new StatusUpdateDto(1L, Status.ACTIVE);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(employee1));
        Mockito.when(repository.save(any())).thenReturn(employee1);
        service.updateStatus(statusUpdateDtoIncheck);
        service.updateStatus(statusUpdateDtoApproved);
        ResponseStatus responseStatus = service.updateStatus(statusUpdateDtoActive);

        // then
        then(responseStatus).isNotNull();
        assertEquals(ResponseStatus.SUCCESS, responseStatus);
        verify(repository,times(3)).save(any());
    }

    @Test
    public void updateEmployeeStateInvalidTransition() {
        // given
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(1L, Status.ACTIVE);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(employee1));
        ResponseStatus responseStatus = service.updateStatus(statusUpdateDto);

        // then
        then(responseStatus).isNotNull();
        assertEquals(ResponseStatus.WRONG_REQUEST, responseStatus);
    }

    @Test
    public void updateEmployeeStateEmployeeNotFound() {
        // given
        StatusUpdateDto statusUpdateDto = new StatusUpdateDto(1L, Status.IN_CHECK);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ResponseStatus responseStatus = service.updateStatus(statusUpdateDto);

        // then
        then(responseStatus).isNotNull();
        assertEquals(ResponseStatus.NO_EMPLOYEE, responseStatus);
    }
}
