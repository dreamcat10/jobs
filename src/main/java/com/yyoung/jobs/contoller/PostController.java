package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//岗位操作
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    //添加岗位
    @PostMapping("/add")
    public Result<String> addPost(@RequestBody Post post){

        postService.insertPost(post);

        return Result.success(null);
    }

    //修改岗位信息
    @PutMapping("/edit")
    public Result<String> editPost(@RequestBody Post post){

        postService.updateById(post);

        return Result.success(null);
    }

    //删除岗位信息
    @DeleteMapping("/del/{id}")
    public Result<String> deletePost(@PathVariable long id){

        postService.removeById(id);

        return Result.success(null);
    }

    @GetMapping("/list/{id}")
    public Result<List<Post>> list(@PathVariable long id){

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id", id);
        List<Post> posts = postService.list(queryWrapper);

        return Result.success(posts);
    }
}
