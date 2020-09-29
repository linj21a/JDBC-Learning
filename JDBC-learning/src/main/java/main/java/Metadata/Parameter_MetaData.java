package main.java.Metadata;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

public class Parameter_MetaData {
    public static void main(String[] args) {
        params_Test();
    }

    private static void params_Test() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("连接上数据库！");

            System.out.println("利用ParameterMetaData获取sql参数信息：");
            String sql = "select name,money from bank where name>? and id<? and money<?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "大壮");
            ps.setInt(2, 2);
            ps.setFloat(3, 100f);
            //因为没有进行查询数据库。
            ParameterMetaData pmds = ps.getParameterMetaData();
            int count = pmds.getParameterCount();//获取参数的个数
            System.out.println("参数个数：" + count);
            for (int i = 1; i <= count; i++) {
                System.out.println("参数名：" + pmds.getParameterClassName(i));
                System.out.println("类型名：" + pmds.getParameterTypeName(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils_DBCP.free(conn, ps, rs);
        }
    }
}
