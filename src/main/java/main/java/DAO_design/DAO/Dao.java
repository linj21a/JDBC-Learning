package main.java.DAO_design.DAO;

import main.java.DAO_design.user.User;

/**
 * DAO接口
 */
public interface Dao {
    void addUser(User user);//添加user
    void deleteUser(String userName);//删除user
    void updateUser(User user);//更新user信息
    User findUser(String userName);//查询user
    boolean toUser(User user,User toUser,float money);//转账业务
}
