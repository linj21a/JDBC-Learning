package main.java.driver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 创建一个工具类，用于创建jdbc加载驱动，释放资源等，优化Test_Driver代码
 */
public class JDBCUtils_C3P0 {

    private static ComboPooledDataSource datasource;
        public static Connection getConnect() throws SQLException {//异常应该抛出
            return datasource.getConnection();
        }

        static{//使用class.forName()方法一般用于静态代码块，而且该方法注册驱动不依赖具体的类库
            try {
                //默认加载xml文件
               // Class.forName("com.mysql.cj.jdbc.Driver");
                datasource = new ComboPooledDataSource();
                //连接配置
               /* datasource.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
                datasource.setDriverClass("com.mysql.cj.jdbc.Driver");
                datasource.setPassword("ZXCVBNM55LHQq");
                datasource.setUser("root");

                //连接池配置
                datasource.setAcquireIncrement(5);//每创建的数量间隔
                datasource.setInitialPoolSize(5);//初始化池的大小
                datasource.setMaxPoolSize(20);//最大大小
                datasource.setMinPoolSize(5);//最小大小*/
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
