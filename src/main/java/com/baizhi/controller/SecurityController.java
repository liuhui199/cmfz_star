package com.baizhi.controller;


import com.baizhi.util.SecurityCode;
import com.baizhi.util.SecurityImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RequestMapping("code")
@Controller
public class SecurityController {
    @RequestMapping("getCode")
    public void getCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String securityCode = SecurityCode.getSecurityCode();
        System.out.println(securityCode);
        HttpSession session = request.getSession();
        session.setAttribute("securityCode", securityCode);
        BufferedImage image = SecurityImage.createImage(securityCode);
        response.setContentType("image/png");
        ImageIO.write(image, "png", response.getOutputStream());
    }

    @RequestMapping("quit")
    public String login(HttpServletRequest request) {
        request.getSession().removeAttribute("login");
        return "redirect:/login/login.jsp";
    }
}
