package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Room {

  private Long id;
  private Long createId;
  private Long floorId;
  private Long replyId;
  private String content;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;


}
