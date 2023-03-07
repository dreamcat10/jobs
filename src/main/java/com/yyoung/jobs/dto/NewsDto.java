package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Floor;
import com.yyoung.jobs.entity.News;
import com.yyoung.jobs.entity.Room;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class NewsDto extends News {

    private List<Remark> remark;
    //当前用户是否对此帖子点赞
    private Boolean isStart;
}

