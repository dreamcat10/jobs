package com.yyoung.jobs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyoung.jobs.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
