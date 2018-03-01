package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookDao {

    Book queryById(long bookId);

    List<Book> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    int reduceNumber(long bookId);
}
