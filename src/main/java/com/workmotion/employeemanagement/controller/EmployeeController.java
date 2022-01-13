package com.workmotion.employeemanagement.controller;

import com.google.gson.Gson;
import com.workmotion.employeemanagement.dto.EmployeeDto;
import com.workmotion.employeemanagement.dto.StatusUpdateDto;
import com.workmotion.employeemanagement.enums.ResponseStatus;
import com.workmotion.employeemanagement.model.Employee;
import com.workmotion.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee employee = employeeService.addEmployee(employeeDto);
        if (employee != null) {
            logger.info("Employee with id: " + employee.getId() + " added to the database");
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson("Employee not added to the database"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateEmployeeStatus(@RequestBody StatusUpdateDto statusUpdateDto) {
        ResponseStatus responseStatus = employeeService.updateStatus(statusUpdateDto);
        if (responseStatus == ResponseStatus.SUCCESS) {
            return ResponseEntity.ok().body(new Gson().toJson("Status successfully updated"));
        } else {
            String responseMessage = "";
            if (responseStatus == ResponseStatus.NO_EMPLOYEE) {
                responseMessage = "Employee not found!";
            } else if (responseStatus == ResponseStatus.WRONG_REQUEST) {
                responseMessage = "Transition not allowed!";
            } else if (responseStatus == ResponseStatus.OTHER) {
                responseMessage = "Bad request!";
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson(responseMessage));
        }
    }


    @GetMapping("/fetch/{employeeId}")
    public ResponseEntity<Object> fetchEmployeeDetails(@PathVariable(value="employeeId") Long id) {
        Employee employee = employeeService.fetchEmployee(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            logger.error("Employee not found for id: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson("Employee not found"));
        }
    }

}
