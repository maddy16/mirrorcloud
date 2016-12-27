/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.taglibs.standard.lang.jstl.test.beans.PublicBean1;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author ahmed
 */
public class PushedFileDaoImpl implements PushedFileDao{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }
    @Override
    public void create(PushedFileRecord pushedFileRecord,int dirtyReason) {
        String sql = "insert into `uploaded_files` (`pc_id`, `file_name`, `file_path`, `date_modified`,`is_dirty`,`dirty_reason`) VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, pushedFileRecord.getPcId(),pushedFileRecord.getFileName(),pushedFileRecord.getFilePath(),pushedFileRecord.getDateModified(),pushedFileRecord.isDirty,DirtyReason.getDirtyReason(dirtyReason));
    }

    @Override
    public PushedFileRecord getPushedFileRecord(Long fileId, int pcId) {
        String SQL = "select * from `uploaded_files` where file_id = ? AND pc_id=?";
        PushedFileRecord pushedFileRecord = jdbcTemplate.queryForObject(SQL,new Object[]{fileId,pcId}, new PushedFileRecordRowMapper());
        return pushedFileRecord;
    }
    
    @Override
    public PushedFileRecord getPushedFileRecord(String filePath, int pcId) {
        PushedFileRecord pushedFileRecord =null;
        String SQL = "select * from `uploaded_files` where `file_path` = ? AND `pc_id`=?";
        try{
        pushedFileRecord = jdbcTemplate.queryForObject(SQL,new Object[]{filePath,pcId}, new PushedFileRecordRowMapper());
        }catch(EmptyResultDataAccessException ex){
                return null;
        }
        return pushedFileRecord;
    }
    
    @Override
    public List<PushedFileRecord> listPushedFiles(int pcId) {
        String SQL = "select * from `uploaded_files` where pc_id=?";
        List <PushedFileRecord> pushedFiles = jdbcTemplate.query(SQL,new Object[]{pcId},new PushedFileRecordRowMapper());
        return pushedFiles;
    }

    @Override
    public void delete(Long fileId, int pcId) {
        String SQL = "delete from `uploaded_files` where `pc_id` = ? AND `file_id` = ?";
        jdbcTemplate.update(SQL, pcId,fileId);
    }

    @Override
    public void update(Long fileId, int pcId, PushedFileRecord pushedFileRecord) {
        String SQL = "update `uploaded_files` set `file_name` = ? , `file_path` = ? , `date_modified` = ? , `is_dirty` = ? ,  `dirty_reason` = ? where `pc_id` = ? AND `file_id` = ?";
        jdbcTemplate.update(SQL, pushedFileRecord.getFileName(), pushedFileRecord.getFilePath(),pushedFileRecord.getDateModified(),pushedFileRecord.isIsDirty(),pushedFileRecord.getDirtyReason(), pcId, fileId);
    }

    @Override
    public List<String> getImmediateChilds(String suffix,int num, int pcId) {
//        SELECT DISTINCT substring_index(substring_index(`file_path`,'\\',3), '\\' ,-1) FROM uploaded_files where `file_path` LIKE 'D_Games' AND `pc_id`=11 AND `file_path` != 'drive' AND ROUND ((LENGTH(`file_path`)- LENGTH( REPLACE ( `file_path`, '\\', ''))) / LENGTH('\\'))>=2 AND IFNULL(`dirty_reason`,'null')!='file deleted'
        List <String> pushedFiles = null;
        try{
        String SQL = "SELECT DISTINCT substring_index(substring_index(`file_path`,?,?), ? ,-1) FROM uploaded_files where `file_path` LIKE ? AND `pc_id`=? AND `file_path` != 'drive' AND ROUND ((LENGTH(`file_path`)- LENGTH( REPLACE ( `file_path`, ?, ''))) / LENGTH(?))>=? AND IFNULL(`dirty_reason`,'null')!='file deleted'";
        pushedFiles = jdbcTemplate.query(SQL,new Object[]{"\\",new Integer(num), "\\",suffix,pcId, "\\", "\\",new Integer(num-1)},new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString(1);
            }
        });
        
        }catch(EmptyResultDataAccessException ex){
                return null;
        }
        return pushedFiles;
    }
    @Override
    public List<String> getDrives(int pcId) {
        List <String> pushedFiles = null;
        try{
        String SQL = "SELECT * FROM `uploaded_files` WHERE `file_path` = 'drive' AND `pc_id` = ?";
        pushedFiles = jdbcTemplate.query(SQL,new Object[]{pcId},new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("file_name");
            }
        });
        
        }catch(EmptyResultDataAccessException ex){
                return null;
        }
        return pushedFiles;
    }

    @Override
    public void deleteDrives(int pcId) {
        String SQL = "delete from `uploaded_files` where `pc_id` = ? AND `file_path`='drive'";
        jdbcTemplate.update(SQL, pcId);
    }

    @Override
    public List<PushedFileRecord> getAllDirtyFiles(int pcId) {
        List<PushedFileRecord> pushedFiles = null;
        try{
        String SQL = "SELECT * FROM uploaded_files where `is_dirty`=1 AND `pc_id`=? ";
        pushedFiles = jdbcTemplate.query(SQL,new Object[]{pcId},new PushedFileRecordRowMapper());
        
        }catch(EmptyResultDataAccessException ex){
                return null;
        }
        return pushedFiles;
    }

    @Override
    public List<PushedFileRecord> getFilesInDirectory(int pcId, String path) {
        path = path.replace("\\", "_");
        String SQL = "SELECT * FROM `uploaded_files` WHERE `file_path` LIKE ? AND `pc_id` = ?";
        List<PushedFileRecord> pushedFiles = null;
        try{
        pushedFiles = jdbcTemplate.query(SQL,new Object[]{path+"%",pcId},new PushedFileRecordRowMapper());
        
        }catch(EmptyResultDataAccessException ex){
                return null;
        }
        return pushedFiles;
    }

    
    class PushedFileRecordRowMapper implements RowMapper<PushedFileRecord>{

        @Override
        public PushedFileRecord mapRow(ResultSet rs, int i) throws SQLException {
            PushedFileRecord pushedFileRecord = new PushedFileRecord();
            pushedFileRecord.setFileId(new Long(rs.getInt("file_id")+""));
            pushedFileRecord.setPcId(rs.getInt("pc_id"));
            pushedFileRecord.setFileName(rs.getString("file_name"));
            pushedFileRecord.setFilePath(rs.getString("file_path"));
            pushedFileRecord.setDateModified(rs.getLong("date_modified"));
            pushedFileRecord.setIsDirty(rs.getBoolean("is_dirty"));
            pushedFileRecord.setDirtyReason(rs.getString("dirty_reason"));
            return pushedFileRecord;
        }
    }
}