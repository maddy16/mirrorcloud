/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static mirror.web.DataController.LOCATION;
import static mirror.web.DataController.createParentDirs;
import static mirror.web.DataController.decode;
import static mirror.web.DataController.sendDownload;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ahmed
 */
@Controller
public class AndroidController {
    @Autowired
    PushedFileDao fileDao;
    @Autowired
    PcDAO pcDao;
    @Autowired
    UserDAO userDAO;
    @Autowired
    SharedLinkDAO sharedLinkDAO;
    @RequestMapping(value = "/pingAnd",method = RequestMethod.GET)
    public @ResponseBody String ping(){
        System.out.println("Pinged By Android");
        JSONObject res = new JSONObject();
        res.accumulate("success", true);
        return res.toString();
    }
    @RequestMapping(value = "/andLogin",method = RequestMethod.POST)
    public @ResponseBody String androidlogin(@RequestParam("uname") String uname,@RequestParam("pass") String pass)
    {
        System.out.println("Request");
        try
        {
            User user1 = userDAO.getUser(uname, 1);
            if(user1!=null)
            {
                if(pass.equals(user1.getPass()))
                {
                    if(user1.getType().equalsIgnoreCase("master"))
                    {
                        JSONObject json = new JSONObject();
                        json.accumulate("correct", true);
                        json.accumulate("user_id", user1.getId());
                        json.accumulate("user_email", user1.getEmail());
                        json.accumulate("user_fullName", user1.getFullName());
                        return json.toString();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            String msg = ex.getMessage();
            if(msg.contains("Could not get JDBC Connection"))
            {
                System.err.println("Database Server Offline");
            }
            ex.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.accumulate("correct", false);
        json.accumulate("errormsg", "Invalid User");
        return json.toString();
    }
    @RequestMapping(value = "/andGetPcs",method = RequestMethod.POST)
    public @ResponseBody String getAllPcs(@RequestParam("userId") int userId,HttpServletRequest request){
        JSONObject resp = new JSONObject();
        try
        {
            
            User user = userDAO.getUser(userId);
            if(user==null)
            {
                resp.accumulate("success", false);
                resp.accumulate("err", "Invalid User");
            }
            else
            {
                List<UserPC> allPcs = pcDao.getAllPcs(user.getId());
                if(allPcs==null)
                {
                    resp.accumulate("success", false);
                    resp.accumulate("err", "No PC Registered");
                }
                else
                {
                    if(allPcs.size()==0)
                    {
                        resp.accumulate("success", false);
                        resp.accumulate("err", "No PC Registered");
                        
                    }
                    else
                    {
                        resp.accumulate("success", true);
                        resp.accumulate("pcs", allPcs);
//                        model.addAttribute("pcs",allPcs);
                    }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            if(ex.getMessage().contains("Last packet sent to the server was 0 ms ago.")){
                resp.accumulate("success", false);
                resp.accumulate("err", "Database Server Error");
            }
            else{
                resp.accumulate("success", false);
                resp.accumulate("err", ex.getMessage());
            }
        }
        return resp.toString();
    }
    @RequestMapping(value = "/andGetPartitions",method = RequestMethod.POST)
    public @ResponseBody String getPartitions(@RequestParam("userId") int userId,HttpServletRequest request){
        System.out.println("USERID = "+userId);
        JSONObject resp = new JSONObject();
        try
        {
            
            User user = userDAO.getUser(userId);
            if(user==null)
            {
                resp.accumulate("success", false);
                resp.accumulate("err", "Invalid User");
            }
            else
            {
                List<UserPC> allPcs = pcDao.getAllPcs(user.getId());
                if(allPcs==null)
                {
                    resp.accumulate("success", false);
                    resp.accumulate("err", "No PC Registered");
                }
                else
                {
                    if(allPcs.size()==0)
                    {
                        resp.accumulate("success", false);
                        resp.accumulate("err", "No PC Registered");
                        
                    }
                    else
                    {
//                        model.addAttribute("pcs",allPcs);
                        List<String> immediateChilds=null;
                        if(request.getParameter("pcId")!=null)
                        {
//                            model.addAttribute("pc_id",request.getParameter("pcId"));
//                            model.addAttribute("thisPc",pcDao.getPcUsingPcId(Integer.parseInt(request.getParameter("pcId"))));
                            immediateChilds = fileDao.getDrives(Integer.parseInt(request.getParameter("pcId")));
                        }
                        else
                        {
                            immediateChilds = fileDao.getDrives(allPcs.get(0).getPcId());
//                            model.addAttribute("thisPc",allPcs.get(0));
//                            model.addAttribute("pc_id",allPcs.get(0).getPcId());
                        }
        //                List<String> immediateChilds = fileDao.getDrives(4);
                        if(immediateChilds==null)
                        {
//                            model.addAttribute("files", null);
                            resp.accumulate("success", true);
                            resp.accumulate("files", null);
                        }
                        else
                        {
                            resp.accumulate("success", true);
                            resp.accumulate("files", immediateChilds);
                        }
                    }
                }
                
//                SystemDetail details= systemDao.getDetails(user.getId());
//                model.addAttribute("system", details);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            if(ex.getMessage().contains("Last packet sent to the server was 0 ms ago.")){
                resp.accumulate("success", false);
                resp.accumulate("err", "Database Server Error");
            }
            else{
                resp.accumulate("success", false);
                resp.accumulate("err", ex.getMessage());
            }
        }
        return resp.toString();
    }
    @RequestMapping(value = "/andGetData",method = RequestMethod.POST)
    public @ResponseBody String getData(@RequestParam("path") String path,@RequestParam("num") int num,@RequestParam("pcId") int pcId,@RequestParam("userId") int userId,HttpServletRequest request){
        path = DataController.decode(path);
        JSONObject json = new JSONObject();
        User user = userDAO.getUser(userId);
        if(user==null)
        {
            json.accumulate("success", false);
            json.accumulate("err", "Invalid User");
        }
        else
        {
            if(path.equals(""))
            {
                List<String> immediateChilds = fileDao.getDrives(pcId);
                HashMap<String,PushedFileRecord> map = new HashMap<>();
                int fileNum =1;
                for(String child : immediateChilds)
                {
                    PushedFileRecord file = new PushedFileRecord();
                    file.setFileName(child);
                    file.setIsDirty(true);
                    map.put(fileNum+"", file);
                    fileNum++;
                }
                json.accumulate("success", true);
                json.accumulate("files",map);
            }
            else
            {
                List<String> immediateChilds = fileDao.getImmediateChilds(path+"%",num,pcId);
                HashMap<String,PushedFileRecord> map = new HashMap<>();
                int fileNum =1;
                for(String child : immediateChilds)
                {
                    PushedFileRecord file = new PushedFileRecord();
                    file.setFileName(child);
                    String nextPath = path+"_"+child;

        //            List<String> furtherChilds = fileDao.getImmediateChilds(nextPath+"%",num+1,((User)session.getAttribute("user")).getId());
                    List<String> furtherChilds = fileDao.getImmediateChilds(nextPath+"%",num+1,pcId);
                    if(furtherChilds==null)
                    {
                        file.setIsDirty(false);
                    }
                    else
                    {
    //                    if(furtherChilds.size()>1)
    //                    {
    //                        file.setIsDirty(true);
    //                    }
    //                    else if(furtherChilds.size()==1)
    //                    {
    //                        if(furtherChilds.get(0).equals(file.getFileName()))
    //                        {
    //                            file.setIsDirty(false);
    //                        }
    //                        else
    //                        {
    //                            file.setIsDirty(true);
    //                        }
    //                    }
                        if(furtherChilds.size()>0)
                        {
                            file.setIsDirty(true);
                        }
                        else 
                        {

                                file.setIsDirty(false);


                        }
                    }
                    String fpath = path.replace("%", "\\");
                    file.setFilePath(fpath);
                    map.put(fileNum+"", file);
                    fileNum++;
                }

        //        JSONArray jsonArray = new JSONArray();
        //        jsonArray.addAll(list);
                json.accumulate("success", true);
                json.accumulate("files",map);
        //        return json;

                }
        }
        
        
        return json.toString();
//        List<String> immediateChilds = fileDao.getImmediateChilds(path+"%",num,((User)session.getAttribute("user")).getId());
    }
    @RequestMapping(value = "/andDownload",method = RequestMethod.GET)
    public void doDownload(@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        path = decode(path);
        User user = userDAO.getUser(userId);
        if(user==null)
        {
            response.sendError(404);
        }
        else
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            String fullPath = null;
            if(path.contains(".zip"))
            {
                fullPath = path;
            }
            else
            {
                fullPath = LOCATION+"\\"+user.getUname()+"\\"+pc.getPcName()+"\\"+path;
            }
            File downloadFile = new File(fullPath);
            sendDownload(downloadFile, fullPath, request, response);
            if(path.contains(".zip"))
            {
                downloadFile.delete();
            }
        }
    }
    @RequestMapping(value = "/andDelete",method = RequestMethod.POST)
    public @ResponseBody String deleteSingleFile(@RequestParam("path") String path,@RequestParam("pc_id") int pcId,@RequestParam("userId") int userId,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        path = decode(path);
        User user = userDAO.getUser(userId);
        System.out.println("File Delete Request: "+path);
        JSONObject responseJson = new JSONObject();
        if(user!=null)
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            if(pc!=null)
            {
                try
                {
                    File f = new File(LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+path);
                    if(f.exists()) {
                        deleteAllFiles(f, pcId);
                        responseJson.accumulate("success", true);
                    }
                    else
                    {
                        responseJson.accumulate("err","file not found");
                        responseJson.accumulate("success", false);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    responseJson.accumulate("success", false);
                    responseJson.accumulate("err", "Exception: "+ex.getMessage());
                }
            }
            else
            {
                responseJson.accumulate("pcFound", false);
                responseJson.accumulate("success", false);
                return responseJson.toString();
            }
        }
        else
        {
            responseJson.accumulate("loggedIn", false);
            responseJson.accumulate("success", false);
            return responseJson.toString();
        }
        System.out.println("DELETING FILE STATUS: "+responseJson);
        if(responseJson.getBoolean("success"))
            System.out.println("File Deleted: "+path);
        return responseJson.toString();
    }
    void setFileRecordToDeleted(PushedFileRecord fileRecord,int pcId){
        if(fileRecord.getDirtyReason() != null)
        {
            if(fileRecord.getDirtyReason().equals("new file")){
                fileDao.delete(fileRecord.getFileId(), pcId);
                return; 
            }
        }
        fileRecord.setIsDirty(true);
        fileRecord.setDirtyReason("file deleted");
        fileRecord.setDateModified(System.currentTimeMillis());
        fileDao.update(fileRecord.getFileId(), pcId, fileRecord);
    }
    private void deleteAllFiles(File f,int pcId)
    {
        File [] files = f.listFiles();
        if(files!=null){
            for(File file : files)
            {
                if(file.isFile()){
                    String path = file.getAbsolutePath().replace(":", "");
                    int count=0;
                    int i=0;
                    for(i=0;i<path.length();i++){
                        if(path.charAt(i)=='\\')
                            count++;
                        if(count==4)
                            break;
                    }
                    path = path.substring(i+1);
                    System.out.println("Delete File Path : "+path);
                    PushedFileRecord fileRecord = fileDao.getPushedFileRecord(path, pcId);
                    if(fileRecord != null){
                        setFileRecordToDeleted(fileRecord, pcId);
                    }
                    file.delete();
                }
                else if(file.isDirectory()){
                    deleteAllFiles(file,pcId);
                }
            }
        }
        String path = f.getAbsolutePath().replace(":", "");
        int count=0;
        int i=0;
        for(i=0;i<path.length();i++){
            if(path.charAt(i)=='\\')
                count++;
            if(count==4)
                break;
        }
        path = path.substring(i+1);
        System.out.println("Delete File Path : "+path);
        PushedFileRecord fileRecord = fileDao.getPushedFileRecord(path, pcId);
        if(fileRecord != null){
            setFileRecordToDeleted(fileRecord, pcId);
        }
        else{
            PushedFileRecord rec = new PushedFileRecord();
            rec.setFileName(f.getName());
            rec.setFilePath(path);
            rec.setIsDirty(true);
            rec.setDirtyReason("file deleted");
            rec.setDateModified(System.currentTimeMillis());
            rec.setPcId(pcId);
            fileDao.create(rec, 2);

        }
        f.delete();
    }
    @RequestMapping(value = "/andUploadFile",method = RequestMethod.POST)
    public @ResponseBody String uploadWebFiles(FileBucket fileBucket,@RequestParam("pc_id") int pcId,@RequestParam("user_id") int userId, HttpServletRequest request,HttpSession session,HttpServletResponse response) throws IOException
    {
        User user = userDAO.getUser(userId);
        HashMap<String,String> map = new HashMap<>();
        MultipartFile multipartFile = fileBucket.getFile();
        if(user!=null)
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            String uname = user.getUname();
            PushedFileRecord pushedFileRecord = new PushedFileRecord();
            pushedFileRecord.setPcId(pc.getPcId());
            File file = new File(DataController.LOCATION+uname+"\\"+pc.getPcName());
            if(!file.exists())
            {
                file.mkdirs();
            }
            String path =fileBucket.getFilePath();
            path = decode(path);
            file = new File(DataController.LOCATION+uname+"\\"+pc.getPcName()+"\\"+path);
            createParentDirs(file.getParentFile().getAbsolutePath());
            // Now do something with file...
            File toUpload = new File( DataController.LOCATION +uname+"\\"+pc.getPcName()+"\\"+ path);
            FileCopyUtils.copy(fileBucket.getFile().getBytes(), toUpload);
            DataController.encrypt(toUpload, userDAO.getUser(pc.getUserId()).getUname(), userDAO.getUser(pc.getUserId()).getPass());
            String fileName = multipartFile.getOriginalFilename();
            pushedFileRecord.setFileName(fileName);
            pushedFileRecord.setFilePath(path);
            pushedFileRecord.setDateModified(toUpload.lastModified());
            PushedFileRecord fetchedRecord = fileDao.getPushedFileRecord(pushedFileRecord.getFilePath(), pushedFileRecord.getPcId());
            pushedFileRecord.setIsDirty(true);
            if(fetchedRecord==null)
            {
                fileDao.create(pushedFileRecord,DirtyReason.NEW_FILE_CREATED);
            }
            else
            {
                pushedFileRecord.setDirtyReason("new file");
                fileDao.update(fetchedRecord.getFileId(), pushedFileRecord.getPcId(), pushedFileRecord);
            }

            map.put("success", "Upload Successfully");
        }
        else
        {
            map.put("error", "User Not Found");
        }
        JSONObject json = new JSONObject();
        json.accumulateAll(map);
        return json.toString();
    }
}
