package com.baizhi.service;

import com.baizhi.dao.UserDao;
import com.baizhi.entity.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Map<String, Object> selectAll(Integer page, Integer rows, String starId) {
        User user = new User();
        user.setStarId(starId);
        System.out.println(user + "=================");
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<User> list = userDao.selectByRowBounds(user, rowBounds);
        int count = userDao.selectCount(user);

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("rows", list);
        map.put("total", count % rows == 0 ? count / rows : count / rows + 1);
        map.put("records", count);
        return map;
    }

    //添加
    @Override
    public String add(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreateDate(new Date());
        System.out.println(user);
        int i = userDao.insertSelective(user);
        if (i == 0) {
            throw new RuntimeException("添加失败");
        }
        return user.getId();
    }

    //修改
    @Override
    public void edit(User user) {
        if ("".equals(user.getPhoto())) {
            user.setPhoto(null);
        }
        try {
            userDao.updateByPrimaryKeySelective(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改失败");
        }
    }

    //删除
    @Override
    public void del(String id, HttpServletRequest request) {
        User user = userDao.selectByPrimaryKey(id);
        int i = userDao.deleteByPrimaryKey(id);
        if (i == 0) {
            throw new RuntimeException("删除失败");
        } else {
            String photo = user.getPhoto();
            File file = new File(request.getServletContext().getRealPath("/images/"), photo);
            boolean b = file.delete();
            if (b == false) {
                throw new RuntimeException("删除文件失败");
            }
        }
    }

    //查询所有用于数据导出
    @Override
    public List<User> export() {
        return userDao.selectAll();
    }

    //折线图
    @Override
    public Integer[] FindMouthBySex(String sex) {
        //使用map集合去以键对值的形式
        Map<String, Integer> map = new HashMap<>();
        //设置一个数组的长度
        Integer[] count = new Integer[6];
        //Example，例子里的属性
        Example example = new Example(User.class);
        //此方法构建一个实例
        Example.Criteria criteria = example.createCriteria();
        //andEqualTo 对比数据
        criteria.andEqualTo("sex", sex);
        List<User> users = userDao.selectByExample(example);
        users.forEach(u -> {
            int month = u.getCreateDate().getMonth();
            if (map.get(month + "月") == null) {
                //这里不等于空
                map.put(month + "月", 1);

            } else {
                int i = map.get(month + "月");
                map.put(month + "月", i + 1);
            }
        });
        for (int i = 0; i < count.length; i++) {
            count[i] = map.get(i + "月");
        }

       /* //将数据按顺序存入数组  i是数据
        for (int i =1 ;i<=6;i++){
            //为了下标不得越界，每次循环i-1
            if(map.get((i-1)+"月")==null){
                //月份从1月开始，没有0月
                count[i-1]=0;
            }else {
                Integer integer = map.get((i-1) + "月");
                count[i-1]=integer;
                System.out.println(i-1+"月"+integer+"人注册");
            }
        }*/
        return count;
    }
}
