package com.baizhi.controller;

import com.baizhi.entity.Album;
import com.baizhi.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    //查询
    @RequestMapping("selectAll")
    Map<String,Object> selectAll(Integer page, Integer rows){
        Map<String, Object> map = albumService.selectAll(page, rows);
        return map;
    }
    //添加
    @RequestMapping("edit")
    Map<String,Object> edit(String oper, Album album, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            if("add".equals(oper)){
                String id = albumService.add(album);
                map.put("message",id);
            }
            if("edit".equals(oper)){
                albumService.edit(album);
            }
            if("del".equals(oper)){
                albumService.del(album.getId(),request);
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
    Map<String,Object> upload(MultipartFile cover, String id, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            cover.transferTo(new File(request.getServletContext().getRealPath("/images"),cover.getOriginalFilename()));
            //修改star对象photo属性
            Album album = new Album();
            album.setId(id);
            album.setCover(cover.getOriginalFilename());
            albumService.edit(album);
            map.put("status",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",false);
        }
        return map;
    }
}
