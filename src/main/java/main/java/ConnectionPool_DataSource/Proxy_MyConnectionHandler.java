package main.java.ConnectionPool_DataSource;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class Proxy_MyConnectionHandler implements InvocationHandler {
    private DataSource2 dataSource2;
    private int currentUser = 0;//记录当前连接了多少个客户
    private Connection realConnection;
    private Connection warpedConnection;

    public Proxy_MyConnectionHandler(DataSource2 dataSource) {
        this.dataSource2 = dataSource;
    }

    /**
     * 用于绑定真正的实现类
     *
     * @param realConnection 真正的连接
     * @return 返回包裹后的类，代理类。
     */
    Connection bind(Connection realConnection) {//用于绑定真正的连接realConnection
        this.realConnection = realConnection;
        this.warpedConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{Connection.class}, this);

        return this.warpedConnection;

    }

    /**
     * 我们关心的方法，我们现在只重写close方法。利用了反射。
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("close".equals(method.getName())) {//只要有人调用该方法，就拦截下来进行以下操作。
            //最大连接
            int maxConnect = 5;
            if (currentUser < maxConnect) {
                this.currentUser++;
                // System.out.println("user"+currentUser);
                this.dataSource2.connectionPool.addLast(this.warpedConnection);//this就是代理接口类
            } else {
                this.realConnection.close();//最大连接数已经达到。我们真的需要关闭了。
                this.currentUser--;
            }
        }
        return method.invoke(this.realConnection, objects);//调用该方法。
    }
}
