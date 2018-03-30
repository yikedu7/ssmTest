package cn.damonto.ssmTest.service;

import cn.damonto.ssmTest.dao.UserDao;
import cn.damonto.ssmTest.dao.UserRoleDao;
import cn.damonto.ssmTest.entity.User;
import cn.damonto.ssmTest.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

    public void save(User user){
        userDao.save(user);
    }

    public void update(User user){
        userDao.update(user);
    }

    public void remove(Long id){
        userDao.deleteById(id);
    }

    public User getUserByNameAndPwd(String name,String pwd){
        return userDao.findByNameAndPwd(name, pwd);
    }

    public List<User> getUsersByPage(int page, int size){
        return userDao.findByPage(page, size);
    }

    public List<User> getUsersByIds(Collection<Long> ids){
        return userDao.findByIds(ids);
    }

    public List<UserRole> getUserRolesByUserId(Long userId){
        return userRoleDao.findByUserId(userId);
    }

    public List<UserRole> getUserRolesByPage(int page,int size){
        return userRoleDao.findByPage(page, size);
    }

    public void addUserRoles(Long userId,Long[] roleIds){
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.asList(roleIds).forEach((roleId) -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        userRoleDao.saveAll(userRoles);
    }
}
