package main.java.Spring_JdbcTemplate;

import main.java.DAO_design.user.User;
import main.java.driver.JDBCUtils_DBCP;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 利用SimpleJdbcTemplate改写上面的NamedParameterJdbcTemplate实现的CRUD操作。
 */

public class Spring_SimpleJdbcTemplate {
    //使用数据源连接池来注册JDBC
    private SimpleJdbcTemplate jdbc = new SimpleJdbcTemplate(JDBCUtils_DBCP.getDatasource());

    public static void main(String[] args) {
        Spring_SimpleJdbcTemplate simpleJdbcTemplate = new Spring_SimpleJdbcTemplate();
        User u = new User();
        u.setId(5);
        u.setMoney(880);
        u.setName("王三小");
        //simpleJdbcTemplate.deleteUser(u.getName());
        //System.out.println(u);

        //返回多个会报错。。。
        System.out.println(simpleJdbcTemplate.findUser1(u));
        //simpleJdbcTemplate.updateUser(u);
        System.out.println(simpleJdbcTemplate.findUserById(3));

    }
    public void addUser(User user) {

        final String sql = "insert into bank(name,money) values(:name,:money)";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);//利用反射获取属性对应sql的参数
        //获取刚插入的主键并返回
        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.getNamedParameterJdbcOperations().update(sql,parameterSource,holder);
        int id = holder.getKey().intValue();
        user.setId(id);
    }


    public void deleteUser(String userName) {//删除操作
        //利用SimpleJdbcTemplate的话sql语句不能是具名参数。
        String sql = "delete from bank where name=?";
      /*  Map<String,String> params = new HashMap<>();//如果使用的是map来替换命名参数，则参数名可以不对应属性名。
        params.put("n",userName);*/
        jdbc.update(sql,userName);
    }


    public void updateUser(User user) {//更新操作
        String sql = "update bank set name=? , money=? where id=?";
       //可变参数
        jdbc.update(sql,user.getName(),user.getMoney(),user.getId());
    }

    public User findUser(String userName) {//查询
        String sql = "select * from bank where name=:n";
        Map params = new HashMap();//如果使用的是map来替换命名参数，则参数名可以不对应属性名。
        params.put("n",userName);

        //这里只能返回一个数据，返回多个会报错。
        return jdbc.getNamedParameterJdbcOperations().queryForObject(sql,
                params,new BeanPropertyRowMapper<User>(User.class));
    }
    public User findUser1(User user) {//查询
        String sql = "select * from bank where name=? and money=?";

        //这里只能返回一个数据，返回多个会报错。
        return jdbc.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),user.getName(),user.getMoney());
    }

    public String findUserById(int id) {
        String sql = "select name from bank where id=:id";
        Map<String,Integer> params = new HashMap<>();
        params.put("id",id);
        User user= jdbc.queryForObject(sql,new ParameterizedBeanPropertyRowMapper<User>().newInstance(User.class),
                id);//id利用了可变参数。
        return user.getName();
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
