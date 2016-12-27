/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author ahmed
 */
public interface UserDAO {

    public void setDataSource(DataSource ds);
    public void create(User user);
    public User getUser(int id);
    public User getUser(String param,int searchBy);
    public List<User> listUsers();
    public List<User> listUsers(String pattern);
    public void delete(int id);
    public void update(int id,String pass);
}
