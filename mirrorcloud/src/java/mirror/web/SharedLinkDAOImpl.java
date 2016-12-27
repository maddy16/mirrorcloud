/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ahmed
 */
public class SharedLinkDAOImpl implements SharedLinkDAO{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void create(SharedLink sharedLink) {
        String sql = "insert into `shared_links` (`pc_id`, `link_text`, `paths`, `access`,`sharedTo`) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, sharedLink.getPcId(),sharedLink.getLinkText(),sharedLink.getPaths(),sharedLink.getAccess(),sharedLink.getSharedTo());
    }

    @Override
    public SharedLink getLink(String linkText) {
        String SQL = "select * from `shared_links` where link_text = ?";
        SharedLink sharedLink = null;
        
        try{
            sharedLink = jdbcTemplate.queryForObject(SQL, 
                        new Object[]{linkText}, new SharedLinkRowMapper());
        }
        catch(EmptyResultDataAccessException ex)
        {
            return null;
        }
        return sharedLink;
    }
    
    @Override
    public void delete(int id) {
        String SQL = "delete from `shared_links` where `link_id` = ?";
          jdbcTemplate.update(SQL, id);
    }

    @Override
    public void update(int id, SharedLink sharedLink) {
        String SQL = "update `shared_links` set `pc_id`= ?,`link_text`=?,`paths`=?,`access`=? where `link_id` = ?";
        jdbcTemplate.update(SQL, sharedLink.getPcId(),sharedLink.getLinkText(),sharedLink.getPaths(),sharedLink.getAccess(),id);
    }
    
}
class SharedLinkRowMapper implements RowMapper<SharedLink>{

    @Override
    public SharedLink mapRow(ResultSet rs, int i) throws SQLException {
        SharedLink sharedLink = new SharedLink();
        sharedLink.setLinkId(rs.getInt("link_id"));
        sharedLink.setPcId(rs.getInt("pc_id"));
        sharedLink.setAccess(rs.getInt("access"));
        sharedLink.setLinkText(rs.getString("link_text"));
        sharedLink.setPaths(rs.getString("paths"));
        sharedLink.setSharedTo(rs.getString("sharedTo"));
        return sharedLink;
    }
}