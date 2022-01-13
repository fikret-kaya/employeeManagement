package com.workmotion.employeemanagement.model;

import com.workmotion.employeemanagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String contractInfo;
    private String age;
    private Date createdDate;
    private Date lastUpdated;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
