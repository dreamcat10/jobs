package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Company{

  private Long id;
  private String name;
  private String address;
  private String content;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;


}
