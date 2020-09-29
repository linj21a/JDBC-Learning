package main.java.DAO_design.DAOImpl;


import main.java.DAO_design.DAO.DaoException;
import main.java.driver.JDBCUtils_DBCP;

import java.sql.*;

/**
 * 策略模式对应的类
 */
public class DaoTemplateRowMap {
    /**
     *
     * @param sql 传入的sql参数
     * @param args 占位符参数
     * @param rowMapper 结果集处理接口
     * @return 需要查询的结果
     */
    public Object find(String sql, Object[] args, RowMapper rowMapper) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            Object object = null;
            while (rs.next()) {
                object = rowMapper.rowMapper(rs);
            }
            return object;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        } finally {
            JDBCUtils_DBCP.free(conn, ps, rs);
        }

    }

    /**
     * @param sql  执行的sql语句
     * @param args sql命令中占位符的实际参数值
     */
    public void upDate(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        } finally {
            JDBCUtils_DBCP.free(conn, ps, null);
        }
    }

    public Object add(String sql, Object[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils_DBCP.getConnect();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            Object key = null;
            while (rs.next()) {
                key = rs.getObject(1);
            }
            return key;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());//千万不能随便打印堆栈跟踪或者抛出编译时异常
            //不利于将来的换其他数据库或者要修改各个接口。不知道那里出了问题。
        } finally {
            JDBCUtils_DBCP.free(conn, ps, rs);
        }

    }
}
