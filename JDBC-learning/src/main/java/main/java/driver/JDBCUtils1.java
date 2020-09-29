package main.java.driver;

import java.sql.*;

/**
 * 前面使用的是静态方法优化的jdbc驱动连接数据库。
 * 下面使用单例设计模式，同时考虑以下线程安全问题。
 */

public final class JDBCUtils1 {
    private  String user = "root";
    private  String password = "ZXCVBNM55LHQq";

    //URL格式，每个数据库都有自己的格式。一般为：
    //JDBC:子协议:子名称://主机地址:端口/数据库？其他设置
    //mysql没有子名称,如果是主机可以写为"JDBC:mysql:///test"
    //注意，mysql8.0需要加上设置时区。
    private  String DB_URL =  "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
    private static JDBCUtils1 JB=null;//使用懒汉式
    private  JDBCUtils1(){}//构造器私有
    public static JDBCUtils1 getInstance(){//懒汉式
        if(JB==null){
            synchronized (JDBCUtils1.class){
                if(JB==null){//防止多线程导致的问题
                    JB = new JDBCUtils1();//最先执行静态代码块
                }
            }
        }
        return JB;
    }
    static{//使用class.forName()方法一般用于静态代码块，而且该方法注册驱动不依赖具体的类库
        try {
            //forName进行类的加载时优先加载静态代码块。
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  Connection getConnect() throws SQLException {//异常应该抛出
        return DriverManager.getConnection(DB_URL, user, password);
    }

    public  void free(Connection conn, Statement st, ResultSet res) {
        try {
            if (res != null) //原则1：晚点连接早点释放。原则2：先创建的后释放
                res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (st != null)
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null)
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
        }


    }
}
