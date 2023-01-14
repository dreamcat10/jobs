package com.yyoung.jobs.dto;

import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.entity.ChatRoom;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDto extends ChatRoom {

    private List<ChatLog> chatLog;

}
