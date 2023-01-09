package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyoung.jobs.dto.UserDto;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.entity.Resume;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.service.ResumeService;
import com.yyoung.jobs.service.UserService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ResumeService resumeService;

    //首页推荐的求职者信息
    @GetMapping("/page/{current}")
    public Result<List<User>> page(@PathVariable long current, HttpServletRequest request){

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<User> page = userService.pageUser(current, request, lambdaQueryWrapper);

        return Result.success(page.getRecords());
    }

    //首页推荐的求职者信息（筛选条件：登录时间）
    @GetMapping("/page/time/{current}")
    public Result<List<User>> pageTime(@PathVariable("current") long current, HttpServletRequest request){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.orderByDesc(User::getLoggingTime);
        Page<User> page = userService.pageUser(current, request, lambdaQueryWrapper);

        return Result.success(page.getRecords());
    }

    //首页推荐的求职者信息（筛选条件：学历）
    @GetMapping("/page/degree/{current}")
    public Result<List<User>> pageDegree(@PathVariable long current,
                                         String degree, HttpServletRequest request){
        log.info("url:{}", request.getRequestURI());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(User::getDegree, degree);
        Page<User> page = userService.pageUser(current, request, lambdaQueryWrapper);

        return Result.success(page.getRecords());
    }

    //首页推荐的求职者信息（筛选条件：地址）
    @GetMapping("/page/addr/{current}")
    public Result<List<User>> pageAddr(@PathVariable long current, String address, HttpServletRequest request){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(User::getAddress, address);
        Page<User> page = userService.pageUser(current, request, lambdaQueryWrapper);

        return Result.success(page.getRecords());
    }

    //根据名字去搜索所有能够匹配的求职者
    @GetMapping("/search/{current}")
    public Result<List<User>> search(@PathVariable long current, String name){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(User::getName, name);
        Page<User> page = userService.pageUser(current, lambdaQueryWrapper);

        return Result.success(page.getRecords());
    }

    //求职者信息详情
    @GetMapping("/detail/{id}")
    public Result<UserDto> detail(@PathVariable("id") long userId){
        User user = userService.getById(userId);
        if (user == null){
            return Result.error("用户不存在!");
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);

        Resume resume = resumeService.getById(user.getResumeId());
        userDto.setResume(resume);

        return Result.success(userDto);
    }


}
