package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.mapper.UserMapper;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PostService postService;

    @Autowired
    EmployeeService employeeService;

    @Override
    public List<User> getUsers() {
        List<User> list = userMapper.selectList(null);
        return list;
    }

    @Override
    public Page<User> pageUser(long current, HttpServletRequest request, LambdaQueryWrapper<User> lambdaQueryWrapper) {
        long phone = (long) request.getSession().getAttribute("emp");

        Long empId = employeeService.getOne(phone).getId();
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id", empId);
        List<Post> posts = postService.list(queryWrapper);

        List<String> names = posts.stream().map((post) -> {
            String postName = post.getName();
            return postName;
        }).collect(Collectors.toList());

        Page<User> page = new Page<>(current, 2);
        lambdaQueryWrapper.in(User::getIntendedPost, names);

        userMapper.selectPage(page, lambdaQueryWrapper);
        return page;
    }

    @Override
    public Page<User> pageUser(long current, LambdaQueryWrapper<User> lambdaQueryWrapper) {
        Page<User> page = new Page<>(current, 2);

        userMapper.selectPage(page, lambdaQueryWrapper);

        return page;
    }


}
