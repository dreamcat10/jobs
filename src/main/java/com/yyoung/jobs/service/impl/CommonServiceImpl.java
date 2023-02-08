package com.yyoung.jobs.service.impl;

import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.service.CommonService;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;


    @Override
    public Object getOne(Long id) {

        User user = userService.getById(id);
        if (user == null){
            Employee emp = employeeService.getById(id);
            return emp;
        }
        return user;

    }
}
