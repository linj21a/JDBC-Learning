package main.java.ConnectionPool_DataSource;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 测试连接池：
 */

public class Test_1 {
    public static void main(String[]args) throws SQLException {
        for(int i=0;i<10;i++){
            Connection conn = JDBCUtils_DBCP.getConnect();
            System.out.println(conn);
            JDBCUtils_DBCP.free(conn,null,null);
        }


    }
}
