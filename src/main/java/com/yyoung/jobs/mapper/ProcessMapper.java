package com.yyoung.jobs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyoung.jobs.dto.ProcessDto;
import com.yyoung.jobs.entity.Process;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    List<ProcessDto> getProcess(Long id);
}
