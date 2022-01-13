package com.workmotion.employeemanagement.service;

import com.workmotion.employeemanagement.dto.EmployeeDto;
import com.workmotion.employeemanagement.dto.StatusUpdateDto;
import com.workmotion.employeemanagement.enums.ResponseStatus;
import com.workmotion.employeemanagement.model.Employee;

public interface EmployeeService {

    Employee addEmployee(EmployeeDto employeeDto);
    ResponseStatus updateStatus(StatusUpdateDto statusUpdateDto);
    Employee fetchEmployee(Long id);

}
