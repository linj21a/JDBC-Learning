package main.java.driver;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.*;

/**
 * 学习如何用jdbc连接数据库完成操作。，第二版本使用工具类静态方法优化。
 */

public class Test_Driver2 {
    public static void main(String[]args) {
        Connection connection= null;
        Statement statement = null;
        ResultSet res = null;
        try{
            System.out.println("connect to database....");
            DruidDataSource  dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");/*是setUsername而不是setName*/
            dataSource.setPassword("ZXCVBNM55LHQq");
            dataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
            dataSource.setInitialSize(10);
            //最大连接数
            dataSource.setMaxActive(10);
            dataSource.setMinIdle(5);
            dataSource.setMaxWait(6000);
            connection = dataSource.getConnection();
            //connection = JDBCUtils_DBCP.getConnect();
            //connection = JDBCUtils_C3P0.getConnect();

            System.out.println("create statement...");
            statement = connection.createStatement();

            //3、选择sql命令操作数据库中的数据
            String sql;
            sql="select * from bank";
            //4、执行sql语句，获取结果集
            res = statement.executeQuery(sql);//获取结果集

            //5、从结果集里面获取数据
            while(res.next()){
                int id = res.getInt("id");
                String name = res.getString("name");
                System.out.println(id+":"+name);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
                JDBCUtils_C3P0.free(connection,statement,res);
        }
    }
}
