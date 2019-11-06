package com.baizhi.service;

import com.baizhi.entity.Banner;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BannerService {
    //查询
    Map<String,Object> selectAll(Integer page,Integer rows );
    //添加
    String add(Banner banner);
    //修改
    void edit(Banner banner);
    //删除
    void del(String id, HttpServletRequest request);
}
