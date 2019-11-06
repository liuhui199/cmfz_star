package com.baizhi.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    //查询所有
    @RequestMapping("selectAll")
    Map<String,Object> selectAll(Integer page,Integer rows,String starId){
        Map<String, Object> map = userService.selectAll(page, rows,starId);
        return map;
    }

    //增删改操作
    @RequestMapping("edit")
    public Map<String,Object> edit(String oper, User user, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        try{
            if("add".equals(oper)){
                String id = userService.add(user);
                map.put("message",id);
            }
            if("edit".equals(oper)){
                userService.edit(user);
            }
            if("del".equals(oper)){
                userService.del(user.getId(),request);
            }
            map.put("status",true);
        }catch (Exception e){
            e.printStackTrace();
            map.put("status",false);
            map.put("message",e.getMessage());
        }
        return map;
    }
    //文件上传
    @RequestMapping("upload")
    public Map<String,Object> upload(MultipartFile photo, String id, HttpServletRequest request){
        System.out.println("进入上传");
        Map<String, Object> map = new HashMap<>();
        try {
            //文件上传
            photo.transferTo(new File(request.getServletContext().getRealPath("/images"),photo.getOriginalFilename()));
            //修改banner对象中cover属性
            User user = new User();
            user.setId(id);
            user.setPhoto(photo.getOriginalFilename());
            userService.edit(user);
            map.put("status",true);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("status",false);
        }
        return map;
    }


    //导出数据poi
    @RequestMapping("export")
    public void export(HttpServletResponse response){
        //准备数据
        List<User> list = userService.export();

        Workbook w = ExcelExportUtil.exportExcel(new ExportParams("用户所有数据", "用户"),com.baizhi.entity.User.class, list);

        String fileName =  "用户报表（"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ").xls";
        //处理中文下载名乱码
        try {
            fileName = new String(fileName.getBytes("gbk"),"iso-8859-1");
            //设置 response
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition","attachment;filename="+fileName);
            w.write(response.getOutputStream());
            w.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //折线图
    @RequestMapping("line")
    public Map<String,Object> line(){
        Map<String, Object> map = new HashMap<>();
        //方法调用，男女设定，传入前台
        Integer[] integers = userService.FindMouthBySex("男");
        map.put("man",integers);
        Integer[] women = userService.FindMouthBySex("女");
        map.put("woman",women);
        return map;
    }
}
