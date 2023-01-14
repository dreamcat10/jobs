package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.ChatRoom;
import com.yyoung.jobs.mapper.ChatRoomMapper;
import com.yyoung.jobs.service.ChatRoomService;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper, ChatRoom> implements ChatRoomService {

}
