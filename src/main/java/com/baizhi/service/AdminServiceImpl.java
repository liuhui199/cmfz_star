package com.baizhi.service;

import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Service
@Transactional
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminDao adminDao;

    @Override
    public void login(Admin admin, String enCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String securityCode = (String) session.getAttribute("securityCode");
        if(securityCode.equals(enCode)){
            Admin one = adminDao.selectOne(admin);
            if(one != null){
                session.setAttribute("login",one);
            }else {
                throw new RuntimeException("用户名或密码错误");
            }
        }else {
            throw new RuntimeException("验证码错误");
        }
    }
}
