package com.baizhi.service;

import com.baizhi.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {
    //查询
    Map<String, Object> selectAll(Integer page, Integer rows, String starId);

    //添加
    String add(User user);

    //修改
    public void edit(User user);

    //删除
    void del(String id, HttpServletRequest request);

    //查所有导出文件
    List<User> export();

    //折线图
    Integer[] FindMouthBySex(String sex);
}
