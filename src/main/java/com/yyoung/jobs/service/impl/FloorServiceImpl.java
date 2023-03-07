package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Floor;
import com.yyoung.jobs.mapper.FloorMapper;
import com.yyoung.jobs.service.FloorService;
import org.springframework.stereotype.Service;

@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements FloorService {
}
