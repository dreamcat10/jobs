package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.mapper.EmployeeMapper;
import com.yyoung.jobs.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    //添加一个用户
    @Override
    public void addEmp(Employee employee) {
        employeeMapper.insert(employee);
    }

    //根据手机号去查找用户
    @Override
    public Employee getOne(Long phone) {
        QueryWrapper<Employee> query = new QueryWrapper<>();
        query.eq("phone", phone);
        Employee employee = employeeMapper.selectOne(query);
        return employee;
    }
}
