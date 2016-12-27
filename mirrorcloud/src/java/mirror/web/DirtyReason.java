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
public class DirtyReason {
    public static final int NULL = 0;
    public static final int NEW_FILE_CREATED = 1;
    public static final int FILE_DELETED = 2;
    
    public static String getDirtyReason(int reasonId)
    {
        String reason = "";
        switch(reasonId)
        {
            case NULL:
                reason = null;
                break;
            case NEW_FILE_CREATED:
                reason = "new file";
                break;
            case FILE_DELETED:
                reason = "file deleted";
                break;
        }
        return reason;
    }
}
