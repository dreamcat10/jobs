package com.yyoung.jobs.contoller;

import com.yyoung.jobs.dto.EmployeeDto;
import com.yyoung.jobs.entity.Employee;
import com.yyoung.jobs.service.EmployeeService;
import com.yyoung.jobs.utils.Result;
import com.yyoung.jobs.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.TimeUnit;

//招聘者操作
@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeController {

    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    EmployeeService employeeService;


    //注册账户
    @RequestMapping("/init")
    public Result<String> init(@RequestBody EmployeeDto employeeDto){

        Integer code = employeeDto.getCode();
        ValueOperations ops = redisTemplate.opsForValue();
        Object sms = ops.get("" + employeeDto.getPhone());
        log.info("验证码:{},{}", sms,code);
        if (sms == null ||!sms.equals(code.toString())){
            return Result.error("验证码错误或已过期!");
        }
        Employee emp = employeeService.getOne(employeeDto.getPhone());
        if (emp != null){
            return Result.error("该手机号已注册过用户");
        }
        Employee employee = new Employee();

        employee.setCompanyId(employeeDto.getCompanyId());
        employee.setName(employeeDto.getName());
        employee.setPhone(employeeDto.getPhone());
        employee.setPassword(employeeDto.getPassword());

        employeeService.addEmp(employee);

        return Result.success(null);
    }

    //用户登录
    @PostMapping("/logging")
    public Result<Employee> logging(@RequestBody EmployeeDto employeeDto, HttpServletRequest request){
        Employee emp = employeeService.getOne(employeeDto.getPhone());
        if (emp == null)
            return Result.error("用户不存在!");
        Integer code = employeeDto.getCode();
        if (code !=null){
            ValueOperations ops = redisTemplate.opsForValue();
            Object sms = ops.get("" + employeeDto.getPhone());
            log.info("验证码:{},{}",code,sms);
            if (sms == null || !sms.equals(code.toString())){
                return Result.error("验证码错误或已过期!");
            }else{
                request.getSession().setAttribute("emp", emp.getPhone());
                return Result.success(emp);
            }
        }

        if (!emp.getPassword().equals(employeeDto.getPassword())){
            return Result.error("账号或密码错误!");
        }
        request.getSession().setAttribute("emp", emp.getPhone());
        return Result.success(emp);

    }

    //发送验证码
    @RequestMapping("/sendMsg")
    public Result<String> sendMsg(long phone){
        log.info("phone:{}",phone);
        String phoneNumbers = String.valueOf(phone);
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
//            Boolean isSuccess = Sample.sendSms(phoneNumbers, code);
            Boolean isSuccess = true;
            log.info("成功？:{}",isSuccess);
            if (isSuccess){
                ValueOperations ops = redisTemplate.opsForValue();
                ops.set(phoneNumbers, ""+code, 50, TimeUnit.MINUTES);
                return Result.success(null);
            }
        } catch (Exception e) {
            log.debug("发送验证码出错,错误信息: {} ", e.getMessage());
        }
        return Result.error("发送失败");
    }

    @RequestMapping("/test")
    public void test(){

    }
}
