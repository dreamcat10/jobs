package com.yyoung.jobs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.mapper.PostMapper;
import com.yyoung.jobs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    PostMapper postMapper;

    public void insertPost(Post post){
        postMapper.insert(post);
    }
}
