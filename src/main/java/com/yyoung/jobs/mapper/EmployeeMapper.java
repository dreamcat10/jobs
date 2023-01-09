package com.yyoung.jobs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyoung.jobs.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
