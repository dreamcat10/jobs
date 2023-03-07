package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Company;
import com.yyoung.jobs.entity.Post;
import lombok.Data;

@Data
public class PostDto extends Post {

    private String createName;
    private String createAvatar;
    private Company company;

}
