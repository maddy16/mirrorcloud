/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static mirror.web.DataController.LOCATION;
import static mirror.web.DataController.sendDownload;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
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
public class AppController {
        
        @Autowired
        private UserDAO userDAO;
        @Autowired
        private PushedFileDao pushedFileDAO;
        @Autowired
        private PcDAO pcDAO;
    @Autowired
    FileValidator fileValidator;

    @InitBinder("fileBucket")
    protected void initBinderFileBucket(WebDataBinder binder) {
        binder.setValidator(fileValidator);
    }
    @RequestMapping(value = "/downloadDirtyFile",method = RequestMethod.POST)
    public void doDownload(@RequestParam("file_id") long fileId,@RequestParam("user_id") int userId,@RequestParam("pc_name") String pcName,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        UserPC pc = pcDAO.getPc(userId, pcName);
        User user = userDAO.getUser(userId);
        if(user!=null)
        {
            if(pc!=null)
            {
                int pcId=pc.getPcId();
                PushedFileRecord dirtyFile = pushedFileDAO.getPushedFileRecord(fileId, pcId);
                if(dirtyFile!=null)
                {
                    String path = DataController.LOCATION+"\\"+user.getUname()+"\\"+pc.getPcName()+"\\"+dirtyFile.getFilePath();
                    File downloadFile = new File(path);
                    DataController.sendDownload(downloadFile, path, request, response);
                }
            }
        }
    }
    @RequestMapping(value = "/cleanFile",method = RequestMethod.POST)
    public @ResponseBody String setFileToClean(@RequestBody String json){
        JSONObject jSONObject = JSONObject.fromObject(json);
        JSONObject responseJson = new JSONObject();
        PushedFileRecord record = (PushedFileRecord) JSONObject.toBean(jSONObject,PushedFileRecord.class);
        pushedFileDAO.update(record.getFileId(), record.getPcId(), record);
        responseJson.accumulate("correct", true);
        return responseJson.toString();
        
    }
    @RequestMapping(value = "/deleteFileRecord",method = RequestMethod.POST)
    public @ResponseBody String deleteFileRecord(@RequestBody String json){
        JSONObject jSONObject = JSONObject.fromObject(json);
        JSONObject responseJson = new JSONObject();
        PushedFileRecord record = (PushedFileRecord) JSONObject.toBean(jSONObject,PushedFileRecord.class);
        pushedFileDAO.delete(record.getFileId(), record.getPcId());
        responseJson.accumulate("correct", true);
        return responseJson.toString();
    }
    @RequestMapping(value = "/deleteFile",method = RequestMethod.POST)
    public @ResponseBody String deleteFile(@RequestBody String json){
        JSONObject jSONObject = JSONObject.fromObject(json);
        JSONObject responseJson = new JSONObject();
        String filePath = jSONObject.getString("file_path");
        int userId = jSONObject.getInt("user_id");
        String pcName = jSONObject.getString("pc_name");
        User user = userDAO.getUser(userId);
        try
        {
            if(user!=null)
            {
                UserPC pc = pcDAO.getPc(userId, pcName);
                if(pc!=null)
                {
                    int pcId = pc.getPcId();
                    File file = new File(DataController.LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+filePath);
                    boolean fileExists = file.exists();
                    boolean recordExists = false;
                    PushedFileRecord fileRecord = pushedFileDAO.getPushedFileRecord(filePath, pcId);
                    if(fileRecord!=null){
                        recordExists=true;
                    }
                    if(fileExists && recordExists){
                        if(file.delete())
                            pushedFileDAO.delete(fileRecord.getFileId(), pcId);
                    } else if(!fileExists && recordExists){
                        pushedFileDAO.delete(fileRecord.getFileId(), pcId);
                    } else if(fileExists && !recordExists){
                        file.delete();
                    }
                    responseJson.accumulate("correct", true);   
                } else{
                    responseJson.accumulate("correct", false);
                    responseJson.accumulate("Error", "Invalid PC");
                }
            } else{
                responseJson.accumulate("correct", false);
                responseJson.accumulate("Error", "Invalid User");
            }
        } catch(Exception ex)
        {
            responseJson.accumulate("correct", false);
            responseJson.accumulate("Error", "Exception: "+ex.getMessage());
            ex.printStackTrace();
        }
        return responseJson.toString();
    }
    @RequestMapping(value="/getAllDirty",method = RequestMethod.POST)
    public @ResponseBody String sendAllDirtyFiles(@RequestParam("pc_name") String pcName,@RequestParam("user_id") int userId)
    {
        UserPC pc = pcDAO.getPc(userId, pcName);
        User user = userDAO.getUser(userId);
        JSONObject responseJson = new JSONObject();
        if(user!=null)
        {
            responseJson.accumulate("userFound", true);
            if(pc!=null)
            {
                int pcId=pc.getPcId();
                responseJson.accumulate("pcFound", true);
                List<PushedFileRecord> allDirtyFiles = pushedFileDAO.getAllDirtyFiles(pcId);
                if(allDirtyFiles.size()==0 || allDirtyFiles==null)
                {
                    responseJson.accumulate("dirtyFilesFound", false);
                }
                else
                {
                    System.out.println("Sending Dirty Files...");
                    responseJson.accumulate("dirtyFilesFound", true);
                    responseJson.accumulate("dirtyFiles", allDirtyFiles);
                }
            }
            else
            {
                responseJson.accumulate("pcFound", false);
            }
        }
        else
        {
            responseJson.accumulate("userFound", false);
        }
        
        return responseJson.toString();
    }
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody String singleFileUpload(FileBucket fileBucket,
            BindingResult result, ModelMap model) throws IOException {
        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "Validation Errors";
        } else {
            MultipartFile multipartFile = fileBucket.getFile();
            int uid = fileBucket.getUserId();
            User user = userDAO.getUser(uid);
            String mac = fileBucket.getMac();
            
            if(user!=null)
            {
                UserPC pc = pcDAO.getPcUsingMac(uid, mac);
                PushedFileRecord pushedFileRecord = new PushedFileRecord();
                pushedFileRecord.setPcId(pc.getPcId());
                File file = new File(DataController.LOCATION+user.getUname()+"\\"+pc.getPcName());
                if(!file.exists())
                {
                    file.mkdirs();
                }
                String path =fileBucket.getFilePath();
                String newPath =path.substring(0,1)+path.substring(2, path.length());
                file = new File(DataController.LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+newPath);
                DataController.createParentDirs(file.getParentFile().getAbsolutePath());
                // Now do something with file...
                File toUpload = new File( DataController.LOCATION +user.getUname()+"\\"+pc.getPcName()+"\\"+newPath);
                FileCopyUtils.copy(fileBucket.getFile().getBytes(), toUpload);
                toUpload = new File( DataController.LOCATION +user.getUname()+"\\"+pc.getPcName()+"\\"+newPath);
                String fileName = multipartFile.getOriginalFilename();
                pushedFileRecord.setFileName(fileName);
                pushedFileRecord.setFilePath(newPath);
                Long dateModified = new Long(fileBucket.getLastModified());
                pushedFileRecord.setDateModified(dateModified);
                PushedFileRecord fetchedRecord = pushedFileDAO.getPushedFileRecord(pushedFileRecord.getFilePath(), pushedFileRecord.getPcId());
                if(fetchedRecord==null)
                {
                    pushedFileDAO.create(pushedFileRecord,DirtyReason.NULL);
                }
                else
                {
                    pushedFileDAO.update(fetchedRecord.getFileId(), pushedFileRecord.getPcId(), pushedFileRecord);
                }
                
                return "success";
            }
            else
            {
                return "invalid user";
            }
            
        }
    }
    @RequestMapping(value = "/setDrives",method = RequestMethod.POST)
    public @ResponseBody String setDrives(@RequestBody String json)
    {
        JSONObject jSONObject = JSONObject.fromObject(json);
        String info = jSONObject.getString("drives");
        int user_id = jSONObject.getInt("user_id");
        String pcName = jSONObject.getString("pc_name");
        User user = userDAO.getUser(user_id);
        JSONObject responseJson = new JSONObject();
        String[] drives = info.split(" ");
        if(user!=null)
        {
            UserPC pc = pcDAO.getPc(user_id, pcName);
            if(pc==null)
            {
                responseJson.accumulate("correct", false);
                responseJson.accumulate("error", "No PC Registered");
            }
            else
            {
                responseJson.accumulate("correct", true);
                List<String> pushedDrives = pushedFileDAO.getDrives(pc.getPcId());
                if(pushedDrives==null)
                {
                    for(String drive : drives)
                    {
                        PushedFileRecord fileRecord = new PushedFileRecord();
                        fileRecord.setDateModified(new Long("0"));
                        fileRecord.setFileName(drive);
                        fileRecord.setFilePath("drive");
                        fileRecord.setPcId(pc.getPcId());
                        pushedFileDAO.create(fileRecord, DirtyReason.NULL);
                    }
                    responseJson.accumulate("status", "Drives Registered");
                }
                else
                {
                    if(pushedDrives.size()==drives.length)
                    {
                        responseJson.accumulate("status", "Drives already present");
                    }
                    else
                    {
                        pushedFileDAO.deleteDrives(pc.getPcId());
                        for(String drive : drives)
                        {
                            PushedFileRecord fileRecord = new PushedFileRecord();
                            fileRecord.setDateModified(new Long("0"));
                            fileRecord.setFileName(drive);
                            fileRecord.setFilePath("drive");
                            fileRecord.setPcId(pc.getPcId());
                            pushedFileDAO.create(fileRecord, DirtyReason.NULL);
                        }
                        responseJson.accumulate("status", "Drives Registered");
                    }
                }
            }
        }
        else
        {
            responseJson.accumulate("correct", false);
            responseJson.accumulate("error", "Invalid User");
        }
        return responseJson.toString();
    }
    @RequestMapping(value = "/appLogin", method = RequestMethod.POST)
    public @ResponseBody String applogin(User user)
    {
        try
        {
            String uname = user.getUname();
            String pass = user.getPass();
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
        }
        JSONObject json = new JSONObject();
        json.accumulate("correct", false);
        json.accumulate("errormsg", "Invalid User");
        return json.toString();
        
    }
    @RequestMapping(value = "/checkName", method = RequestMethod.POST)
    public @ResponseBody String checkPcName(@RequestBody String details)
    {
        JSONObject jsonDetail = JSONObject.fromObject(details);
        int userId = jsonDetail.getInt("user_id");
        User user = userDAO.getUser(userId);
        JSONObject responseJson = new JSONObject();        
        if(user!=null)
        {
            String pcName = jsonDetail.getString("pc_name");
            UserPC pc = pcDAO.getPc(userId, pcName);
            if(pc==null)
            {
                responseJson.accumulate("correct", true);
            }
            else
            {
                responseJson.accumulate("correct", false);
                responseJson.accumulate("error", "Not Available");
            }
        }
        else
        {
            responseJson.accumulate("correct", false);
            responseJson.accumulate("error", "Invalid User");
        }
        return responseJson.toString();
    }
    @RequestMapping(value = "/registerPc", method = RequestMethod.POST)
    public @ResponseBody String registerPc(@RequestBody String details)
    {
        JSONObject jsonDetail = JSONObject.fromObject(details);
        System.out.println(jsonDetail);
        
        UserPC pc = (UserPC) JSONObject.toBean(jsonDetail, UserPC.class);
        User user = userDAO.getUser(pc.getUserId());
        JSONObject responseJson = new JSONObject();        
        if(user!=null)
        {
            responseJson.accumulate("correct", true);
            
            UserPC byMac = pcDAO.getPcUsingMac(pc.getUserId(), pc.getDetail().getMacAddress());
            UserPC byName = pcDAO.getPc(pc.getUserId(), pc.getPcName());
            if(byName==null)
            {
                System.out.println("No PC By Name: "+pc.getPcName());
                pcDAO.create(pc);
                responseJson.accumulate("status", "PC registered");
            }
            else
            {
                if(byMac==null)
                {
                    System.out.println("No PC By MAC: "+pc.getDetail().getMacAddress());
                    pcDAO.registerAnotherMac(byName.getPcId(), pc.getDetail());
                    pcDAO.updateAddress(byName.getPcId(), pc.getAddress());
                    responseJson.accumulate("status", "PC registered");
                }
                else if(byName.getPcId() == byMac.getPcId())
                {
                    pcDAO.updateAddress(byName.getPcId(), pc.getAddress());
                    responseJson.accumulate("status", "MAC confirmed");
                }
            }
        }
        else
        {
            responseJson.accumulate("correct", false);
            responseJson.accumulate("error", "User Not Found");
        }
        return responseJson.toString();
    }
    
    @RequestMapping(value = "/pingClient", method = RequestMethod.POST)
    public @ResponseBody String pingClient(@RequestParam("pc_id") int pcId){
        JSONObject response = new JSONObject();
        UserPC pc = pcDAO.getPcUsingPcId(pcId);
//        System.out.println("Ping Requested for PC "+pcId);
        Socket s = new Socket();
        if(pc!=null){
            try {
                System.out.print("IP : "+pc.getAddress()+". ");
                s.connect(new InetSocketAddress(pc.getAddress(), 5713),2000);
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String resp = br.readLine();
                System.out.println("PING RESPONSE for PC "+pcId+": "+resp);
                if(resp!=null){
                    if(resp.equalsIgnoreCase("connected"))
                        response.accumulate("connected", true);
                    else
                        response.accumulate("connected", false);
                }else{
                    response.accumulate("connected", false);
                }
                br.close();
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                response.accumulate("connected", false);
            }
        }
        else{
            response.accumulate("connected", false);   
        }
            return response.toString();
    }
    
}
