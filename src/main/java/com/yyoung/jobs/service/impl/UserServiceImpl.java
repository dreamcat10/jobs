package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.dto.TermDto;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.mapper.UserMapper;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Page<User> pageUser(HttpServletRequest request, LambdaQueryWrapper<User> lambdaQueryWrapper, Page<User> page) {
        long phone = (long) request.getSession().getAttribute("emp");

        Long empId = employeeService.getOne(phone).getId();
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id", empId);
        List<Post> posts = postService.list(queryWrapper);

        List<String> names = posts.stream().map((post) -> {
            String postName = post.getName();
            return postName;
        }).collect(Collectors.toList());

        lambdaQueryWrapper.in(User::getIntendedPost, names);

        userMapper.selectPage(page, lambdaQueryWrapper);
        return page;
    }

    @Override
    public Page<User> pageUser( LambdaQueryWrapper<User> lambdaQueryWrapper,Page<User> page) {

        userMapper.selectPage(page, lambdaQueryWrapper);

        return page;
    }

    //筛选前端传来的有效条件
    @Override
    public LambdaQueryWrapper<User> screenTerms(LambdaQueryWrapper<User> queryWrapper, TermDto termDto) {
        if (StringUtils.hasLength(termDto.getDegree())){
            if (termDto.getDegree().equals("高中及以下")){
                queryWrapper.in(User::getDegree, "高中","初中");
            }else
            queryWrapper.eq(User::getDegree, termDto.getDegree());
        }
        if (StringUtils.hasLength(termDto.getAddr())){
            queryWrapper.like(User::getAddress, termDto.getAddr());
        }
        if (StringUtils.hasLength(termDto.getPost())){
            queryWrapper.eq(User::getIntendedPost, termDto.getPost());
        }
        if (termDto.getTime()){
            queryWrapper.orderByDesc(User::getLoggingTime);
        }

        return queryWrapper;
    }


}
