package main.java.LoadFileTest;



import main.java.driver.JDBCUtils_DBCP;

import java.io.*;
import java.sql.*;

/**
 * 使用JDBC将文本文件插入到数据库并读取出来
 * 文件：Text_Test.java 类型文本类型
 * 使用的数据库数据类型为：Text
 * 涉及：I/O流操作
 */
public class Text_Test {
    public static void main(String[]args){
        //insert_Text();
        read_Text();
    }

    private static void insert_Text() {
        Connection conn =null;
        PreparedStatement ps = null;
        ResultSet re = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("connect database ....");
            String sql = "insert into big_text(Big_text) Values(?)";
            ps = conn.prepareStatement(sql);
            File f = new File("src/LoadFileTest/Text_Test.java");//创建文件对象
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f));//装饰流
            ps.setCharacterStream(1,bufferedReader,f.length());//读取

            ps.executeUpdate();

            System.out.println("insert successfully！");

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils_DBCP.free(conn,ps,re);
        }
    }
    private static void read_Text() {
        Connection conn =null;
        Statement st = null;
        ResultSet re = null;
        try{

            conn = JDBCUtils_DBCP.getConnect();
            System.out.println("connect database ....");
            String sql = "select big_text from big_text where id=1";
            st = conn.createStatement();
            re = st.executeQuery(sql);
            File f = new File("E:\\Text_Test_bak.java");//保存的文件对象
            BufferedReader bf;
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            while(re.next()){
                //String s = re.getString("big_test");//这种方法也行
               // 这种也行
               /*Clob clob =  re.getClob(1);//获取第一列数据
               bf = new BufferedReader( clob.getCharacterStream());*/
                bf = new BufferedReader(re.getCharacterStream("big_text"));
                String buff;
                while((buff=bf.readLine())!=null){
                    bw.write(buff);
                    bw.newLine();
                }
                bw.close();
                bf.close();
            }
            System.out.println("read successfully！");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils_DBCP.free(conn,st,re);
        }
    }
}
