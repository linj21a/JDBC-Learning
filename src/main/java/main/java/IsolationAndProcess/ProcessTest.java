package main.java.IsolationAndProcess;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

public class ProcessTest {
    public static void main(String[] args) {
        getMoneyByName("王二小");
    }

    /**
     * 通过用户的名字获取其财产
     *
     * @param name
     */
    private static void getMoneyByName(String name) {
        Connection conn = null;
        CallableStatement cs = null;//继承自PreparedStatement,故用法类似于PreparedStatement
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();


            System.out.println("连接上数据库！");
            String sql = "{call getNameByMoney(?,?)}";

            cs = conn.prepareCall(sql);
            //需要注册存储过程输出参数，注明其类型
            cs.registerOutParameter(1, Types.FLOAT);
            //设置占位符参数
            cs.setString(2, name);
            float money = 0f;
            cs.executeQuery();//执行存储过程
            money = cs.getFloat(1);//1表示占位符位置，必须对应参数类型
            System.out.println("名字为" + name + "的客户余额为:" + money);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils_DBCP.free(conn, cs, rs);
        }


    }
}
