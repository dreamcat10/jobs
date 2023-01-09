package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    void addEmp(Employee employee);

    Employee getOne(Long phone);
}
