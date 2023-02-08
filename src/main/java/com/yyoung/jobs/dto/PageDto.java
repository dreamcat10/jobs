package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDto<T> {

    private List<T> recodes;
    private Long total;
    private Long size;
}
