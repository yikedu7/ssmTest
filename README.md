> Java Web 开发中，SSH 框架（Struts + Spring + Hibernate）和 SSM 框架（Spring + SpringMVC + MyBatis）是最常见的两个集成框架。它们很好地利用了 Spring IoC 特性，极大程度地减少了代码量和降低了代码之间的耦合度。之前看了一些 Spring Core 和 SpringMVC 的内容，但总觉得对 IoC 和 AOP 等概念没有很深刻的理解。通过这次动手配置 SSM 框架，真的是深刻体会到 Spring IoC 和 Bean 在实际开发中的重要意义。
>
> 项目地址：[https://github.com/DamonDu/ssmTest](https://github.com/DamonDu/ssmTest)

### (一) 项目回顾

在上次 Java Web 项目：[Java Web 开发(一)：简易税务计算器](http://splitmusic.cn/2017/09/29/TaxCalculator/) 中，我尝试用最原生的 Java 语言（或者说 POJO）实现一个简单的 Http Server。由于项目时间的不足和当时对 Java 语言的不熟悉，最终写出了一个功能十分单一且简陋的 Server，而且充斥着各种 Bad Smelling 的代码。但是该项目也不是完全没有收获，在一次又一次的 Debug 中，我遇到了各种新手可能都会遇到的疑惑：

1. 单纯使用 POJO，会导致前端代码和后端代码高度耦合，且几乎没有很好的解耦手段。
2. 难以实现功能全面的应用，主要原因是没有很好的依赖管理和存在大量的冗余代码。
3. 做数据持久化时会遇到各种问题：不友好的数据库管理、拙劣的事务管理......

**SSM 框架几乎完美解决了上面提到的问题**：Spring 通过 IoC 和 Bean 使得 POJO 也能低耦合地实现各种业务和服务功能；MyBatis 极大地降低了数据持久化的难度，通过 Mapper 机制简化了数据访问层（DAO）的开发；SprinMVC 则通过 controller 来处理用户请求和后台操作之间的关系，简化了 Web 层的开发；当然，使用 Maven 来进行依赖管理也让工程文件变得更加简洁明了。

接下来，我会按步骤逐一地记录我配置 SSM 的过程，同时解释一些新手开发者可能有疑问的概念以及强调一些我自己掉入的坑。另外，我的配置很大程度上是对 [Github - liyifeng1994/ssm](https://github.com/liyifeng1994/ssm) 模仿练习，同时他的 README 也讲解地十分仔细，推荐给大家参考学习。

### (二) SSM 框架结构

#### 创建 maven 项目

我是用的 IDE 是 IDEA，在 IDEA 中创建一个 maven 项目的步骤是：New Project — 选择 Maven — 选择合适的 JDK 版本 — 选择模板（此处略过此步）— New — 填写 GroupId 和 ArtifactId — 设置项目名称与路径 — Finish。

在这里，我填写的 GroupId 为 `cn.damonto` ，ArtifactId 为 `ssmTest` ，项目名称默认与 ArtifactId 相同。

> GroupId 和 ArtifactId 被统称为“坐标”，是为了保证项目唯一性而提出的。
>
> GroupId 一般分为两段，第一段为域，第二段为公司名称。域又分为org、com、cn等等许多，其中org为非营利组织，com为商业组织。ArtifactId 一般与项目名称相同。
>
> 举个 apache 公司的 tomcat 项目例子：这个项目的 GroupId 是 org.apache，它的域是org，公司名称是apache，ArtifactId 是 tomcat。

#### 建立目录结构

在进行编码之前，先将一些文件夹提前建立，使得项目最终的目录结构如下：

```
ssmTest
	|pom.xml			# maven配置文件
	|src				# 源代码
	  |test				# 测试文件
	  |main				
	    |java			# java文件
	    |resources		# xml等配置文件
	    	|mapper		# mybatis mapper配置文件
	    	|spring		# spring相关配置文件
	    |webapp			# Web层
	    |sql			# SQL文件
```

在 java 文件夹中创建必要的包，我将包的命名空间统一为 `cn.damonto.ssmTest`：

```
java
  |cn
    |damonto
       |ssmTest
          |dao			# 数据访问层，Data Access Object
          |dto			# 数据传输层，Data Transfer Object
          |entity		# 实体类
          |service		# 业务逻辑
            |impl
          |web			# web controller
```

提前建立好文件夹与包是必要的，这让我们后续的开发更有条理。

### (三) 配置 pom.xml

`pom.xml` 即为 maven 工程实现依赖管理的配置文件。在本项目中，除了必要的 Spring（包括核心库、Dao、test 等）、SpringMVC 和 MyBatis，还需要导入的依赖有：junit（单元测试）、mysql-connector-java（MySQL数据库连接）、c3p0（一个数据库连接池）、mybatis-spring（用于整合 Spring 与 MyBatis）......

各个依赖的具体编码可以在 [Maven Repository: Search/Browse/Explore](https://mvnrepository.com/) 中查询得到。

> 理论上，库应该不存在版本兼容的问题的。但是在这里 mybatis-spring 却出现了版本兼容的问题，使用 1.2.2 版本会出现 `org.mybatis.spring.transaction.SpringManagedTransaction.getTimeout()L` 的错误。将版本改为 1.3.0 即可解决这个问题。

pom.xml 的具体实现见：[pom.xml](https://github.com/DamonDu/ssmTest/blob/master/pom.xml)

### (四) 配置 Spring 与 MyBatis

#### DAO：spring-dao.xml 与 mybatis-config.xml

在 spring 文件夹中创建如下的 spring-dao.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <!--jdbc相关参数-->
    <context:property-placeholder location="classpath:jdbc.properties" />

    <!--配置c3p0连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}" />
        <property name="jdbcUrl" value="${jdbc.url}" />
        <property name="user" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />

        <property name="maxPoolSize" value="30" />
        <property name="minPoolSize" value="10" />
        <property name="autoCommitOnClose" value="false" />
        <property name="checkoutTimeout" value="10000" />
        <property name="acquireRetryAttempts" value="2" />
    </bean>

    <!--配置 mybatis 的 sqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <property name="configLocation" value="classpath:mybatis-config.xml" />
        <property name="typeAliasesPackage" value="cn.damonto.ssmTest.entity"/>
        <property name="mapperLocations" value="classpath:mapper/*.xml" />
    </bean>

    <!--配置扫描DAO包-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
        <property name="basePackage" value="cn.damonto.ssmTest.dao" />
    </bean>
</beans>
```

spring-dao.xml 配置数据访问层，主要包含四个方面的配置：

1. **从 jdbc.properties 中读取数据库相关参数。**在基于spring开发应用的时候，一般都会将数据库的配置放置在properties文件中。例如：

   ```properties
   jdbc.driver=com.mysql.jdbc.Driver
   jdbc.url=jdbc:mysql://localhost:3306/ssm?useUnicode=true&characterEncoding=utf8
   jdbc.username=your_username
   jdbc.password=your_password
   ```

   在 XML 中，使用 `property-placeholder` 来将数据库配置读取并注入，在需要使用配置参数时，通过 `${...}` 来选取参数。

   > 关于 Spring 如何实现配置文件的加载，可以阅读 [Spring 中 property-placeholder 的使用与解析](https://www.cnblogs.com/leftthen/p/5615066.html) 进行了解。

2. **配置 c3p0 连接池。**c3p0 是一个开源的 JDBC 连接池，它实现了数据源和 JNDI 绑定，支持 JDBC3 规范和 JDBC2 的标准扩展。使用连接池可以方便我们对数据库连接进行管理，类似的连接池还有 DBCP、Proxool 、 BoneCP 等。

   从线程的角度看，每创建一个数据库连接（Connection）其实都相当于创建了一个 ThreadLocal，所以连接池本质上也是一个线程池。在 [Java 并发学习笔记(二)：结构化并发应用程序](http://splitmusic.cn/2018/02/08/java-concurrency-learning-2/) 一文中，提到过通过公式计算一个最优的线程池大小，可以达到最大化的资源利用。同样，应该也可以使用相同的公式计算出一个最优的连接池大小。

3. **配置 MyBatis-Spring。**要和 Spring 一起使用 MyBatis，我们需要在 Spring 应用上下文中定义至少两样东西:一个 `SqlSessionFactory` 和至少一个数据映射器类。同时，在这里我们需要**在 resources 文件夹**创建MyBatis 的配置文件mybatis-config.xml，如下：

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE configuration
           PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
       <settings>
           <!--开启主键自增-->
           <setting name="useGeneratedKeys" value="true" />
           <!--开启列名别名-->
           <setting name="useColumnLabel" value="true" />
           <!--开启驼峰命名转换-->
           <setting name="mapUnderscoreToCamelCase" value="true" />
       </settings>
   </configuration>
   ```

   > 需要注意的是”开启驼峰命名转换“。由于 SQL 的变量命名习惯是如同“book_id”的下划线分割形式，而 Java 字段命名一般则是如同“bookId”的驼峰式命名，所以需要开启驼峰命名转换以保证命名规则能互相对应。

4. **指定与扫描 Dao 包。**

#### Service：spring-service.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="cn.damonto.ssmTest.service" />

    <!--注入数据库连接池-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--基于注解的声明式事务-->
    <tx:annotation-driven transaction-manager="transactionManager" />

</beans>
```

spring-service.xml 做了三件事：

1. 开启对 service 包的扫描。
2. 注入数据库连接池。
3. 开启基于注解的声明式事务。之所以要使用基于注解的声明式事务，主要是为了方便 service 层的开发。事务方法往往是影响服务器性能的关键，使用注解来控制事务便于后期开发的解耦，避免其他层的代码混入事务方法中。

> 在编写这个配置文件时我的一个小疑惑是： `dataSource` 这个 bean reference 是不存在于本文件的上下文环境的，所以 IDE 会提示 Cannot resolve 的错误。在这里是可以忽视这个错误提示的，因为在后期运行时我们必然需要同时加载 spring-service.xml 和 spring-dao.xml，这样在运行时上下文就可以找到 `dataSource` 。

#### Web：spring-web.xml 与 web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:context="http://mybatis.org/schema/mybatis-spring"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://mybatis.org/schema/mybatis-spring http://mybatis.org/schema/mybatis-spring.xsd">

    <context:scan base-package="cn.damonto.ssmTest.controller" />

    <!--配置 Spring MVC-->
    <mvc:annotation-driven />
    <mvc:default-servlet-handler />

    <!--配置 Spring MVC 视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
        <property name="prefix" value="/WEB-INF/jsp" />
        <property name="suffix" value=".jsp" />
    </bean>
</beans>
```

spring-web.xml 的内容就是简单的配置一下 SpringMVC 了，由于这次我没有想具体的实现 Web 层的东西，所以就只是简单的配置一下 servlet 和 ViewResolver，在这里也不做过多的深入探讨。

在 webapp 文件夹建立如下的目录结构：

```
webapp
  |WEB-INF
     |jsp
     |web.xml
```

web.xml 文件如下，配置了必要的 servlet 和 servlet-mapping：

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1" metadata-complete="true">

    <!--配置 DispatcherServlet-->
    <servlet>
        <servlet-name>ssmTest-dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>

        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/spring-*.xml</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>ssmTest-dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

</web-app>
```

至此，一个最基本的 SSM 框架构建完毕。接下来通过一个简单的测试来检查 Spring 与 Mybatis 有没有正确配置。

### (五) 应用与测试

在这里，我们通过一个简单的图书管理系统来测试我们的配置。

#### 测试准备

首先测试前要先写好最基本的实体类和接口类，实体类可以参考 [cn.damonto.ssmTest.entity](https://github.com/DamonDu/ssmTest/tree/master/src/main/java/cn/damonto/ssmTest/entity) ，Dao 接口类参考 [cn.damonto.ssmTest.dao](https://github.com/DamonDu/ssmTest/tree/master/src/main/java/cn/damonto/ssmTest/dao) 。我们只需要定义好 Dao 接口而不需要写具体实现，只要我们编写完对应的 mapper，Mybatis 会帮助我们动态地实现 Dao。

例如 BookDao 对应的 mapper 配置文件 BookDao.xml 代码如下（位于 resources/mapper）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.damonto.ssmTest.dao.BookDao">

   <select id="queryById" resultType="Book" >
       SELECT *
       FROM book
       WHERE book_id = #{bookId}
   </select>

    <select id="queryAll" resultType="Book">
        SELECT *
        FROM book
        ORDER BY book_id
        LIMIT #{offset}, #{limit}
    </select>

    <update id="reduceNumber" >
        UPDATE book
        SET book_number = book_number - 1
        WHERE book_id = #{bookId} AND book_number > 0
    </update>
</mapper>
```

AppointmentDao.xml 如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.damonto.ssmTest.dao.AppointmentDao">

    <insert id="insertAppointment">
        INSERT ignore INTO appointment (book_id, student_id)
        VALUE (#{bookId}, #{studentId})
    </insert>

    <select id="queryByKeyWithBook" resultType="Appointment">
        SELECT a.book_id, a.student_id, a.appointment_time,
          b.book_id "book.book_id", b.book_name "book.book_name", b.book_number "book.book_number"
        FROM appointment a
        INNER  JOIN book b ON a.book_id = b.book_id
        WHERE a.book_id = #{bookId} AND a.student_id = #{studentId}
    </select>
</mapper>
```

可以看到 Mybatis 让我们在 XML 中用与 SQL 语法相似的形式来进行 SQL 操作，并且将 DAO 层的代码很好地解耦，对于我们 SQL 操作的编写和 Debug 都十分方便。

当然，在测试前还需要先在数据库中建好对应的 Table 并插入一些样例数据作测试，SQL 参考如下：

```sql
create table BOOK
(
	book_id bigint(20) not null auto_increment comment '图书ID',
	book_name varchar(100) not null comment '图书名称',
	book_number int(11) not null comment '图书数量',
	primary key(book_id)
)engine=innodb auto_increment=1000 default char set=utf8;

insert into BOOK (book_id, book_name, book_number)
value
	(1000, 'bookA', 10),
    (1001, 'bookB', 10),
    (1002, 'bookC', 10),
    (1003, 'bookD', 10);
    
create table APPOINTMENT
 (
	book_id bigint(20) not null comment '图书ID',
    student_id bigint(20) not null comment '学生ID',
    appointment_time timestamp not null default current_timestamp on update current_timestamp comment '预约时间',
    primary key(book_id, student_id),
    index idx_atime(appointment_time)
 )engine=innodb default char set=utf8
```

#### 编写测试文件

在 test/java 文件夹中建立与 src/java 文件夹的包结构，并且在对应的路径下编写测试文件。

首先先写测试基类 BaseTest.java ，在这里我们加载了 spring-dao.xml 和 spring-service.xml，由于不进行 web 层的开发所以不需要 spring-web.xml：

```java
package cn.damonto.ssmTest;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class BaseTest {

}
```

接着，我们以测试 BookDao 为例：

```java
package cn.damonto.ssmTest.dao;

import cn.damonto.ssmTest.BaseTest;
import cn.damonto.ssmTest.entity.Book;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookDaoTest extends BaseTest{

    @Autowired
    private BookDao bookDao;

    @Test
    public void testQueryById () throws Exception {
        long bookId = 1000;
        Book book = bookDao.queryById(bookId);
        System.out.println(book);
    }

    @Test
    public void testQueryAll () throws Exception {
        List<Book> books = bookDao.queryAll(0, 4);
        for (Book book : books) {
            System.out.println(book);
        }
    }

    @Test
    public void testReduceNumber () throws Exception {
        long bookId = 1000;
        int number = bookDao.reduceNumber(bookId);
        System.out.println("Update = " + number);
    }
}
```

> 由于 BookDao 的实现是有 MyBatis 在运行时才进行的，所以编译时会发现 `bookDao` 的 bean 装载会提示错误，这个错误同样是可以忽视的。

在这里就不做严谨的断言测试了，直接通过控制台输出测试，若无异常应有如下的输入：

```
Book [book id = 1000, book name = bookA, books number = 10]
Book [book id = 1000, book name = bookA, books number = 10]
Book [book id = 1001, book name = bookB, books number = 10]
Book [book id = 1002, book name = bookC, books number = 10]
Book [book id = 1003, book name = bookD, books number = 10]
Update = 1
```

对于 AppointmentDao 也采取相似的测试，若同样无异常，则证明 SSM 框架配置成功。

### (六) 总结

SSM 框架和 SSH 框架几乎可以适用于所有场景的 Java Web 开发，并且可以极大地提高开发效率和提升代码质量。但是还是存在一些问题：

1. Spring 和 SpringMVC 过度地依赖 XML 进行配置，虽然已经很大简化了配置但随着项目的复杂必然还是会让配置文件越来越臃肿，不符合现在追求轻量级框架的需求。所以，新兴的更加轻量级 SpringBoot 必然会在未来取代大多数 SpringMVC 的工作。
2. 随着服务器流量和并发访问的增加，简单的 SSM 框架肯定是无法满足需求的。并且对于特定用途的服务器（例如需要高响应的游戏服务器），SSM 也是无法很好的工作。

------

*参考网站：*

1. *Github - liyifeng1994/ssm：[https://github.com/liyifeng1994/ssm](https://github.com/liyifeng1994/ssm)*
2. *Spring 中 property-placeholder 的使用与解析：[https://www.cnblogs.com/leftthen/p/5615066.html](https://www.cnblogs.com/leftthen/p/5615066.html)*
3. *mybatis-spring document：[http://www.mybatis.org/spring/zh/](http://www.mybatis.org/spring/zh/)*
4. *Ant风格路径表达式详解：[http://www.bug315.com/article/131.htm](http://www.bug315.com/article/131.htm)*
5. *c3p0数据库连接池的使用详解：[https://www.cnblogs.com/fingerboy/p/5184398.html](https://www.cnblogs.com/fingerboy/p/5184398.html)*