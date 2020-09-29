package main.java.Spring_JdbcTemplate;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.user.User;
import main.java.driver.JDBCUtils_DBCP;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;


/**
 * 使用JdbcTemplate实现对数据的增删改查。
 * 1、什么是JdbcTemplate？
 * + JdbcTemplate是Spring提供的访问数据库的方式之一，是Spring中最基本、最底层的访问数据库的实现方式。
 * + 通过使用JdbcTemplate，开发者无需关心数据库连接的创建和关闭细节，只需要专注于实现业务逻辑即可。
 * + 在使用JdbcTemplate的时候，只需要声明即可，无需自己初始化，因为Spring在初始化数据源datasource的时候会
 * 自己创建JdbcTemplate的实例。
 */
public class JdbcTemplateTest implements Dao {
    //使用数据源连接池来注册JDBC
    private JdbcTemplate jdbc = new JdbcTemplate(JDBCUtils_DBCP.getDatasource());

    public static void main(String[] args) {
        JdbcTemplateTest jdbcTest = new JdbcTemplateTest();
       /* User user = new User();
        user = jdbcTest.findUser("王二小");
        System.out.println(user);
        user.setName("李二平");
        user.setMoney(500f);
        jdbcTest.addUser(user);
        System.out.println("插入user: " + user);
        user.setId(6);
        jdbcTest.updateUser(user);*/
        System.out.println(jdbcTest.findUserById(3));
    }

    @Override
    public void addUser(User user) {

        final String sql = "insert into bank(name,money) values(?,?)";
        final Object[] args = new Object[]{user.getName(), user.getMoney()};
        //获取刚插入的主键并返回
        KeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                //PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                return ps;
            }
        }, holder);
        int id = Objects.requireNonNull(holder.getKey()).intValue();
        user.setId(id);
    }

    @Override
    public void deleteUser(String userName) {//删除操作
        String sql = "delete from bank where name=?";
        Object[] args = new Object[]{userName};
        jdbc.update(sql, args);
    }

    @Override
    public void updateUser(User user) {//更新操作
        String sql = "update bank set name=? , money=? where id=?";
        Object[] args = new Object[]{user.getName(), user.getMoney(), user.getId()};
        jdbc.update(sql, args);//返回影响的记录条数
    }

    @Override
    public User findUser(String userName) {//查询
        String sql = "select * from bank where name=?";
        Object[] args = new Object[]{userName};
        //我们原本的实现是创建一个结果集处理接口，实现处理方法。JdbcTemplate类似，只不过是利用了结果集参数反射获取属性值。
        User user = (User) jdbc.queryForObject(sql, args, new BeanPropertyRowMapper<User>(User.class));
        if (user != null)
            return user;
        else throw new NullPointerException("空异常！");
    }

    public String findUserById(int id) {
        String sql = "select name from bank where id=?";
        Object[] args = new Object[]{id};
        return jdbc.queryForObject(sql, args, String.class);
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
