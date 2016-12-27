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
public interface PcDAO {

    public void setDataSource(DataSource ds);
    public void create(UserPC pc);
    public UserPC getPcUsingMac(int userId,String mac);
    public UserPC getPcUsingPcId(int pcId);
    public UserPC getPc(int userId,String name);
    public List<UserPC> getAllPcs(int userId);
    public void registerAnotherMac(int pcId,SystemDetail detail);
    public void delete(int pcId);
    public void updateName(int pcId,String name);
    public void updateAddress(int pcId,String address);
}
