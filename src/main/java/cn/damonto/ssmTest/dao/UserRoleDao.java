package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface UserRoleDao {

    void save(UserRole userRole);

    void saveAll(Collection<UserRole> userRoles);

    void deleteById(Long id);

    void update(UserRole userRole);

    UserRole findById(Long id);

    List<UserRole> findByUserId(Long userId);

    List<UserRole> findByPage(@Param("page") int page, @Param("size") int size);

}
