package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Resume;
import com.yyoung.jobs.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {

    private Resume resume;
}
