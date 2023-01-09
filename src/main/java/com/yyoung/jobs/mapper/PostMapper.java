package com.yyoung.jobs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyoung.jobs.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
}
