package com.regi.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regi.common.R;
import com.regi.entity.User;
import com.regi.service.UserService;
import com.regi.utils.SMSUtils;
import com.regi.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
//    private RedisTemplate redisTemplate;
    private StringRedisTemplate redisTemplate;
    @PostMapping("/sendMsg")
    public R<String> sendMag(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if(phone!=null){


            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            SMSUtils.sendMessage("瑞吉外卖","SMS_461890879",phone,code);
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
//            session.setAttribute(phone,code);
            System.out.println(code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        Object codeInSession = session.getAttribute(phone);
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if(codeInSession!=null&&codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
