package com.workmotion.employeemanagement.service.impl;

import com.workmotion.employeemanagement.dto.EmployeeDto;
import com.workmotion.employeemanagement.enums.ResponseStatus;
import com.workmotion.employeemanagement.enums.Status;
import com.workmotion.employeemanagement.dto.StatusUpdateDto;
import com.workmotion.employeemanagement.model.Employee;
import com.workmotion.employeemanagement.repository.EmployeeRepository;
import com.workmotion.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public Employee addEmployee(EmployeeDto employeeDto) {
        try {
            if (employeeDto.getName().isEmpty() ||
                    employeeDto.getContractInfo().isEmpty() || employeeDto.getAge().isEmpty()) {
                logger.error("Bad create employee request: Empty field(s)");
                return null;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Bad create employee request: Missing field(s)");
            return null;
        }

        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setContractInfo(employeeDto.getContractInfo());
        employee.setAge(employeeDto.getAge());
        employee.setCreatedDate(new Date(System.currentTimeMillis()));
        employee.setLastUpdated(employee.getCreatedDate());
        employee.setStatus(Status.ADDED);

        return repository.save(employee);
    }

    @Override
    public ResponseStatus updateStatus(StatusUpdateDto statusUpdateDto) {
        try {
            if (statusUpdateDto.getEmployeeId() == null || statusUpdateDto.getStatus() == null) {
                logger.error("Bad update employee status request: Missing field(s)");
                return ResponseStatus.OTHER;
            }
        } catch (Exception e) {
            logger.error("Bad update employee status request: " + e.getMessage());
            return null;
        }

        Employee employee = fetchEmployee(statusUpdateDto.getEmployeeId());
        if (employee == null) {
            logger.error("Employee not found while updating the status of employee id: "
                    + statusUpdateDto.getEmployeeId());
            return ResponseStatus.NO_EMPLOYEE;
        }

        boolean isWrongRequest = false;
        if (employee.getStatus() == Status.ADDED &&
                statusUpdateDto.getStatus() != Status.IN_CHECK) {
            isWrongRequest = true;
        } else if (employee.getStatus() == Status.IN_CHECK &&
                statusUpdateDto.getStatus() != Status.APPROVED) {
            isWrongRequest = true;
        } else if (employee.getStatus() == Status.APPROVED &&
                statusUpdateDto.getStatus() == Status.ADDED) {
            isWrongRequest = true;
        } else if (employee.getStatus() == Status.ACTIVE) {
            isWrongRequest = true;
        }

        if (isWrongRequest) {
            logger.error("Transition order error occured while the updating status of employee id: "
                    + statusUpdateDto.getEmployeeId());
            return ResponseStatus.WRONG_REQUEST;
        }

        employee.setStatus(statusUpdateDto.getStatus());
        employee.setLastUpdated(new Date(System.currentTimeMillis()));
        if (repository.save(employee) == null) {
            logger.error("Unrecognized error occured while updating the status of employee id: "
                    + statusUpdateDto.getEmployeeId());
            return ResponseStatus.OTHER;
        }

        logger.info("Status updated for Employee id: " + statusUpdateDto.getEmployeeId() +
                " updated status: " + statusUpdateDto.getStatus());
        return ResponseStatus.SUCCESS;
    }

    @Override
    public Employee fetchEmployee(Long id) {
        return repository.findById(id).orElse(null);
    }
}
