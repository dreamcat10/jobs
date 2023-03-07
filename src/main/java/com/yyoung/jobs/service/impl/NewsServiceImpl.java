package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.News;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.mapper.NewsMapper;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.NewsService;
import com.yyoung.jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    //查询出创建帖子用户的信息
    @Override
    public Object getCreate(Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return employee;
        }

        User user = userService.getById(id);
        if (user != null){
            return user;
        }
        return null;
    }
}
