package main.java.DBUtils;

import main.java.driver.JDBCUtils_C3P0;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 学习DBUtils中的各个ResultSetHandler的使用
 * 在DBUtils里面，共有13个ResultSetHandler，
 * 其中2个是抽象类：
 * AbstractKeyedHandler
 * AbstractListHandler,
 * 11个是具体的实现类：
 * <p>
 * ArrayHandler, ArrayListHandler,
 * BeanHandler, BeanListHandler,
 * MapHandler, MapListHandler,
 * KeyedHandler,
 * BeanMapHandler,
 * ColumnListHandler,BaseResultSetHandler,
 * ScalarHandler
 */

public class DBUtils_ResultSetHandlerDemo {
    /**
     * ArrayHandler,将一条记录封装为一个数组_Object[]数组
     * ArrayListHandler,将多条记录封装为数组链表，即多个Object[]组成的list
     */
    @Test
    public void ArrayHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        //只查询一条记录。即使查询多条也返回一条
        Object[] objects = queryRunner.query("select * from bank where id>15", new ArrayHandler());
        System.out.println(Arrays.toString(objects));
    }

    @Test
    public void ArrayListHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        List<Object[]> list = queryRunner.query("select * from bank where id>15", new ArrayListHandler());
        for (Object[] objects : list) {
            System.out.println(Arrays.toString(objects));
        }

    }

    /**
     * BeanHandler:将一条记录封装到一个javaBean里面
     * BeanListHandler：每条记录时一个javaBean，多条记录封装成一个list。
     */
    @Test
    public void BeanHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        User user = queryRunner.query("select * from user where username=? ", new BeanHandler<User>(User.class), "dam");
        System.out.println(user.toString());
    }

    @Test
    public void BeanListHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        List<User> list = queryRunner.query("select * from user ", new BeanListHandler<User>(User.class));
        for (User u : list) {
            System.out.println(u.toString());
        }

    }

    /**
     * MapHandler:将一条记录封装到一个map
     * MapListHandler：将多个记录封装到一个list，每个记录是一个map
     */
    @Test
    public void MapHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        // //map的值是Object
        Map<String, Object> map = queryRunner.query("select * from user where username=? ", new MapHandler(), "dam");
        System.out.println(map);
    }

    @Test
    public void MapListHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        //map的值是Object
        List<Map<String, Object>> list = queryRunner.query("select * from user ", new MapListHandler());
        for (Map u : list) {
            System.out.println(u.toString());
        }

    }

    /**
     * ColumnListHandler：将数据的某一列封装到list里面
     */
    @Test
    public void ColumnListHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        //map的值是Object
        List<String> list = queryRunner.query("select * from user ", new ColumnListHandler<String>("password"));
        for (String password : list) {
            System.out.println(password.toString());
        }

    }

    /**
     * ScalarHandler:看名字旧知道，将单个值进行封装
     * 如计算记录条数什么的，count
     */
    @Test
    public void ScalarHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        //map的值是Object
        Object count = queryRunner.query("select count(*) from user ", new ScalarHandler<Long>());
        System.out.println(count);
    }
    /**
     * KeyedHandler
     * 最重要的一个：
     * 将查到的一条记录封装为map，多个记录封装为多个map
     * 外围建立一个map，其值为上述的map，key则可以指定
     */
    @Test
    public void KeyedHandler_Demo() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        //map的值是Object
        Map<Object,Map<String,Object>> map = queryRunner.query("select * from user ", new KeyedHandler<>("username"));
        for (Object key:map.keySet()
             ) {
            System.out.println(key+" "+map.get(key));
        }
    }

}
