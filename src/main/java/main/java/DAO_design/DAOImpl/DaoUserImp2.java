package main.java.DAO_design.DAOImpl;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 使用策略模式改进模板方法设计的不足：依赖具体的抽象方法实现。一改则需要改很多处代码。
 */

public class DaoUserImp2 implements Dao {
    private DaoTemplateRowMap templateRowMap=new DaoTemplateRowMap();

    public User findUser(String userName) {
        String sql = "select id,name, money from bank where name=?";
        Object[] args = new Object[]{userName};
        //new RowMapper{}用lambada表达式取代。
        return (User) templateRowMap.find(sql, args, new RowMapper() {
            @Override
            public Object rowMapper(ResultSet rs) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setMoney(rs.getFloat("money"));
                user.setName(rs.getString("name"));

                return user;
            }
        });

    }
    public Float findByName(String userName){
        String sql = "select money from bank where name=?";
        Object[] args = new Object[]{userName};
        return (Float)templateRowMap.find(sql, args, new RowMapper() {
            @Override
            public Object rowMapper(ResultSet rs) throws SQLException {
                Object object = rs.getFloat("money");
                return object;
            }
        });

    }

        public void addUser(User user) {
            String sql = "insert into bank(name,money) values(?,?)";
            Object[] args = new Object[]{user.getName(), user.getMoney()};
            int id = Integer.parseInt(templateRowMap.add(sql, args).toString());
            user.setId(id);
        }


        public void deleteUser(String userName) {
            String sql = "delete from bank where name=?";
            Object[] args = new Object[]{userName};
            templateRowMap.upDate(sql, args);

        }


        public void updateUser(User user) {
            String sql = "update bank set name=? set money=? where id=?";
            Object[] args = new Object[]{user.getName(), user.getMoney(), user.getId()};
            templateRowMap.upDate(sql, args);
        }




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

