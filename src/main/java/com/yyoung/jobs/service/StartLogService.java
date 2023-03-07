package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.StartLog;

public interface StartLogService extends IService<StartLog> {

    Boolean isStart(Long id, Long newsId);
}
