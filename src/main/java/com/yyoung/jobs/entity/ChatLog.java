package com.yyoung.jobs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatLog {

  private Long id;
  private Long chatRoomId;
  private Long fromId;
  private Long toId;
  private String context;
  private Integer state;
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

}
