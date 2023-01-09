package com.yyoung.jobs.common;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.*;

import com.aliyun.tea.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Sample {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static Boolean sendSms(String phoneNumbers, Integer code) throws Exception {

        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.Client client = Sample.createClient(Aliyun.ACCESS_KEY_ID, Aliyun.ACCESS_KEY_SECRET);
        com.aliyun.dysmsapi20170525.models.SendSmsRequest request = new com.aliyun.dysmsapi20170525.models.SendSmsRequest();
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();

        request.phoneNumbers = phoneNumbers;
        request.signName = Aliyun.SIGN_NAME;
        request.templateCode = Aliyun.TEMPLATE_CODE;
        request.templateParam = "{\"code\":\"" + code + "\"}";


        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse response = client.sendSmsWithOptions(request, runtime);
            String isSuccess = response.body.code;
            System.out.println(isSuccess);
            if (isSuccess.equals("OK")) {
                return true;
            } else
                return false;

        } catch (TeaException error) {
            // 如有需要，请打印 error
            log.debug(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            log.debug(error.message);
        }
        return false;
    }


}
