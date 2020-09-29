package main.java.Metadata;



import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

/**
 * 数据库的元数据，表达了数据库配置信息。
 */

public class Database_MetaData {
    public static void main(String[]args){
        params_Test();
    }
    private static void params_Test(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("连接上数据库！");

            DatabaseMetaData metaData = conn.getMetaData();//获取数据库的元数据信息,即基本的配置


            System.out.println("驱动版本："+metaData.getDriverVersion()+"\n驱动名字："+metaData.getDriverName()+
                    "\t是否支持事务："+metaData.supportsTransactions()+"\n数据库名字"+metaData.getDatabaseProductName()+
                    "版本："+
                    metaData.getDatabaseProductVersion());


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils_DBCP.free(conn, ps, rs);
        }
    }
}
