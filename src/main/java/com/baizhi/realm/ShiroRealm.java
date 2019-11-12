package com.baizhi.realm;

import com.baizhi.dao.AdminDao;
import com.baizhi.dao.AdminRoleDao;
import com.baizhi.dao.RoleDao;
import com.baizhi.entity.Admin;
import com.baizhi.entity.AdminRole;
import com.baizhi.entity.Role;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private AdminRoleDao adminRoleDao;
    @Autowired
    private RoleDao roleDao;

    //-------------------- 授权 ------------------------
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //根据身份集合获取用户的主分身
        String username = (String) principalCollection.getPrimaryPrincipal();
        //查询admin对象
        Admin admin = adminDao.selectOne(new Admin(null, username, null, null, null, null, null));
        //根据admin的id获取关系表中的adminRole对象的集合
        List<AdminRole> list = adminRoleDao.select(new AdminRole(null, admin.getId(), null));

        ArrayList<String> list1 = new ArrayList<>();
        //遍历adminRole集合,取其中的adminRole对象里边的roleId属性
        for (AdminRole adminRole : list) {
            //根据roleId属性,获取role对象
            Role role = roleDao.selectOne(new Role(adminRole.getRoleId(), null));
            list1.add(role.getName());
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(list1);
        System.out.println("---------- 授权 ---------");
        return info;
    }

    //------------------- 认证 -------------------------
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Admin admin = new Admin();
        admin.setUsername(token.getUsername());
        Admin one = adminDao.selectOne(admin);
        if (one == null) {
            return null;
        } else {
            SimpleAccount account = new SimpleAccount(one.getUsername(), one.getPassword(), ByteSource.Util.bytes(one.getSalt()), "com.baizhi.realm.ShiroRealm");
            return account;
        }
    }
}
