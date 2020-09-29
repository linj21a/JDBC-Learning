package main.java.ConnectionPool_DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 使用连接池来优化
 *
 */

public class DataSource {
    private static String user = "root";
    private static String password = "ZXCVBNM55LHQq";
    private static String DB_URL =  "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
    private  LinkedList<Connection> connectionsPool;//连接池
    private  int InitCount = 5;//初始化连接池的容
    private  int MAXCONNECTION=10;//最大连接数
    private  int currentCount=0;//当前创建的连接数

    public DataSource(){
        try{
            connectionsPool = new LinkedList<>();
            for(int i=0;i<InitCount;i++){
                connectionsPool.addLast(createConnection());
                currentCount++;
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("创建连接失败，已创建数目："+currentCount);
        }
    }
    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,user,password);
    }
    public Connection getConnection() throws SQLException {
        synchronized (DataSource.class){
            if(this.connectionsPool.size()>0){
                return this.connectionsPool.removeFirst();
            }else if(currentCount<MAXCONNECTION){
                this.connectionsPool.addLast(createConnection());
                currentCount++;
                return this.connectionsPool.removeFirst();
            }else
                throw new SQLException("已经达到最大的连接数！无法创建连接！");
        }

    }
    public  void free(Connection conn){
        this.connectionsPool.addLast(conn);//头插法
        //this.currentCount--;
    }

}
