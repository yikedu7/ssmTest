package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.Function;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface FunctionDao {

    void save(Function function);

    void deleteById(Long id);

    void updateUrl(@Param("id") Long id, @Param("url") String url);

    void update(Function function);

    List<Function> findAll();

    Function findById(Long id);

    Collection<Function> findByIds(Collection<Long> ids);

    List<Function> findByPage(@Param("page") int page, @Param("size") int size, @Param("parentId") Long parentId);

}
