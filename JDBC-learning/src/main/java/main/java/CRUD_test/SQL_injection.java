package main.java.CRUD_test;



import main.java.driver.JDBCUtils1;

import java.sql.*;

/**
 * 了解SQL注入问题
 */

public class SQL_injection {
    private static Connection connection= null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet res = null;
    public static void main(String[]args){
        String name="' or 1 or '";
        read(name);
    }

    private static void read(String s) {

        try{
            System.out.println("connect to database....");
            connection = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            String sql;
            sql="select id,name from test_user where name=?";//使用？号表示未知参数
            System.out.println(sql);
            //4、执行sql语句，获取结果集
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,s);//将第一个问号参数设置为s

            res= preparedStatement.executeQuery(sql);//执行，不用sql，如果填入sql，则之前的设置失效

            while(res.next()){
                int id = res.getInt("id");
                String name = res.getString("name");
                System.out.println(id+":"+name);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils1.getInstance().free(connection,preparedStatement,res);
        }

    }
}
