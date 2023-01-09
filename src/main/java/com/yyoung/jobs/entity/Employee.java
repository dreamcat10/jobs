package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Employee {

  private Long id;
  private String name;
  private Integer sex;
  private Long companyId;
  private String password;
  private long phone;
  private String avatar;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime loggingTime;




}
