package com.yyoung.jobs.contoller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyoung.jobs.dto.PostDto;
import com.yyoung.jobs.entity.Company;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.entity.Post;
import com.yyoung.jobs.service.CompanyService;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.service.PostService;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//岗位操作
@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CompanyService companyService;

    @Autowired
    EmployeeService employeeService;

    //添加岗位
    @PutMapping("/add")
    public Result<String> addPost(@RequestBody Post post){
        log.info("post:{}",post);
        Long companyId = post.getCompanyId();
        Company company = companyService.getById(companyId);
        String address = company.getAddress();

        post.setAddress(address);
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
    @DeleteMapping("/del")
    public Result<String> deletePost(@RequestBody Map<String, List<String>> ids){

        List<String> list = ids.get("ids");
        postService.removeBatchByIds(list);

        return Result.success(null);
    }

    @GetMapping("/list/{id}")
    public Result<List<Post>> list(@PathVariable Long id){

        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("emp_id", id);
        List<Post> posts = postService.list(queryWrapper);

        return Result.success(posts);
    }

    @GetMapping("/detail/{postId}")
    public Result<PostDto> detail(@PathVariable Long postId){

        Post post = postService.getById(postId);
        PostDto postDto = new PostDto();

        if (post == null){
            return Result.error("招聘信息已过期！！！");
        }
        BeanUtils.copyProperties(post, postDto);
        Long companyId = postDto.getCompanyId();
        Company company = companyService.getById(companyId);

        Long empId = postDto.getEmpId();
        Employee emp = employeeService.getById(empId);

        if (emp == null || company == null){
            return Result.error("招聘信息已过期！！！");
        }
        postDto.setCompany(company);
        postDto.setCreateAvatar(emp.getAvatar());
        postDto.setCreateName(emp.getName());

        return Result.success(postDto);
    }
}
