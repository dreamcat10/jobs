package com.yyoung.jobs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yyoung.jobs.entity.Post;

public interface PostService extends IService<Post> {
    void insertPost(Post post);
}
