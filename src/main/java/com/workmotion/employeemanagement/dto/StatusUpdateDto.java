package com.workmotion.employeemanagement.dto;

import com.workmotion.employeemanagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusUpdateDto {

    private Long employeeId;
    private Status status;
}
