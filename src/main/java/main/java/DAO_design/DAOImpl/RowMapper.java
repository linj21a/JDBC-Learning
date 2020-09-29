package main.java.DAO_design.DAOImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
     Object rowMapper(ResultSet rs) throws SQLException;
}
