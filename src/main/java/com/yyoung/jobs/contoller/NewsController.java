package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyoung.jobs.dto.NewsDto;
import com.yyoung.jobs.dto.PageDto;
import com.yyoung.jobs.dto.Remark;
import com.yyoung.jobs.entity.*;
import com.yyoung.jobs.service.FloorService;
import com.yyoung.jobs.service.NewsService;
import com.yyoung.jobs.service.RoomService;
import com.yyoung.jobs.service.StartLogService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private StartLogService startLogService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private RoomService roomService;

    /**
     * 分页获取帖子数
     * @param id
     * @param current
     * @param size
     * @param type 0:点赞量 1:发布时间
     * @return
     */
    @GetMapping("/list/{id}/{current}/{size}/{type}")
    public Result<PageDto<NewsDto>> news(@PathVariable("id") Long id, @PathVariable("current") Long current,
                                      @PathVariable("size") Long size, @PathVariable("type") int type){

        Page<News> page = new Page<>(current, size);

        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        if (type == 0){
            queryWrapper.orderByDesc(News::getStarts);
        }
        if (type == 1){
            queryWrapper.orderByDesc(News::getCreateTime);
        }

        Page<News> newsPage = newsService.page(page, queryWrapper);

        List<News> newsList = newsPage.getRecords();
        List<StartLog> startLogs = startLogService.list(new LambdaQueryWrapper<StartLog>().eq(StartLog::getStartId, id));

        List<Long> newsId = startLogs.stream().map(log -> {
            return log.getNewsId();
        }).collect(Collectors.toList());

        List<NewsDto> newsDtos = newsList.stream().map(item -> {
            NewsDto newsDto = new NewsDto();
            BeanUtils.copyProperties(item, newsDto);
            if (newsId.contains(item.getId())) {
                newsDto.setIsStart(true);
            }else
                newsDto.setIsStart(false);
            return newsDto;
        }).collect(Collectors.toList());

        return Result.success(new PageDto<NewsDto>(newsDtos,newsPage.getTotal(),size));
    }

    /**
     * 获取当前用户创建的帖子
     * @param id 当前用户的id
     * @return
     */
    @GetMapping("/list/{id}")
    public Result<List<News>> newslist(@PathVariable Long id){
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getCreateId, id);

        List<News> list = newsService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 获取指定帖子的详情
     * @param id 当前用户id
     * @param newsId 指定帖子的id
     * @return
     */
    @GetMapping("/detail/{id}/{newsId}")
    public Result<NewsDto> newsDetail(@PathVariable("id") Long id,@PathVariable("newsId") Long newsId){

        News news = newsService.getById(newsId);

        NewsDto newsDto = new NewsDto();
        BeanUtils.copyProperties(news, newsDto);

        Boolean isStart = startLogService.isStart(id, newsId);
        newsDto.setIsStart(isStart);

        List<Floor> floorList = floorService.list(new LambdaQueryWrapper<Floor>().eq(Floor::getNewsId, newsId).orderByDesc(Floor::getCreateTime));

        LinkedList<Remark> remarks = new LinkedList<>();
        for (Floor floor : floorList) {
            Remark remark = new Remark();
            remark.setFloor(floor);
            long floorId = floor.getId();
            List<Room> rooms = roomService.list(new LambdaQueryWrapper<Room>().eq(Room::getFloorId, floorId).orderByAsc(Room::getCreateTime));
            remark.setRooms(rooms);
            remarks.add(remark);
        }
        newsDto.setRemark(remarks);
        return Result.success(newsDto);
    }

    /**
     * 添加评论
     * @param id 评论者id
     * @param newsId 评论所在帖子id
     * @param type 评论对象类型
     * @param room 评论的详细信息
     * @return
     */
    @PutMapping("/reply/{id}/{newsId}/{type}")
    public Result<String > putReply(@PathVariable("id") Long id,@PathVariable("newsId") Long newsId,
                                    @PathVariable("type") String type,@RequestBody Room room){

        log.info("type:{},id:{},newId:{},room:{}",type,id,newsId,room);

        List<String> info = selectInfo(id);
        if (info == null){
            return Result.error("当前用户不存在！！！");
        }
        String avatar = info.get(0);
        String name = info.get(1);
        switch (type){
            case "news":
                Floor floor = new Floor();
                floor.setCreateAvatar(avatar);
                floor.setCreateName(name);
                floor.setCreateId(id);
                floor.setContent(room.getContent());
                floor.setNewsId(newsId);

                floorService.save(floor);
                break;
            case "floor":
                Room replyFloor = new Room();
                replyFloor.setContent(room.getContent());
                replyFloor.setFloorId(room.getFloorId());
                replyFloor.setCreateAvatar(avatar);
                replyFloor.setCreateName(name);
                replyFloor.setCreateId(id);

                roomService.save(replyFloor);
                break;
            case "room":
                List<String> replyInfo = selectInfo(room.getReplyId());
                Room replyRoom = new Room();
                replyRoom.setReplyId(room.getReplyId());
                replyRoom.setReplyName(replyInfo.get(1));
                replyRoom.setCreateName(name);
                replyRoom.setCreateAvatar(avatar);
                replyRoom.setCreateId(id);
                replyRoom.setContent(room.getContent());
                replyRoom.setFloorId(room.getFloorId());

                roomService.save(replyRoom);
                break;
            default:
                Result.error("回复类型出错");
        }

        return Result.success(null);
    }

    @PutMapping("/add")
    public Result<String> addNews(@RequestBody News news){

        log.info("news:{}",news);
        if (news != null){
            boolean save = newsService.save(news);
            if (!save)
                return Result.error("发表失败");
        }else {
            return Result.error("发表失败");
        }

        return Result.success(null);
    }

    @DeleteMapping("/delete")
    public Result<String> deleteNews(@RequestBody Map<String , List<String>> ids){
        log.info("ids: {}",ids.get("ids"));
        List<String> list = ids.get("ids");

        newsService.removeBatchByIds(list);
        return Result.success(null);
    }

    @PatchMapping("/start/{id}")
    public Result<String> starting(@PathVariable("id") Long id, @RequestBody NewsDto newsDto){

        StartLog startLog = new StartLog();
        startLog.setStartId(id);
        startLog.setNewsId(newsDto.getId());
        if (newsDto.getIsStart()){
            startLogService.save(startLog);
        }else {
            startLogService.remove(new LambdaQueryWrapper<StartLog>()
                                        .eq(StartLog::getNewsId,newsDto.getId()).eq(StartLog::getStartId,id));
        }

        newsService.updateById(newsDto);
        return Result.success(null);
    }

    public List<String> selectInfo(long id){
        Object create = newsService.getCreate(id);
        if (create == null){
            return null;
        }
        String avatar = null;
        String name = null;
        if (create instanceof User){
            avatar = ((User) create).getAvatar();
            name = ((User) create).getName();
        }
        if (create instanceof Employee){
            avatar = ((Employee) create).getAvatar();
            name = ((Employee) create).getName();
        }
        ArrayList<String> info = new ArrayList<>();
        info.add(avatar);
        info.add(name);
        return info;
    }
}

