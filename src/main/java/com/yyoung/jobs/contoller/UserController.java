package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.yyoung.jobs.dto.PageDto;
import com.yyoung.jobs.dto.TermDto;
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

    @Value("${page.size}")
    Long size;

    @Autowired
    UserService userService;

    @Autowired
    ResumeService resumeService;

    //首页推荐的求职者信息
    @PostMapping("/page/{current}")
    public Result<PageDto> page(@PathVariable long current, @RequestBody TermDto termDto){
        log.info("term:{}", termDto);
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (termDto != null)
            queryWrapper = userService.screenTerms(queryWrapper, termDto);

        Page<User> userPage = userService.pageUser(queryWrapper,page);

        return Result.success(new PageDto<User>(page.getRecords(),page.getTotal(),size));
    }

    //首页感兴趣的求职者信息
    //根据当前用户的岗位需求去匹配合适的求职者
    @PostMapping("/page/inter/{current}")
    public Result<PageDto> recommend(@PathVariable long current, HttpServletRequest request, @RequestBody TermDto termDto){

        Page<User> userPage = new Page<>(current, size);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (termDto != null)
            lambdaQueryWrapper = userService.screenTerms(lambdaQueryWrapper, termDto);

        Page<User> page = userService.pageUser(request, lambdaQueryWrapper,userPage);

        return Result.success(new PageDto<User>(page.getRecords(),page.getTotal(),size));
    }
//
//    //首页推荐的求职者信息（筛选条件：登录时间）
//    @GetMapping("/page/time/{current}")
//    public Result<PageDto> pageTime(@PathVariable("current") long current, HttpServletRequest request){
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.orderByDesc(User::getLoggingTime);
//
//        Page<User> userPage = new Page<>(current, size);
//        Page<User> page = userService.pageUser(lambdaQueryWrapper,userPage);
//        return Result.success(new PageDto(page.getRecords(),page.getTotal(),size));
//    }
//
//    //首页推荐的求职者信息（筛选条件：学历）
//    @GetMapping("/page/degree/{current}")
//    public Result<PageDto> pageDegree(@PathVariable long current,
//                                         String degree, HttpServletRequest request){
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(User::getDegree, degree);
//
//        Page<User> userPage = new Page<>(current, size);
//        Page<User> page = userService.pageUser(lambdaQueryWrapper,userPage);
//
//        return Result.success(new PageDto(page.getRecords(),page.getTotal(),size));
//    }
//
//    //首页推荐的求职者信息（筛选条件：地址）
//    @GetMapping("/page/addr/{current}")
//    public Result<PageDto> pageAddr(@PathVariable long current, String address, HttpServletRequest request){
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.like(User::getAddress, address);
//
//        Page<User> userPage = new Page<>(current, size);
//        Page<User> page = userService.pageUser(lambdaQueryWrapper, userPage);
//
//        return Result.success(new PageDto(page.getRecords(),page.getTotal(),size));
//    }

    //根据名字去搜索所有能够匹配的求职者
    @PostMapping("/page/{current}/{name}")
    public Result<PageDto> search(@PathVariable("current") long current, @PathVariable("name") String name, @RequestBody TermDto termDto){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName, name);

        Page<User> userPage = new Page<>(current, size);
        if (termDto != null)
            lambdaQueryWrapper = userService.screenTerms(lambdaQueryWrapper, termDto);

        Page<User> page = userService.pageUser( lambdaQueryWrapper, userPage);

        return Result.success(new PageDto<User>(page.getRecords(),page.getTotal(),size));

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
