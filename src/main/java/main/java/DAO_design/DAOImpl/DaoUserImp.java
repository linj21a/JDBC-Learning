package main.java.DAO_design.DAOImpl;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 父类抽取和模板设计模式简化设计代码
 */

public class DaoUserImp extends AbstractDaoObjectImpl implements Dao {
    @Override
    public void addUser(User user) {
        String sql = "insert into bank(name,money) values(?,?)";
        Object[] args = new Object[]{user.getName(), user.getMoney()};
        int id = Integer.parseInt(super.add(sql, args).toString());
        user.setId(id);
    }

    @Override
    public void deleteUser(String userName) {
        String sql = "delete from bank where name=?";
        Object[] args = new Object[]{userName};
        super.upDate(sql, args);

    }

    @Override
    public void updateUser(User user) {
        String sql = "update bank set name=? set money=? where id=?";
        Object[] args = new Object[]{user.getName(), user.getMoney(), user.getId()};
        super.upDate(sql, args);
    }

    @Override
    public User findUser(String userName) {
        String sql = "select id,name, money from bank where name=?";
        Object[] args = new Object[]{userName};
        return (User) super.find(sql, args);

    }


    @Override
    protected Object rowMapper(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setMoney(rs.getFloat("money"));
        user.setName(rs.getString("name"));

        return user;
    }

    @Override
    public boolean toUser(User user, User toUser, float money) {
        if (money > 0.0001) {
            user.setMoney(user.getMoney() - money);
            toUser.setMoney(toUser.getMoney() + money);
            updateUser(user);
            updateUser(toUser);
            return true;
        } else
            throw new RuntimeException("转账金额不能为负数！");
    }
}
