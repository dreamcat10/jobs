package com.yyoung.jobs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yyoung.jobs.entity.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
