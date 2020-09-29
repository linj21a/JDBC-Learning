package main.java.ConnectionPool_DataSource;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 代理Connectin类
 */
public class Proxy_MyConnection implements Connection {//首先得实现该接口
    private Connection realConnection;//所代理的类
    private DataSource2 dataSource2;//连接池
    private int currentUser = 0;//记录当前连接了多少个客户

    /**
     * @param conn  代理接口
     * @param pool2 连接池
     */
    Proxy_MyConnection(Connection conn, DataSource2 pool2) {//构造器,别人构造不出来。只有同一包内才能构造。
        this.realConnection = conn;
        this.dataSource2 = pool2;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.realConnection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String s) throws SQLException {
        return this.realConnection.prepareStatement(s);
    }

    @Override
    public CallableStatement prepareCall(String s) throws SQLException {
        return this.realConnection.prepareCall(s);
    }

    @Override
    public String nativeSQL(String s) throws SQLException {
        return this.realConnection.nativeSQL(s);
    }

    @Override
    public void setAutoCommit(boolean b) throws SQLException {
        this.realConnection.setAutoCommit(b);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.realConnection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this.realConnection.commit();
    }

    @Override
    public void rollback() throws SQLException {
        this.realConnection.rollback();
    }

    @Override
    public void close() throws SQLException {
        //最大连接
        int maxConnect = 5;
        if (currentUser < maxConnect) {
            this.currentUser++;
            // System.out.println("user"+currentUser);
            this.dataSource2.connectionPool.addLast(this);//this就是代理接口类
        } else {
            this.realConnection.close();//最大连接数已经达到。我们真的需要关闭了。
            this.currentUser--;
        }

    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.realConnection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.realConnection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean b) throws SQLException {
        this.realConnection.setReadOnly(b);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.realConnection.isReadOnly();
    }

    @Override
    public void setCatalog(String s) throws SQLException {
        this.realConnection.setCatalog(s);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.realConnection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int i) throws SQLException {
        this.realConnection.setTransactionIsolation(i);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.realConnection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.realConnection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.realConnection.clearWarnings();
    }

    @Override
    public Statement createStatement(int i, int i1) throws SQLException {
        return this.realConnection.createStatement(i, i1);
    }

    @Override
    public PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
        return this.realConnection.prepareStatement(s, i, i1);
    }

    @Override
    public CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
        return this.realConnection.prepareCall(s, i, i1);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.realConnection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.realConnection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int i) throws SQLException {
        this.realConnection.setHoldability(i);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.realConnection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.realConnection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String s) throws SQLException {
        return this.realConnection.setSavepoint(s);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        this.realConnection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.realConnection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int i, int i1, int i2) throws SQLException {
        return this.realConnection.createStatement(i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
        return this.realConnection.prepareStatement(s, i, i1, i2);
    }

    @Override
    public CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
        return this.realConnection.prepareCall(s, i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String s, int i) throws SQLException {
        return this.realConnection.prepareStatement(s, i);
    }

    @Override
    public PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
        return this.realConnection.prepareStatement(s, ints);
    }

    @Override
    public PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
        return this.realConnection.prepareStatement(s, strings);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.realConnection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.realConnection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.realConnection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.realConnection.createSQLXML();
    }

    @Override
    public boolean isValid(int i) throws SQLException {
        return this.realConnection.isValid(i);
    }

    @Override
    public void setClientInfo(String s, String s1) throws SQLClientInfoException {
        this.realConnection.setClientInfo(s, s1);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.realConnection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String s) throws SQLException {
        return this.realConnection.getClientInfo(s);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.realConnection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String s, Object[] objects) throws SQLException {
        return this.realConnection.createArrayOf(s, objects);
    }

    @Override
    public Struct createStruct(String s, Object[] objects) throws SQLException {
        return this.realConnection.createStruct(s, objects);
    }

    @Override
    public void setSchema(String s) throws SQLException {
        this.realConnection.setSchema(s);
    }

    @Override
    public String getSchema() throws SQLException {
        return this.realConnection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this.realConnection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int i) throws SQLException {
        this.realConnection.setNetworkTimeout(executor, i);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.realConnection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return this.realConnection.unwrap(aClass);
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return this.realConnection.isWrapperFor(aClass);
    }
}
