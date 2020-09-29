package main.java.DAO_design.DAOImpl;

import main.java.DAO_design.DAO.Dao;
import main.java.DAO_design.DAO.DaoException;
import main.java.DAO_design.user.User;
import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

/**
 * 接口实现类，真实实现DAO接口的函数，只是单纯的处理数据。
 */

public class DaoImpl implements Dao {
    private User user;
    @Override
    public void addUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();
            String sql = "insert into bank(name,money) Values(?,?)";
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,user.getName());
            ps.setFloat(2,user.getMoney());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            while(rs.next()){
                int id = rs.getInt(1);
                user.setId(id);
            }
        } catch (SQLException e) {
           throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        }finally {
            JDBCUtils_DBCP.free(conn,ps,rs);
        }

    }

    @Override
    public void deleteUser(String userName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();
            String sql = "delete from bank where name=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        }finally {
            JDBCUtils_DBCP.free(conn,ps,rs);
        }

    }

    @Override
    public void updateUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();
            String sql = "update bank set money=? set name=? where id=?";
            ps = conn.prepareStatement(sql);
            ps.setFloat(1,user.getMoney());
            ps.setString(2,user.getName());
            ps.setInt(3,user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        }finally {
            JDBCUtils_DBCP.free(conn,ps,rs);
        }
    }

    @Override
    public User findUser(String userName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils_DBCP.getConnect();
            String sql = "select * from bank where name=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            rs = ps.executeQuery();
            while(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setMoney(rs.getFloat("money"));
                user.setName(rs.getString("name"));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        }finally {
            JDBCUtils_DBCP.free(conn,ps,rs);
        }

    }

    @Override
    public boolean toUser(User user,User toUser,float money) {
        if(money>0.0001){
            user.setMoney(user.getMoney()-money);
            toUser.setMoney(toUser.getMoney()+money);
            updateUser(user);
            updateUser(toUser);
            return true;
        }else
            throw new RuntimeException("转账金额不能为负数！");
    }
}
