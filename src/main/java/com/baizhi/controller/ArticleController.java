package com.baizhi.controller;

import com.baizhi.entity.Article;
import com.baizhi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    //查询所有
    @RequestMapping("selectAll")
    Map<String, Object> selectAll(Integer page, Integer rows) {
        Map<String, Object> map = articleService.selectAll(page, rows);
        return map;
    }

    //增删改
    @RequestMapping("add")
    Map<String, Object> add(Article article, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();

        String id = articleService.add(article);
        map.put("message", id);
        map.put("status", true);

        return map;
    }

    //增删改
    @RequestMapping("edit")
    Map<String, Object> edit(Article article, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        articleService.edit(article);
        map.put("status", true);
        return map;
    }

    //删除
    @RequestMapping("del")
    Map<String, Object> del(Article article, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        articleService.del(article.getId(), request);
        map.put("status", true);
        return map;
    }


    //调用业务层实现es功能
    @RequestMapping("search")
    public List<Article> search(String content) {
        System.out.println("1111111111111111111111");
        return articleService.search(content);
    }
}
