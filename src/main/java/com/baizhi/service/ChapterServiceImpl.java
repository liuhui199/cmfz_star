package com.baizhi.service;

import com.baizhi.dao.AlbumDao;
import com.baizhi.dao.ChapterDao;
import com.baizhi.entity.Album;
import com.baizhi.entity.Chapter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterDao chapterDao;
    @Autowired
    private AlbumDao albumDao;

    @Override
    public Map<String, Object> selectAll(Integer page, Integer rows,String albumId) {
        Chapter chapter = new Chapter();
        chapter.setAlbumId(albumId);
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        List<Chapter> list = chapterDao.selectByRowBounds(chapter, rowBounds);
        int count = chapterDao.selectCount(chapter);

        Map<String, Object> map = new HashMap<>();
        map.put("page",page);
        map.put("rows",list);
        map.put("total",count%rows==0?count/rows:count/rows+1);
        map.put("records",count);
        return map;
    }
    //添加
    @Override
    public String add(Chapter chapter) {
        chapter.setId(UUID.randomUUID().toString());
        chapter.setCreateDate(new Date());
        int i = chapterDao.insertSelective(chapter);
        if(i == 0){
            throw new RuntimeException("添加章节失败");
        }
        return chapter.getId();
    }
    //修改
    @Override
    public void edit(Chapter chapter) {
        /*int i = chapterDao.updateByPrimaryKeySelective(chapter); //这种方法要丢失文件
        if(i == 0){
            throw new RuntimeException("修改章节失败");
        }*/
        if("".equals(chapter.getName())){
            chapter.setName(null);
        }
        try{
            chapterDao.updateByPrimaryKeySelective(chapter);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("修改失败");
        }
    }
    //删除
    @Override
    public void del(String id, HttpServletRequest request) {
        Chapter chapter = chapterDao.selectByPrimaryKey(id);
        System.out.println(id+"#####################");
        int i = chapterDao.deleteByPrimaryKey(id);
        if(i == 0){
            throw new RuntimeException("删除失败");
        }else {
            String name = chapter.getName();
            File file = new File(request.getServletContext().getRealPath("/music/"),name);
            boolean b = file.delete();
            if(b == false){
                throw new RuntimeException("删除章节文件失败");
            }
        }
    }

    //查询专辑中章节数量
    @Override
    public Album selectOne(String id) {
        Album album = albumDao.selectByPrimaryKey(id);
        return album;
    }
}
