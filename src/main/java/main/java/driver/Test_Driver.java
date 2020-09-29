package main.java.driver;

/**
 * 学习如何用jdbc连接数据库完成操作。，第一版本
 */

import java.sql.*;

public class Test_Driver {
   // public static String DB_Driver="com.mysql.jdbc.Driver";mysql5.0版本
    public static String URL = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";//服务器地址
    //6.0版本以后，改为cj还要加入时区，也可以指定不使用ssl"jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC"

    /*
    MySQL 5.5.45+, 5.6.26+ and 5.7.6+版本默认要求建立SSL连接
    mysql8.0和之前版本的区别，首先驱动换了，不是com.mysql.jdbc.Driver而是'com.mysql.cj.jdbc.Driver',
    此外mysql8.0是不需要建立ssl连接的，你需要显示关闭。最后你需要设置CST。
     */
    public  static String user="root";//用户
    public static String password="ZXCVBNM55LHQq";//密码
    public static void main(String[]args) throws SQLException {
        Connection connection= null;
        Statement statement = null;
        ResultSet res = null;

        try{
            //1、注册驱动,方式有三种
            //DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
           // Class.forName("com.mysql.cj.jdbc.Driver");
            //System.setProperty("JDBC:driver","com.mysql.cj.jdbc.Driver");

            //2、连接数据库，创建connection对象和statement对象
            System.out.println("connect to database....");
            connection = DriverManager.getConnection(URL,user,password);

            System.out.println("create statement...");
            statement = connection.createStatement();

            //3、选择sql命令操作数据库中的数据
            String sql;
            sql="select * from test_user";
            //4、执行sql语句，获取结果集
            res = statement.executeQuery(sql);//获取结果集

            //5、从结果集里面获取数据
            while(res.next()){
                int id = res.getInt("id");
                String name = res.getString("name");
                System.out.println(id+":"+name);

            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }finally {
            if(connection!=null)
                connection.close();
            if(statement!=null)
                statement.close();
            if(res!=null)
                res.close();
        }
    }
}
