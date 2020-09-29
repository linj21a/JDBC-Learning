package main.java.Metadata;


import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultParameter_MetaData {
    public static void main(String[]args){
        String sql="select name as A,money from bank where id<4";
        List< Map<String,Object>> listtMap = read(sql);
        System.out.println(listtMap);
    }
    private static List< Map<String,Object>> read(String sql){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("连接上数据库！");

            st = conn.createStatement();
            rs = st.executeQuery(sql);

            ResultSetMetaData rsMd = rs.getMetaData();//获取结果集的元数据
            int count = rsMd.getColumnCount();//获取列的数量
            System.out.println("列数："+count);
            String []columnNames = new String[count];//用于存放列名
            for(int i=0;i<count;i++){
                //columnNames[i]=rsMd.getColumnName(i+1);判断是别名还是原列名
                columnNames[i]=rsMd.getColumnLabel(i+1);
            }
            for(String s:columnNames){
                System.out.print(s);
            }
            //下面将读出来的数据存起来。
            Map<String,Object> map;
            List< Map<String,Object>> listMap = new ArrayList<>();

            while(rs.next()){
                map = new HashMap<>();//创建一个map
                for(int i=0;i<count;i++){//循环添加记录进去，把所有的列都加进去。
                    System.out.println(columnNames[i]);
                    map.put(columnNames[i],rs.getObject(columnNames[i]));//存放name的值
                    //第一列是name，第二行是money
                }
                listMap.add(map);//完成一行数据的保存。
            }
            return listMap;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtils_DBCP.free(conn, st, rs);
        }
    }
}
