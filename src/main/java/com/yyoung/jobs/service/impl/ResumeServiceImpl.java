package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Resume;
import com.yyoung.jobs.mapper.ResumeMapper;
import com.yyoung.jobs.service.ResumeService;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, Resume> implements ResumeService {
}
