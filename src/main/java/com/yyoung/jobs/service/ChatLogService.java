package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.ChatLog;

import java.util.List;

public interface ChatLogService extends IService<ChatLog> {

    List<ChatLog> listLogs(Long roomId, Long toId);

    void receive(Long toId);
}
