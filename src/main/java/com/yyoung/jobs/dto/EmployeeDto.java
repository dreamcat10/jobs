package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Employee;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDto extends Employee {

    private Integer code;

}
