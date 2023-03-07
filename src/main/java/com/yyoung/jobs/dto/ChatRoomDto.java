package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.entity.ChatRoom;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDto {

    private Long id;
    private Long mineId;
    private Long otherId;
    private String oAvatar;
    private String oName;
    private Long creatId;

}
