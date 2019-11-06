package com.baizhi.service;

import com.baizhi.entity.Album;
import com.baizhi.entity.Chapter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ChapterService {
    //查询
    Map<String,Object> selectAll(Integer page, Integer rows,String albumId);
    //添加
    String add(Chapter chapter);
    //修改
    void edit(Chapter chapter);
    //删除
    void del(String id, HttpServletRequest request);
    //查询专辑中的数量
    Album selectOne(String id);
}
