package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.RoleFunction;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface RoleFunctionDao {

    void save(RoleFunction roleFunction);

    void saveAll(Collection<RoleFunction> roleFunctions);

    void deleteByRoleId(Long roleId);

    void update(RoleFunction roleFunction);

    List<RoleFunction> findByRoleId(Long roleId);

    RoleFunction findById(Long id);

    List<RoleFunction> findByPage(@Param("page") int page, @Param("size") int size);

}
