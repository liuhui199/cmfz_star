package com.baizhi.controller;

import com.baizhi.entity.Star;
import com.baizhi.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("star")
public class StarController {
    @Autowired
    private StarService starService;

    //查询
    @RequestMapping("selectAll")
    @ResponseBody
    Map<String, Object> selectAll(Integer page, Integer rows) {
        Map<String, Object> map = starService.selectAll(page, rows);
        return map;
    }

    //添加
    @RequestMapping("edit")
    @ResponseBody
    Map<String, Object> edit(String oper, Star star, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            if ("add".equals(oper)) {
                String id = starService.add(star);
                map.put("message", id);
            }
            if ("edit".equals(oper)) {
                starService.edit(star);
            }
            if ("del".equals(oper)) {
                starService.del(star.getId(), request);
            }
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", false);
            map.put("message", e.getMessage());
        }
        return map;
    }

    //文件上传
    @RequestMapping("upload")
    @ResponseBody
    Map<String, Object> upload(MultipartFile photo, String id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            photo.transferTo(new File(request.getServletContext().getRealPath("/images"), photo.getOriginalFilename()));
            //修改star对象photo属性
            Star star = new Star();
            star.setId(id);
            star.setPhoto(photo.getOriginalFilename());
            starService.edit(star);
            map.put("status", true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", false);
        }
        return map;
    }

    //下拉列表
    @RequestMapping("getAllStar")
    public void getAllStar(HttpServletResponse response) throws IOException {
        List<Star> list = starService.getAllStarForSelect();
        String string = "<select>";
        for (Star star : list) {
            string += "<option value=" + star.getId() + ">" + star.getRealname() + "</option>";
        }
        string += "</select>";
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(string);
        /*
        <select>
            <option value='1'>张三</opiton>
            <option value='2'>李四</opiton>
            <option value='1'>张三</opiton>
        </select>
         */
    }

}
