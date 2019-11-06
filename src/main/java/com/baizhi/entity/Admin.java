package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id //通用mapper专用注解,主键
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String age;
    private String sex;
}
