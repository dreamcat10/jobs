package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.StartLog;
import com.yyoung.jobs.mapper.StartLogMapper;
import com.yyoung.jobs.service.StartLogService;
import org.springframework.stereotype.Service;

@Service
public class StartLogServiceImpl extends ServiceImpl<StartLogMapper, StartLog> implements StartLogService {


    @Override
    public Boolean isStart(Long id, Long newsId) {
        LambdaQueryWrapper<StartLog> startQueryWrapper = new LambdaQueryWrapper<>();
        startQueryWrapper.eq(StartLog::getNewsId, newsId);
        startQueryWrapper.eq(StartLog::getStartId, id);

        long count = this.count(startQueryWrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
