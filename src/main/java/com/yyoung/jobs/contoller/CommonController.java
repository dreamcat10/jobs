package com.yyoung.jobs.contoller;

import com.aliyun.credentials.http.HttpResponse;
import com.yyoung.jobs.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${file.path}")
    String path;

    //上传简历
    @PostMapping("/upLoad/{type}")
    public Result<String> upLoad(@RequestBody MultipartFile file,@PathVariable String type, HttpSession session){

        session.setAttribute("emp","17723402287");
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        String fileName = session.getAttribute("emp") + suffix;
        System.out.println(fileName);

        String filePath = path + type + "\\";
        File dir = new File(filePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(filePath  +  fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败!");
        }

        return Result.success(fileName);
    }

    //下载文件
    @GetMapping("/down/{type}/{fileName}")
    public void downLoad(@PathVariable("type") String type,
                         @PathVariable("fileName") String fileName, HttpServletResponse response){

        String filePath = path + type + "\\" ;

        FileInputStream ins = null;
        ServletOutputStream os = null;
        try {
             ins = new FileInputStream(filePath + fileName);
             os = response.getOutputStream();
             if (type.equals("resume"))
                 response.setContentType("application/pdf");
             else
                 response.setContentType("image/jpeg");
             int len = 0;
             byte[] bytes = new byte[1024];
             while ((len = ins.read(bytes)) != -1){
                 os.write(bytes, 0, len);
                 os.flush();
             }
         } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                ins.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
