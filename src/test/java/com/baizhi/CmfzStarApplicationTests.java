package com.baizhi;

import com.baizhi.dao.AdminDao;
import com.baizhi.entity.Admin;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SpringBootTest
class CmfzStarApplicationTests {
    @Autowired
    private AdminDao adminDao;

    @Test
    void contextLoads() {
        //查所有
        /*List<Admin> list = adminDao.selectAll();
        list.forEach(l-> System.out.println(l));*/
        //条件查询
        /*Admin admin = new Admin();
        admin.setUsername("liuh");
        admin.setNickname("浮尘");
        List<Admin> list1 = adminDao.select(admin);
        list1.forEach(l2-> System.out.println(l2));*/
        //键查询
       /* Admin key = adminDao.selectByPrimaryKey("2");
        System.out.println(key);*/
        //分页
        /*Admin admin = new Admin();
        RowBounds rowBounds = new RowBounds(1, 3);
        List<Admin> admins = adminDao.selectByRowBounds(admin, rowBounds);
        admins.forEach(admin1 -> System.out.println(admin1));*/
        //
        /*Example example = new Example(Admin.class);
        example.createCriteria().andBetween("age",18,20)
                .andEqualTo("sex","女");
        List<Admin> list = adminDao.selectByExample(example);
        list.forEach(l-> System.out.println(l));*/

        //修改
        Admin admin = new Admin();
        admin.setId("2");
        admin.setUsername("小白");
        adminDao.updateByPrimaryKeySelective(admin);
    }

}
