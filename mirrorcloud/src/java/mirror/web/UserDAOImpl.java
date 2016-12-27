/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ahmed
 */
public class UserDAOImpl implements UserDAO{
    
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void create(User user) {
        String sql = "insert into `user_accounts` (`user_uname`, `user_pass`, `user_type`, `user_email`, `user_fullname`) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getUname(),user.getPass(),user.getType(),user.getEmail(),user.getFullName());
    }

    @Override
    public User getUser(int id) {
        String SQL = "select * from `user_accounts` where user_id = ?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(SQL, 
                        new Object[]{id}, new UserRowMapper());
        }
        catch(EmptyResultDataAccessException ex)
        {
            System.err.println("Query Returned Null");
            return null;
        }
        return user;
    }

    @Override
    public User getUser(String param, int searchBy) {
        User user=null;
        if(searchBy==1)
        {
            String SQL = "select * from `user_accounts` where `user_uname` = ?";
            try{
            user = jdbcTemplate.queryForObject(SQL, 
                        new Object[]{param}, new UserRowMapper());
            
            }catch(EmptyResultDataAccessException ex)
            {
                System.err.println("Query Returned Null");
                return null;
            }
        }
        else if(searchBy==2)
        {
            String SQL = "select * from `user_accounts` where `user_email` = ?";
            try{
            user = jdbcTemplate.queryForObject(SQL, 
                        new Object[]{param}, new UserRowMapper());
            
            }catch(EmptyResultDataAccessException ex)
            {
                return null;
            }
        }
        return user;
    }

    @Override
    public List<User> listUsers() {
        String SQL = "select * from Student";
      List <User> users = jdbcTemplate.query(SQL, 
                                new UserRowMapper());
      return users;
    }
    @Override
    public List<User> listUsers(String pattern) {
        String SQL = "select * from `user_accounts` where `user_uname` LIKE ? OR `user_fullname` LIKE ?";
      List <User> users = jdbcTemplate.query(SQL, new Object[]{pattern,pattern},
                                new UserRowMapper());
      return users;
    }
    @Override
    public void delete(int id) {
        String SQL = "delete from `user_accounts` where `user_id` = ?";
          jdbcTemplate.update(SQL, id);
    }

    @Override
    public void update(int id, String pass) {
         String SQL = "update `user_accounts` set `user_password` = ? where `user_id` = ?";
      jdbcTemplate.update(SQL, pass, id);
    }
    
}
class UserRowMapper implements RowMapper<User>{

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUname(rs.getString("user_uname"));
        user.setEmail(rs.getString("user_email"));
        user.setFullName(rs.getString("user_fullName"));
        user.setPass(rs.getString("user_pass"));
        user.setType(rs.getString("user_type"));
        return user;
    }
}

