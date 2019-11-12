package com.baizhi.service;

import com.baizhi.entity.Article;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ArticleService {
    //查询所有
    Map<String, Object> selectAll(Integer page, Integer rows);

    //添加
    String add(Article article);

    //修改
    void edit(Article article);

    //删除
    void del(String id, HttpServletRequest request);

    //es查询
    List<Article> search(String content);
}
