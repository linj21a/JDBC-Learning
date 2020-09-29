package main.java.SomeAPI;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

public class GetKey {
    public static void main(String[] args) {
        getKeys();
    }

    private static void getKeys() {
        Connection conn = null;
        PreparedStatement ps = null;//继承自PreparedStatement,故用法类似于PreparedStatement
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("连接上数据库！");

            String sql = "insert into bank(name,money) values('大壮',1000)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//规范写法，表明可以获取主键
            //ps = conn.prepareStatement(sql);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();//因为主键不一定只有一列构成，可以为都多列，故为结果集类型。

            while (rs.next()) {
                //我们这里知道它只有一列：id
                int id = rs.getInt(1);
                System.out.println("id:" + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils_DBCP.free(conn, ps, rs);
        }
    }
}
