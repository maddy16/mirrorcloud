/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

/**
 *
 * @author ahmed
 */
public class PushedFileRecord {
    int pcId;

    Long fileId,dateModified;
    String fileName,filePath,dirtyReason;

    public String getDirtyReason() {
        return dirtyReason;
    }

    public void setDirtyReason(String dirtyReason) {
        this.dirtyReason = dirtyReason;
    }

    
    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getDateModified() {
        return dateModified;
    }

    public void setDateModified(Long dateModified) {
        this.dateModified = dateModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isIsDirty() {
        return isDirty;
    }

    public void setIsDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
    boolean isDirty;
    @Override
    public String toString()
    {
        String s = "File Id: "+fileId+", File Name: "+fileName+", File Path: "+filePath+", IsDirty: "+isDirty+", Dirty Reason: "+dirtyReason==null?"NULL":dirtyReason;
        return s;
    }
}
