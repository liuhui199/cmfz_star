package com.baizhi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @Excel(name = "编号")
    private String id;
    @Excel(name = "电话")
    private String phone; //电话
    @Excel(name = "用户名")
    private String username; //用户名
    @Excel(name = "用户密码")
    private String password; //用户密码
    private String salt;  //盐
    @Excel(name = "昵称")
    private String nickname; //昵称
    @Excel(name = "省")
    private String province; //省
    @Excel(name = "城市")
    private String city;  //城市
    private String sign; //签名
    private String photo;  //头像
    @Excel(name = "性别")
    private String sex;  //性别
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate; //上传日期
    private String starId;  //明星id
}
