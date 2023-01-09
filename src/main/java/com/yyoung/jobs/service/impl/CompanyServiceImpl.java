package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Company;
import com.yyoung.jobs.mapper.CompanyMapper;
import com.yyoung.jobs.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Autowired
    CompanyMapper companyMapper;
}
