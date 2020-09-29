package main.java.driver;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 阿里巴巴数据库连接池的使用的druid
 * 同c3p0使用
 */

public class JDBC_Druid {
   public static DruidDataSource dataSource;//对应方式1
   //public static DataSource dataSource;//对应方式2
    public  static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    //使用 静态代码块建立注册驱动。
    static{
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            //------------------------
            //1、使用设置属性的方式，类型是DruidDataSource
           /* dataSource = new DruidDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("ZXCVBNM55LHQq");
            dataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
            dataSource.setInitialSize(10);
            //最大连接数
            dataSource.setMaxActive(10);
            dataSource.setMinIdle(5);
            dataSource.setMaxWait(6000);*/
            //------------------------------
            //2、使用配置文件的方式，config.properties,但是这个时候创建的dataSource类型不对应于DruidDataSource
            System.out.println(JDBC_Druid.class.getClassLoader().getResource(""));
            InputStream inputs = JDBC_Druid.class.getClassLoader().getResourceAsStream("Druid.properties");
            Properties properties = new Properties();
            assert inputs != null;
            properties.load(inputs);
            /*添加强转！！*/
            dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
            //3、使用xml来配置信息

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
        return dataSource;
    }

}
