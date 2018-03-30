package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface RoleDao {

    void save(Role role);

    void deleteById(Long id);

    void update(Role role);

    Role findById(Long id);

    List<Role> findByIds(Collection<Long> ids);

    List<Role> findByPage(@Param("page") int page, @Param("size") int size);



}
