package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yyoung.jobs.entity.Company;
import com.yyoung.jobs.service.CompanyService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//公司操作
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Autowired
    CompanyService companyService;

    //获取公司信息
    @GetMapping("/get/{id}")
    public Result<Company> get(@PathVariable long id){

        Company company = companyService.getById(id);
        return Result.success(company);
    }


    @GetMapping("/search")
    public Result<List<Company>> search(@Param("name") String name){
        log.info("name:{}",name);
        LambdaQueryWrapper<Company> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Company::getName, name);

        List<Company> list = companyService.list(queryWrapper);

        return Result.success(list);
    }
}
