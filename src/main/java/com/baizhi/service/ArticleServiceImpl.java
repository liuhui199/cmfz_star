package com.baizhi.service;

import com.baizhi.dao.ArticleDao;
import com.baizhi.entity.Article;
import com.baizhi.repository.ArticleRepository;
import org.apache.commons.collections4.IterableUtils;
import org.apache.ibatis.session.RowBounds;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;

    //查询所有
    @Override
    public Map<String, Object> selectAll(Integer page, Integer rows) {
        Article article = new Article();
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Article> list = articleDao.selectByRowBounds(article, rowBounds);
        int count = articleDao.selectCount(article);

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("rows", list);
        map.put("total", count % rows == 0 ? count / rows : count / rows + 1);
        map.put("records", count);
        return map;
    }

    //添加
    @Override
    public String add(Article article) {
        article.setId(UUID.randomUUID().toString());
        article.setCreateDate(new Date());
        int i = articleDao.insertSelective(article);
        if (i == 0) {
            throw new RuntimeException("添加失败！");
        }
        return article.getId();
    }

    //修改
    @Override
    public void edit(Article article) {
        int i = articleDao.updateByPrimaryKeySelective(article);
        if (i == 0) {
            throw new RuntimeException("修改文章失败");
        }
    }

    //删除
    @Override
    public void del(String id, HttpServletRequest request) {
        int i = articleDao.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new RuntimeException("删除文章失败");
        }
    }

    //---------------------- es功能 ------------------------
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public List<Article> search(String content) {
        if ("".equals(content)) {
            Iterable<Article> all = articleRepository.findAll();
            List<Article> list = IterableUtils.toList(all);
            return list;
        } else {
            //先准备一个高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("*")
                    .preTags("<span color='red'>")
                    .postTags("</span>")
                    .requireFieldMatch(false);

            NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                    .withQuery(QueryBuilders.queryStringQuery(content)
                            .field("name")
                            .field("author")
                            .field("brief")
                            .field("content"))
                    .withSort(SortBuilders.scoreSort())
                    .withHighlightBuilder(highlightBuilder) //高亮
                    .build();

            AggregatedPage<Article> articles = elasticsearchTemplate.queryForPage(nativeSearchQuery, Article.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                    SearchHits hits = response.getHits();
                    List<Article> list = new ArrayList<>();
                    for (SearchHit hit : hits) {
                        Map<String, Object> map = hit.getSourceAsMap();
                        Article article = new Article();
                        article.setId(map.get("id").toString());
                        article.setName(map.get("name").toString());
                        article.setAuthor(map.get("author").toString());
                        article.setContent(map.get("content").toString());
                        String date = map.get("createDate").toString();
                        article.setCreateDate(new Date(Long.valueOf(date)));

                        Map<String, HighlightField> fieldMap = hit.getHighlightFields();
                        if (fieldMap.get("name") != null) {
                            System.out.println("fieldMap.get('name'):" + fieldMap.get("name"));
                            System.out.println(fieldMap.get("name"));
                            article.setName(fieldMap.get("name").getFragments()[0].toString());
                        }
                        if (fieldMap.get("author") != null) {
                            article.setAuthor(fieldMap.get("author").getFragments()[0].toString());
                        }
                        if (fieldMap.get("brief") != null) {
                            article.setBrief(fieldMap.get("brief").getFragments()[0].toString());
                        }
                        if (fieldMap.get("content") != null) {
                            article.setContent(fieldMap.get("content").getFragments()[0].toString());
                        }
                        list.add(article);
                    }
                    return new AggregatedPageImpl<T>((List<T>) list);
                }

                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                    return null;
                }
            });
            System.out.println("2222222222222222222222222");
            System.out.println(articles.getContent() + "33333333333");
            return articles.getContent();
        }
    }

}
