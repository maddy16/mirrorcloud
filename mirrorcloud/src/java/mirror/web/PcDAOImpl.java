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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ahmed
 */
public class PcDAOImpl implements PcDAO{
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void create(UserPC pc) {
        String sql = "insert into `user_pcs` (`user_id`, `pc_name`,`pc_address`) VALUES(?,?,?)";
        jdbcTemplate.update(sql, pc.getUserId(),pc.getPcName(),pc.getAddress());
        sql = "Select * from `user_pcs` where `pc_name`=? AND `user_id`=?";
        int i = jdbcTemplate.queryForObject(sql,new Object[]{pc.getPcName(),pc.getUserId()}, new RowMapper<Integer>(){

            @Override
            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getInt("pc_id");
            }
            
        });
        
        pc.setPcId(i);
        pc.getDetail().setPcId(i);
        SystemDetail system = pc.getDetail();
        sql = "insert into `systems` (`pc_id`, `machine_name`, `os_name`, `machine_model`, `machine_cpu`, `machine_memory`, `machine_mac`) VALUES(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, system.getPcId(),system.getMachineName(),system.getOsName(),system.getSysModel(),system.getCpu(),system.getMemory(),system.getMacAddress());
    }

    @Override
    public UserPC getPcUsingMac(int userId, String mac) {
        String SQL = "select * from `systems`,`user_pcs` where `systems`.`pc_id` = `user_pcs`.`pc_id` AND user_id = ? AND `systems`.`machine_mac`=?";
      UserPC pc=null;
      try{
          pc = jdbcTemplate.queryForObject(SQL,new Object[]{userId,mac}, new UserPCRowMapper());
      }
      catch(EmptyResultDataAccessException ex)
      {
          ex.printStackTrace();
        return null;
      }
      return pc;
    }

    @Override
    public UserPC getPc(int userId, String name) {
        String SQL = "select * from `user_pcs`,`systems`  where `systems`.`pc_id`=`user_pcs`.`pc_id`  AND user_id = ? AND `pc_name` = ?";
      UserPC pc = null;
      try{
          pc = jdbcTemplate.queryForObject(SQL,new Object[]{userId,name}, new UserPCRowMapper());
      }
      catch(EmptyResultDataAccessException ex)
      {
        return null;
      }
      return pc;
    }

    @Override
    public void delete(int pcId) {
        String SQL = "delete from `systems` where `pc_id` = ?";
        jdbcTemplate.update(SQL, pcId);
        SQL = "delete from `shared_links` where `pc_id` = ?";
        jdbcTemplate.update(SQL, pcId);
        SQL = "delete from `user_pcs` where `pc_id` = ?";
        jdbcTemplate.update(SQL, pcId);
    }

    @Override
    public void updateName(int pcId, String name) {
        String SQL = "update `user_pcs` set `pc_name` = ? where `pc_id` = ?";
      jdbcTemplate.update(SQL, name, pcId);
    }
    public void updateAddress(int pcId,String address){
        String SQL = "update `user_pcs` set `pc_address` = ? where `pc_id` = ?";
      jdbcTemplate.update(SQL, address, pcId);
    }
    public void updateSystemDetails(UserPC pc)
    {
        SystemDetail system = pc.getDetail();
        String SQL = "update `systems` set `machine_name` = ? , `os_name` = ?, `machine_model` = ?, `machine_cpu` = ?, `machine_memory` = ?, `machine_mac` = ?  where `pc_id` = ?";
        jdbcTemplate.update(SQL, system.getMachineName(), system.getOsName(),system.getSysModel(),system.getCpu(),system.getMemory(),system.getMacAddress(),pc.getPcId());
    }

    @Override
    public void registerAnotherMac(int pcId,SystemDetail system) {
        String sql = "insert into `systems` (`pc_id`, `machine_name`, `os_name`, `machine_model`, `machine_cpu`, `machine_memory`, `machine_mac`) VALUES(?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, pcId,system.getMachineName(),system.getOsName(),system.getSysModel(),system.getCpu(),system.getMemory(),system.getMacAddress());
    }

    @Override
    public List<UserPC> getAllPcs(int userId) {
        String SQL = "select * from `user_pcs`,`systems`  where `systems`.`pc_id`=`user_pcs`.`pc_id`  AND user_id = ?";
      List<UserPC> pcs = null;
      try{
          pcs = jdbcTemplate.query(SQL,new Object[]{userId}, new UserPCRowMapper());
      }
      catch(EmptyResultDataAccessException ex)
      {
        return null;
      }
      return pcs;
    }

    @Override
    public UserPC getPcUsingPcId(int pcId) {
        String SQL = "select * from `user_pcs`,`systems`  where `systems`.`pc_id`=`user_pcs`.`pc_id`  AND `user_pcs`.`pc_id`=?";
      UserPC pc = null;
      try{
          pc = jdbcTemplate.queryForObject(SQL,new Object[]{pcId}, new UserPCRowMapper());
      }
      catch(EmptyResultDataAccessException ex)
      {
        return null;
      }
      return pc;
    }
    
}
class UserPCRowMapper implements RowMapper<UserPC>{

    @Override
    public UserPC mapRow(ResultSet rs, int i) throws SQLException {
        SystemDetail system = new SystemDetail();
        system.setSystemId(rs.getInt("system_id"));
        system.setPcId(rs.getInt("pc_id"));
        system.setMachineName(rs.getString("machine_name"));
        system.setOsName(rs.getString("os_name"));
        system.setSysModel(rs.getString("machine_model"));
        system.setCpu(rs.getString("machine_cpu"));
        system.setMemory(rs.getString("machine_memory"));
        system.setMacAddress(rs.getString("machine_mac"));
        UserPC pc = new UserPC();
        pc.setPcId(rs.getInt("pc_id"));
        pc.setDetail(system);
        pc.setUserId(rs.getInt("user_id"));
        pc.setPcName(rs.getString("pc_name"));
        pc.setAddress(rs.getString("pc_address"));
        return pc;
    }
}
