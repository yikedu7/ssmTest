package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserDao {

    void save(User user);

    void deleteById(Long id);

    void update(User user);

    User findById(Long id);

    User findByNameAndPwd(@Param("name") String name, @Param("pwd") String pwd);

    List<User> findByIds(Collection<Long> ids);

    List<User> findByPage(@Param("page") int page, @Param("size") int size);
}
