package main.java.CRUD_test;


import main.java.driver.JDBCUtils1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 使用JDBC实现数据库mysql的增删改查CRUD
 */

public class JDBC_CRUD {
    private static Connection connection= null;
    private static Statement statement = null;
    private static ResultSet res = null;
    public static void main(String[]args){
        //create();
        //read();
        //update();
       // delete();

    }
    public static void create(){
        try{
            System.out.println("connect to database....");
            connection = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            statement = connection.createStatement();
            String sql;
            sql="insert into test_user(id,name) values(6,'王二小')";
            //4、执行sql语句，获取结果集
            int i = statement.executeUpdate(sql);//获取影响的记录条数
            System.out.println("i="+i);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils1.getInstance().free(connection,statement,res);
        }
    }
    public static void read(){
        try{
            System.out.println("connect to database....");
            connection = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            statement = connection.createStatement();
            String sql;
            sql="select id,name from test_user where id>4";
            //4、执行sql语句，获取结果集
            res = statement.executeQuery(sql);//获取结果集
            while(res.next()){
                int id = res.getInt("id");
                String name = res.getString("name");
                System.out.println(id+":"+name);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils1.getInstance().free(connection,statement,res);
        }

    }
    public static void update(){
        try{
            System.out.println("connect to database....");
            connection = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            statement = connection.createStatement();
            String sql;
            sql="update test_user set name='王二蛋' where id>4 ";
            //4、执行sql语句，获取结果集
            int i = statement.executeUpdate(sql);//获取影响的记录条数
            System.out.println("i="+i);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils1.getInstance().free(connection,statement,res);
        }


    }
    public static void delete(){
        try{
            System.out.println("connect to database....");
            connection = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            statement = connection.createStatement();
            String sql;
            sql="delete from test_user where name='王二蛋'";
            //4、执行sql语句，获取结果集
            int i = statement.executeUpdate(sql);//获取影响的记录条数
            System.out.println("i="+i);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils1.getInstance().free(connection,statement,res);
        }

    }

}
