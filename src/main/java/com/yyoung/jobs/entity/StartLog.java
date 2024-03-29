package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartLog {

  private Long id;
  private Long startId;
  private Long newsId;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;


}
