package main.java.DBUtils;
import main.java.driver.JDBCUtils_C3P0;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用DBUtils进行CRUD操作。
 *
 */

public class DBUtils_CRUD {

    /**
     * DBUtils的增加数据的操作
     * 1、不传入datasource的话，
     */
   @Test
    public void DBUtils_Add() throws SQLException {
       QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
       /*使用可变参数的update*/
      queryRunner.update("insert into bank(name,money) values(?,?)","Amy",2000);
    }
    /**
     * DBUtils的删除数据操作
     */
    @Test
    public void DBUtils_Delete() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        queryRunner.update("delete from bank where id<10");
    }
    /**
     * DBUtils的修改数据操作
     */
    @Test
    public void DBUtils_Alter() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        queryRunner.update("update bank set money=? where id=?",2000,10);

    }

    //--------------------------------------查询操作

    /**
     * 查询一条数据,需要先将数据进行封装为对象。这里已经封装好的user
     */
    @Test
    public void DBUtils_Query() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        /*
         * 传入的是一个ResultSetHandler对象，而且泛型参数必须是我们封装的对象。
         */
        User user = queryRunner.query("select * from user where username=?", new ResultSetHandler<User>() {
            @Override
            public User handle(ResultSet resultSet) throws SQLException {
                User user1 = new User();
                while (resultSet.next()){
                    user1.setPassword(resultSet.getString("password"));
                    user1.setUserName(resultSet.getString("username"));
                }
                return user1;
            }
        },"dam");
        System.out.println(user.toString());
    }
    /**
     * 查询多条数据,需要先将数据进行封装为对象。这里已经封装好的user。使用一个list或者map来存储
     */
    @Test
    public void DBUtils_QueryBatch() throws SQLException {
        QueryRunner queryRunner = new QueryRunner(JDBCUtils_C3P0.getDatasource());
        /*
         * 传入的是一个ResultSetHandler对象，而且泛型参数必须是我们封装的对象。
         */
       List<User> list = queryRunner.query("select * from user", new ResultSetHandler<List<User>>() {
           @Override
           public List<User> handle(ResultSet resultSet) throws SQLException {
              List<User>list1 = new ArrayList<>();
              while (resultSet.next()){
                  User user = new User();
                  user.setPassword(resultSet.getString("password"));
                  user.setUserName(resultSet.getString("username"));
                  list1.add(user);
              }
              return list1;
           }
       });
        System.out.println(list.toString());
    }
}
