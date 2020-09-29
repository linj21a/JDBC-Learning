package main.java.EvensAndSavepoint;

import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

/**
 * 练习使用JDBC连接数据库，运行事务
 */

public class Event_test {
    public static void main(String[] args) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Savepoint s1 = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            conn.setAutoCommit(false);//将自动提交关闭
            String sql1 = "update bank set money=money-10 where name='王二小'";
            st = conn.createStatement();
            st.executeUpdate(sql1);
            String sql2 = "select money from bank where name='王小'";
            rs = st.executeQuery(sql2);
            s1 = conn.setSavepoint();
            System.out.println(s1);
            float money = 0.0f;
            while (rs.next()) {
                money = rs.getFloat("money");

            }
            if (money > 1000) {
                throw new RuntimeException("数目已经达到1000元，不需要再转入！");
            }
            System.out.println("更新王二小的钱");
            String sql3 = "update bank set money=money+10 where name='王小'";
            st.executeUpdate(sql3);
            //conn.commit();
        } catch (RuntimeException e) {
            if (conn != null && s1 != null)
                try {
                    conn.rollback(s1);//发生异常，回滚到指定的事务点
                    conn.commit();//因为有一部分sql命令执行了，所以需要提交。
                    //conn.rollback();//回滚
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils_DBCP.free(conn, st, rs);
        }
    }
}
