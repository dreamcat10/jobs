package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.News;

public interface NewsService extends IService<News> {

    Object getCreate(Long id);
}
