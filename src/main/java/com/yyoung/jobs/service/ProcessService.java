package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.dto.ProcessDto;
import com.yyoung.jobs.entity.Process;
import com.yyoung.jobs.mapper.ProcessMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ProcessService extends IService<Process> {

    List<ProcessDto> getProcess(Long id);

}
