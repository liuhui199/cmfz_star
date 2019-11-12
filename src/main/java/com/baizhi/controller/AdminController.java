package com.baizhi.controller;

import com.baizhi.entity.Admin;
import com.baizhi.service.AdminService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /*@RequestMapping("login")
    public Map<String, Object> login(Admin admin, String enCode, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(admin);
        try {
            adminService.login(admin, enCode, request);
            map.put("status", true);
        } catch (Exception e) {
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }*/
    @RequestMapping("login")
    public Map<String, Object> login(Admin admin, String enCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String securityCode = (String) session.getAttribute("securityCode");
        Map<String, Object> map = new HashMap<>();
        try {
            if (securityCode.equals(enCode)) { //验证码验证  然后shiro验证
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(), admin.getPassword());
                subject.login(token);
                map.put("status", true);
            } else {
                map.put("message", "验证码错误！");
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("message", "用户名或密码错误！");
        }
        return map;
    }

    @RequestMapping("logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/main.jsp";
    }

}
