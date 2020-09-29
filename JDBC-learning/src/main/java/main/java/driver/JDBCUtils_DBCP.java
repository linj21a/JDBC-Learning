package main.java.driver;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * 创建一个工具类，用于创建jdbc加载驱动，释放资源等，优化Test_Driver代码
 */

public final class JDBCUtils_DBCP {
   /* private static String user = "root";
    private static String password = "ZXCVBNM55LHQq";

    //URL格式，每个数据库都有自己的格式。一般为：
    //JDBC:子协议:子名称://主机地址:端口/数据库？其他设置
    //mysql没有子名称,如果是主机可以写为"JDBC:mysql:///test"
    //注意，mysql8.0需要加上设置时区。
    private static String DB_URL =  "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";*/
//&generateSimpleParameterMetadata=true";
    //private static DataSource datasource;//数据源1
    private static DataSource datasource;//代理模式的数据源2
    public static Connection getConnect() throws SQLException {//异常应该抛出
        //return DriverManager.getConnection(DB_URL, user, password);
        return datasource.getConnection();
    }

    static{//使用class.forName()方法一般用于静态代码块，而且该方法注册驱动不依赖具体的类库
        try {
            //forName进行类的加载时优先加载静态代码块。
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println(JDBCUtils.class.getClassLoader().getResource(""));
            InputStream inputs = JDBCUtils_DBCP.class.getClassLoader().getResourceAsStream("DBCP_Config.properties");
            Properties properties = new Properties();
            assert inputs != null;
            properties.load(inputs);
            datasource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void free(Connection conn, Statement st, ResultSet res) {
        try {
            if (res != null) //原则1：晚点连接早点释放。原则2：先创建的后释放
                res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

                try {
                    if (st != null)
                        st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null)
                        try {
                            conn.close();
                            //datasource.free(conn);//重用，将创建的连接不关闭，而是将其回收到连接池
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
        }


    }

    public static DataSource getDatasource() {
        return datasource;
    }
}
