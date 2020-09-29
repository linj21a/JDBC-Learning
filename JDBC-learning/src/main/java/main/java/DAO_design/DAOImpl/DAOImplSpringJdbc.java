package main.java.DAO_design.DAOImpl;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.user.User;
import main.java.driver.JDBCUtils_DBCP;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用封装好的JDBC来改写DAO实现。
 */

public class DAOImplSpringJdbc implements Dao {
    //使用数据源连接池来注册JDBC
    private NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(JDBCUtils_DBCP.getDatasource());

    public void addUser(User user) {

        final String sql = "insert into bank(name,money) values(:name,:money)";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);//利用反射获取属性对应sql的参数
        //获取刚插入的主键并返回
        KeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(sql,parameterSource,holder);
        int id = holder.getKey().intValue();
        user.setId(id);
    }


    public void deleteUser(String userName) {//删除操作
        String sql = "delete from bank where name=:n";
        Map<String,String> params = new HashMap<>();//如果使用的是map来替换命名参数，则参数名可以不对应属性名。
        params.put("n",userName);
        jdbc.update(sql, params);
    }


    public void updateUser(User user) {//更新操作
        String sql = "update bank set name=:name , money=:money where id=:id";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);//利用反射获取属性对应sql的参数
        jdbc.update(sql,parameterSource);
    }

    public User findUser(String userName) {//查询
        String sql = "select * from bank where name=:n";
        Map params = new HashMap();//如果使用的是map来替换命名参数，则参数名可以不对应属性名。
        params.put("n",userName);

        //这里只能返回一个数据，返回多个会报错。
        return jdbc.queryForObject(sql,params,new BeanPropertyRowMapper<User>(User.class));
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
