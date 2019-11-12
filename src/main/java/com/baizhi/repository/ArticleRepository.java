package com.baizhi.repository;

import com.baizhi.entity.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//定义一个抽象类
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {
}
