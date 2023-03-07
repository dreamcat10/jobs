package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yyoung.jobs.dto.ProcessDto;
import com.yyoung.jobs.entity.Process;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.service.ProcessService;
import com.yyoung.jobs.service.UserService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService processService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    //拿到当前招聘者的工作概述
    @GetMapping("/summary/{id}")
    public Result<int[]> getProcessSummary(@PathVariable Long id){

        int[] list = {0,0,0,0};

        LambdaQueryWrapper<Process> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Process::getEmpId, id);

        List<Process> processes = processService.list(queryWrapper);

       if (processes != null){
           for (Process process : processes) {
               if (process.getConnect() != 0){
                   list[0] += 1;
               }
               if (process.getResume() != 0){
                   list[1] += 1;
               }
               if (process.getInterview() != 0){
                   list[2] += 1;
               }
               if (process.getEmployment() != 0){
                   list[3] += 1;
               }
           }
       }

        return Result.success(list);
    }

    @GetMapping("/list/{id}")
    public Result<List<ProcessDto>> getProcess(@PathVariable() Long id){

        List<ProcessDto> list = processService.getProcess(id);

        log.info("list:{}",list);

        return Result.success(list);
    }
}
