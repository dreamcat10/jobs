package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {

    List<User> getUsers();

    Page<User> pageUser(long current, HttpServletRequest request, LambdaQueryWrapper<User> lambdaQueryWrapper);

    Page<User> pageUser(long current, LambdaQueryWrapper<User> lambdaQueryWrapper);
}
