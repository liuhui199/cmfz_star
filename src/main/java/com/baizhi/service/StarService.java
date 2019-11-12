package com.baizhi.service;

import com.baizhi.entity.Star;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StarService {
    //查询
    Map<String, Object> selectAll(Integer page, Integer rows);

    //添加
    String add(Star star);

    //修改
    public void edit(Star star);

    //删除
    void del(String id, HttpServletRequest request);

    //查询明星
    List<Star> getAllStarForSelect();
}
