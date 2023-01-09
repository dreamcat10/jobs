package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {

  private Long id;
  private String name;
  private Long age;
  private String avatar;
  private Integer sex;
  private String degree;
  private String intendedPost;
  private String address;
  private long phone;
  private Long resumeId;
  private String password;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime loggingTime;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;


}
