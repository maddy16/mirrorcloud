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
public interface SharedLinkDAO {
    public void setDataSource(DataSource ds);
    public void create(SharedLink sharedLink);
    public SharedLink getLink(String linkText);
    public void delete(int id);
    public void update(int id,SharedLink sharedLink);
}
