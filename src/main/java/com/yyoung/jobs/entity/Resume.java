package com.yyoung.jobs.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Resume {

  private Long id;
  private Long userId;
  private String educationalExp;
  private String awardExp;
  private String projectExp;
  private String workExp;
  private String resumeAddr;


}
