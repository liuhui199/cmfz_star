package com.baizhi.service;

import com.baizhi.entity.Album;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AlbumService {
    //查询
    Map<String, Object> selectAll(Integer page, Integer rows);

    //添加
    String add(Album album);

    //修改
    public void edit(Album album);

    //删除
    void del(String id, HttpServletRequest request);
}
