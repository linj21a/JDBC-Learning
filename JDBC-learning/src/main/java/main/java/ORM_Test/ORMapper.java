package main.java.ORM_Test;


import main.java.DAO_design.user.User;
import main.java.driver.JDBCUtils_DBCP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

/**
 * 了解ORM是如何实现的。利用了反射机制,对象关系映射
 */

public class ORMapper {
    public static void main(String[] args) {
        String sql = "select name,money from bank where id<4";
        Object object = read(sql,Bean.class);
        System.out.println(object);

        object = read(sql, User.class);
        System.out.println(object);
    }

    private static <T>T read(String sql,Class<T> clazz) {
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
            System.out.println("列数：" + count);
            String[] columnNames = new String[count];//用于存放列名
            for (int i = 0; i < count; i++) {
                //columnNames[i]=rsMd.getColumnName(i+1);判断是别名还是原列名
                columnNames[i] = rsMd.getColumnLabel(i + 1);
            }

            //建立ORM
            T object = null;

            Method[] methods;
            if (rs.next()) {
                object = clazz.getDeclaredConstructor().newInstance();//默认有无参构造器，这是约定
                for (int i = 0; i < count; i++) {//循环添加记录进去，把所有的列都加进去。
                    String name = columnNames[i];
                    //因为我们的方法是规范方法，我们需要将name的开头字母大写
                    String re_name = name.substring(0,1).toUpperCase()+name.substring(1);
                   // System.out.println("name:"+name+" re_name:"+re_name);
                    String methodName = "set" + re_name;


                    //利用反射
                    methods = clazz.getMethods();//获取公有方法
                    for (Method m : methods) {
                        if (m.getName().equals(methodName)) {
                            m.invoke(object, rs.getObject(name));//寻找对应的方法，并调用
                        }
                    }
                }

            }
            return object;

        } catch (SQLException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException | InstantiationException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtils_DBCP.free(conn, st, rs);
        }
    }
}
