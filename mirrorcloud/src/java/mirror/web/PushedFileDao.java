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
public interface PushedFileDao {
    public void setDataSource(DataSource ds);
    public void create(PushedFileRecord pushedFileRecord,int dirtyReason);
    public PushedFileRecord getPushedFileRecord(Long fileId,int pcId);
    public PushedFileRecord getPushedFileRecord(String filePath,int pcId);
    public List<PushedFileRecord> listPushedFiles(int pcId);
    public List<PushedFileRecord> getFilesInDirectory(int pcId,String path);
    public List<String> getImmediateChilds(String suffix,int num, int pcId);
    public List<String> getDrives(int pcId);
    public List<PushedFileRecord> getAllDirtyFiles(int pcId);
    public void deleteDrives(int pcId);
    public void delete(Long fileId,int pcId);
    public void update(Long fileId,int pcId,PushedFileRecord pushedFileRecord);
}
