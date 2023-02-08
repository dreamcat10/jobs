package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyoung.jobs.dto.ChatRoomDto;
import com.yyoung.jobs.dto.PageDto;
import com.yyoung.jobs.entity.ChatLog;
import com.yyoung.jobs.entity.ChatRoom;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.User;
import com.yyoung.jobs.service.*;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    CommonService commonService;

    @Value("${page.size}")
    Long size;

    @PostMapping("/room/{fromId}/{toId}")
    public Result<String> createRoom(@PathVariable("fromId") Long fromId,@PathVariable("toId") Long toId){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setUser1(fromId);
        chatRoom.setUser2(toId);
        chatRoom.setCreatId(fromId);

        chatRoomService.save(chatRoom);
        return Result.success("成功创建聊天室");
    }

    /**
     * 获取当前用户所有的聊天房间 `
     * @param id 用户id
     * @return
     */
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
            chatRoomDto.setId(chatRoom.getId());
            chatRoomDto.setMine(id);
            chatRoomDto.setOther(id.equals(chatRoom.getUser1()) ? chatRoom.getUser2() : chatRoom.getUser1());
            chatRoomDto.setCreatId(chatRoom.getCreatId());
            Object other = commonService.getOne(chatRoomDto.getOther());

            if (other == null){
                chatRoomDto.setOAvatar("-1.png");
                chatRoomDto.setOName("用户已注销");
            }else {
                if (other instanceof User){
                    User info = (User)other;
                    chatRoomDto.setOAvatar(info.getAvatar());
                    chatRoomDto.setOName(info.getName());
                }
                if (other instanceof Employee){
                    Employee info = (Employee)other;
                    chatRoomDto.setOAvatar(info.getAvatar());
                    chatRoomDto.setOName(info.getName());
                }

            }
            chatRoomDtos.add(chatRoomDto);
        }

        return Result.success(chatRoomDtos);
    }

    /**
     * 拿到当前聊天室里的聊天记录（分页查询）
     * @param id 聊天室ID
     * @return
     */
    @GetMapping("/logs/{id}/{current}")
    public Result<PageDto> chatLogs(@PathVariable("id") Long id, @PathVariable("current") int current){

        Page<ChatLog> page =  new Page<>(current, 10);
        LambdaQueryWrapper<ChatLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatLog::getChatRoomId, id);
        queryWrapper.orderByDesc(ChatLog::getCreateTime);

        Page<ChatLog> logPage = chatLogService.page(page, queryWrapper);

        return Result.success(new PageDto<ChatLog>(logPage.getRecords(),logPage.getTotal(), size));
    }

}
