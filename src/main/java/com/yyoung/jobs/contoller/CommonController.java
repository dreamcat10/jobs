package com.yyoung.jobs.contoller;

import com.aliyun.credentials.http.HttpResponse;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.yyoung.jobs.common.Aliyun;
import com.yyoung.jobs.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {


    @Value("${file.path}")
    String path;

    //上传简历
    @PostMapping("/upLoad/{type}")
    public Result<String> upLoad(@RequestBody MultipartFile file, @PathVariable String type, HttpServletRequest request){

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());

        String fileName = request.getSession().getAttribute("emp") + suffix;
        System.out.println(fileName);

        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "jobs/" + type + '/' + fileName;
        OSS ossClient = new OSSClientBuilder().build(Aliyun.ENDPOINT, Aliyun.ACCESS_KEY_ID, Aliyun.ACCESS_KEY_SECRET);
        try {

            InputStream inputStream = file.getInputStream();

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getContentType(suffix));
            objectMetadata.setObjectAcl(CannedAccessControlList.PublicRead);
            objectMetadata.setContentDisposition("inline");

            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(Aliyun.BUCKETNAME, objectName, inputStream);
            putObjectRequest.setMetadata(objectMetadata);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            if (result.getResponse().getStatusCode() != 200){
                return Result.error("上传失败!");
            }
            log.info("uri:{}",result.getResponse().getUri());
            return Result.success(result.getResponse().getUri());

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        //普通上传
//        String filePath = path + type + "\\";
//        File dir = new File(filePath);
//        if (!dir.exists()){
//            dir.mkdirs();
//        }
//        try {
//            file.transferTo(new File(filePath  +  fileName));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Result.error("上传失败!");
//        }
//        return Result.success(fileName);

         return Result.error("上传失败!");
    }

    //下载文件
    @GetMapping("/down/{type}/{fileName}")
    public void downLoad(@PathVariable("type") String type,
                         @PathVariable("fileName") String fileName, HttpServletResponse response){

        String objectName = "jobs/" + type + '/' + fileName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(Aliyun.ENDPOINT, Aliyun.ACCESS_KEY_ID, Aliyun.ACCESS_KEY_SECRET);
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(Aliyun.BUCKETNAME, objectName);

            // 读取文件内容。
            InputStream objectContent = ossObject.getObjectContent();
            ServletOutputStream os = response.getOutputStream();
            if (type.equals("resume"))
                response.setContentType("application/pdf");
            else
                response.setContentType("image/png");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = objectContent.read(bytes)) != -1){
                os.write(bytes, 0, len);
                os.flush();
            }

            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            objectContent.close();
            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            ossObject.close();

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
     //普通下载
//        String filePath = path + type + "\\" ;
//
//        FileInputStream ins = null;
//        ServletOutputStream os = null;
//        try {
//             ins = new FileInputStream(filePath + fileName);
//             os = response.getOutputStream();
//             if (type.equals("resume"))
//                 response.setContentType("application/pdf");
//             else
//                 response.setContentType("image/jpeg");
//             int len = 0;
//             byte[] bytes = new byte[1024];
//             while ((len = ins.read(bytes)) != -1){
//                 os.write(bytes, 0, len);
//                 os.flush();
//             }
//         } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                ins.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    private static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }

        return "image/jpeg";
    }

}
