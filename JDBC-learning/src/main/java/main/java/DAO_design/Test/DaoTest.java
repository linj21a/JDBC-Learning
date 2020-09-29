package main.java.DAO_design.Test;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.Factory.DaoFactory;
import main.java.DAO_design.user.User;

public class DaoTest {
    public static void main(String[] args) {
       // User user = new User();

        Dao daoSpringJdbc = DaoFactory.getInstance();

        System.out.println(daoSpringJdbc.findUser("王二小"));
        User user = new User();
        user.setId(29);
        user.setName("王大锤");
        user.setMoney(20000f);
       /* daoSpringJdbc.addUser(user);
        System.out.println("添加的user："+user);
        daoSpringJdbc.deleteUser("容嬷嬷1");*/
        daoSpringJdbc.updateUser(user);
    }
}
