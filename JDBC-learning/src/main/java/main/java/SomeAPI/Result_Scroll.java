package main.java.SomeAPI;



import main.java.driver.JDBCUtils1;

import java.sql.*;

/**
 * 可滚动的结果集：
 */

public class Result_Scroll {
    public static void main(String[]args){
        result_scroll();
    }
    private static void result_scroll(){
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            System.out.println("connect to database....");
            conn = JDBCUtils1.getInstance().getConnect();
            System.out.println("create statement...");
            String sql = "select * from bank where id<10";

            //设置可滚动的结果集，SCROLL,SENSITIVE表示：对数据库的数据修改敏感，可以感知。
            //st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //设置可滚动更新的结果集,敏感数据集
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(sql);
            int i=0;
            while(rs.next()){
                Thread.sleep(10000);//睡眠10秒
                System.out.println("show:。。。。"+i);
                i++;
                String name = rs.getString("name");
                System.out.println("id:"+rs.getInt("id")+" name:"+name
                +" money"+rs.getFloat("money"));
            }
            //
            System.out.println("--------");
            if(rs.previous())//往前面滚动
            {
                System.out.println("id:"+rs.getInt("id")+" name:"+rs.getString("name")
                        +" money"+rs.getFloat("money"));
            }

            rs.first();//直接回到第一条数据
            System.out.println("id:"+rs.getInt("id")+" name:"+rs.getString("name")
                    +" money"+rs.getFloat("money"));
           /* rs.last();//最后一条

            rs.beforeFirst();//第一条的前面，结果集的初始态
            rs.afterLast();//最后一条数据后面
            rs.isAfterLast();//boolean,判断是否位于末尾*/
            rs.absolute(5);//直接回到第五行数据,绝对定位行号
            System.out.println("id:"+rs.getInt("id")+" name:"+rs.getString("name")
                    +" money"+rs.getFloat("money"));


        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils1.getInstance().free(conn, st, rs);
        }
    }
}
