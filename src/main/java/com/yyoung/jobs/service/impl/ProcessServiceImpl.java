package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.dto.ProcessDto;
import com.yyoung.jobs.entity.Process;
import com.yyoung.jobs.mapper.ProcessMapper;
import com.yyoung.jobs.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    ProcessMapper processMapper;

    @Override
    public List<ProcessDto> getProcess(Long id) {
        List<ProcessDto> process = processMapper.getProcess(id);
        System.out.println(process);
        return  process;
    }
}
