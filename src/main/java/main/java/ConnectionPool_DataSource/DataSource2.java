package main.java.ConnectionPool_DataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

public class DataSource2 implements DataSource {
    private static String user = "root";
    private static String password = "ZXCVBNM55LHQq";
    private static String DB_URL =  "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
     LinkedList<Connection> connectionPool;//连接池
    private  int InitCount = 3;//初始化连接池的容量
    private  int MAXCONNECTION=10;//创建的最大连接数
    private  int currentCount=0;//当前创建的连接数

    public DataSource2(){
        try{
            connectionPool = new LinkedList<>();
            for(int i=0;i<InitCount;i++){
                connectionPool.addLast(createConnection());
                currentCount++;
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError("创建连接失败，已创建数目："+currentCount);
        }
    }
    private  Connection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL,user,password);
        main.java.ConnectionPool_DataSource.Proxy_MyConnection proxy_conn = new main.java.ConnectionPool_DataSource.Proxy_MyConnection(conn,this);
        return proxy_conn;//代理类
       /*Proxy_MyConnectionHandler proxy = new Proxy_MyConnectionHandler(this);//传入的是数据源
        return proxy.bind(conn);*/
    }
    public Connection getConnection() throws SQLException {
        synchronized (DataSource2.class){
            if(this.connectionPool.size()>0){
                //System.out.println("size"+this.connectionPool.size());
                return this.connectionPool.removeFirst();
            }else if(currentCount<MAXCONNECTION){
                this.connectionPool.addLast(createConnection());
                currentCount++;
                return this.connectionPool.removeFirst();
            }else
                throw new SQLException("已经达到最大的连接数！无法创建连接！");
        }

    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public  void free(Connection conn){
        this.connectionPool.addLast(conn);//头插法
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }
}
