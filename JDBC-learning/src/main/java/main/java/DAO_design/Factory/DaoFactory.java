package main.java.DAO_design.Factory;

import main.java.DAO_design.DAO.Dao;//统一接口

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * 创建工厂类，用于生产DAO接口实例
 */
public class DaoFactory {
    private static Dao daoImpl = null;
    private DaoFactory(){}
    static{
        try {
            InputStream inputs = new FileInputStream(
                    new File("src/main/java/main/java/DAO_design/Factory/DAOImpl.properties"));
            //InputStream inputs = DaoFactory.class.getClassLoader().
            //        getResourceAsStream("/DAOImpl.properties");
            Properties properties = new Properties();
            properties.load(inputs);
            String DaoImplClass = properties.getProperty("DaoImplClass");

            //创建实例
            //daoImpl = Class.forName(DaoImplClass).newInstance();//过时方法
            //替代方案
            Class<?> clazz = Class.forName(DaoImplClass);
            daoImpl = (Dao) clazz.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException |
                InvocationTargetException | ClassNotFoundException | NoSuchMethodException | IOException e) {
            e.printStackTrace();
        }
    }

   /* public static DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null)
                    daoFactory = new DaoFactory();
            }
        }
        return daoFactory;
    }*/
    public static Dao getInstance(){
        return  daoImpl;
    }
}
