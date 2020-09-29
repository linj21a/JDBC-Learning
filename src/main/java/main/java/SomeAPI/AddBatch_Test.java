package main.java.SomeAPI;



import main.java.driver.JDBCUtils1;

import java.sql.*;

/**
 * 批处理命令
 * 如一次插入40个数据
 */
public class AddBatch_Test {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        insert();
        long end = System.currentTimeMillis();
        System.out.println("insert:"+(end-start)+"ms");
        start = System.currentTimeMillis();
        insert_Batch();
        end = System.currentTimeMillis();
        System.out.println("insert_Batch:"+(end-start)+"ms");

    }

    private static void insert_Batch() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("connect to database....");
            conn = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            String sql = "insert into test_user(name) value(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 40; i++) {
                ps.setString(1, "name_batch" + i);
                ps.addBatch();//打包，处理
            }
            ps.executeBatch();//运行批处理命令


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils1.getInstance().free(conn, ps, rs);
        }
    }
    private static void insert(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("connect to database....");
            conn = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            String sql = "insert into test_user(name) value(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 40; i++) {
                ps.setString(1, "name" + i);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils1.getInstance().free(conn, ps, rs);
        }
    }
}