package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.Process;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcessDto extends Process {

    private String userName;
    private String userAvatar;
    private String postName;
}
