package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyoung.jobs.dto.ChatRoomDto;
import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.entity.ChatRoom;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.service.ChatLogService;
import com.yyoung.jobs.service.ChatRoomService;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.UserService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatLogService chatLogService;

    @PostMapping("/room/{fromId}/{toId}")
    public Result<String> createRoom(@PathVariable("fromId") Long fromId,@PathVariable("toId") Long toId){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser1(fromId);
        chatRoom.setUser2(toId);
        chatRoom.setCreatId(fromId);

        chatRoomService.save(chatRoom);
        return Result.success("成功创建聊天室");
    }
    @GetMapping("/rooms/{id}")
    public Result<List<ChatRoomDto>> chatRooms(@PathVariable Long id){

        LambdaQueryWrapper<ChatRoom> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatRoom::getUser1, id);
        queryWrapper.or();
        queryWrapper.eq(ChatRoom::getUser2, id);

        List<ChatRoom> chatRooms = chatRoomService.list(queryWrapper);

        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            BeanUtils.copyProperties(chatRoom, chatRoomDto);

            List<ChatLog> chatLogs = chatLogService.listLogs(chatRoom.getId(),id);
            chatRoomDto.setChatLog(chatLogs);
            chatRoomDtos.add(chatRoomDto);
        }

        return Result.success(chatRoomDtos);
    }

}
