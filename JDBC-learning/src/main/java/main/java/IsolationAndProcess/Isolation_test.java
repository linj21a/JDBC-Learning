package main.java.IsolationAndProcess;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

/**
 * 修改事务的隔离级别
 */

public class Isolation_test {
    public static void main(String[]args){
        read();
    }
    private static void read(){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();

            //设置隔离级别
            conn.setTransactionIsolation(Connection.TRANSACTION_NONE);//类型是int类型，但并不是所有的都能调用，必须数据库支持。
            st = conn.createStatement();

            System.out.println("连接上数据库！");
            String sql = "select * from bank";
            rs = st.executeQuery(sql);
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                float money = rs.getFloat("money");
                System.out.printf("%d\t%s\t%.4f",id,name,money);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils_DBCP.free(conn,st,rs);
        }
    }
}
