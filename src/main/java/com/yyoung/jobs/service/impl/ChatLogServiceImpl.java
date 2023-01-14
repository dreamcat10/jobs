package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.entity.ChatRoom;
import com.yyoung.jobs.mapper.ChatLogMapper;
import com.yyoung.jobs.service.ChatLogService;
import com.yyoung.jobs.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements ChatLogService {

    @Autowired
    ChatRoomService chatRoomService;

    @Override
    public List<ChatLog> listLogs(Long roomId,Long toId) {
        LambdaQueryWrapper<ChatLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatLog::getChatRoomId, roomId);
        List<ChatLog> chatLogs = list(queryWrapper);

        //将未接收的消息过滤
        chatLogs = chatLogs.stream().filter((item) -> {
            return !(item.getToId().equals(toId) && item.getState() == 0);
        }).collect(Collectors.toList());

        return chatLogs;
    }

    @Override
    public void receive(Long toId) {
        LambdaQueryWrapper<ChatRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatRoom::getUser1, toId);
        queryWrapper.or();
        queryWrapper.eq(ChatRoom::getUser2, toId);

        List<ChatRoom> chatRooms = chatRoomService.list(queryWrapper);
        for (ChatRoom chatRoom : chatRooms) {
            Long roomId = chatRoom.getId();
            ChatLog chatLog = new ChatLog();
            chatLog.setState(1);
            update(chatLog,new LambdaQueryWrapper<ChatLog>().eq(ChatLog::getChatRoomId, roomId)
                                                            .eq(ChatLog::getToId, toId)
                                                            .eq(ChatLog::getState, 0));

        }

    }
}
