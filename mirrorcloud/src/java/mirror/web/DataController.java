/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;


import java.io.File;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class DataController {
    public static String LOCATION="D:\\mirrorWebUploads\\";
    @Autowired
    PushedFileDao fileDao;
    @Autowired
    PcDAO pcDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    SharedLinkDAO sharedLinkDAO;
    @RequestMapping(value = "/public",method = RequestMethod.GET)
    public String getSharedData(@RequestParam("data") String data,HttpSession session,HttpServletResponse response,ModelMap model,HttpServletRequest request) throws IOException{
        SharedLink link = sharedLinkDAO.getLink(data);
        if(session.getAttribute("user")==null)
        {
            if(link!=null){
                if(link.getPaths().equalsIgnoreCase("no file"))
                    response.sendRedirect("/MirrorWeb"); // SHow Error No File To Show
                if(!link.getSharedTo().equals("All")){
                    response.sendRedirect("/MirrorWeb"); // SHow Error NOT ALLOWED WITHOUT LOGIN
                }
            }
            model.addAttribute("uname", "Guest");
            model.addAttribute("linkAction", "login");
            model.addAttribute("linkText", "Login");
            model.addAttribute("regShowClass", " ");
            model.addAttribute("command", new User());
        }
        else
        {
            User user = (User)session.getAttribute("user");
            if(link!=null){
                if(link.getPaths().equalsIgnoreCase("no file"))
                    response.sendRedirect("/MirrorWeb"); // SHow Error NOT ALLOWED WITHOUT LOGIN
                if(!link.getSharedTo().equals("All")){
                    boolean valid = false;
                    String sharedTo = link.getSharedTo();
                    String[] ids = sharedTo.split(" ");
                    for(String sid:ids){
                        int parseInt = Integer.parseInt(sid);
                        if(user.getId()==parseInt){
                            valid=true;
                            break;
                        }
                            
                    }
                    if(!valid)
                        response.sendRedirect("/MirrorWeb"); // SHow Error Invalid USER
                }
            }
            model.addAttribute("uname", user.getFullName());
            model.addAttribute("linkAction", "logout");
            model.addAttribute("linkText", "Logout");
            model.addAttribute("regShowClass", "hide");
            model.addAttribute("command", new User());
        }
        
        if(link==null){
            model.addAttribute("dataLink", "Invalid Link");
        }
        else{
            String path = link.getPaths();
            model.addAttribute("pc_id",link.getPcId());
            if(path.equals("full")){
                List<String> immediateChilds = fileDao.getDrives(link.getPcId());
                model.addAttribute("files",immediateChilds);
                model.addAttribute("paths", "");
                model.addAttribute("dataAccess",link.getAccess());
                model.addAttribute("parentPath", "");
                model.addAttribute("num",2);
            }
            else if(path.contains("<")){
                String[] splitted = path.split("<");
                List<String> shared = new ArrayList<>();
                for(int i=0;i<splitted.length;i++)
                {
                    String[] temp= splitted[i].replace("\\", "%").split("%");
                    shared.add(temp[temp.length-1]);
                }
                String p = path.split("<")[0].replace("\\", "%");
                System.out.println("Before: p = "+p);
                int num=0;
                int lastIndex = p.lastIndexOf("%");
                p = path.substring(0, lastIndex);
                p = p.replace("\\", "_");
                if(p.contains("%"))
                {    
                    num = p.split("%").length;
                    p = p.replace("%", "_");    
                    p = p.replace("\\", "_");
                }
                else{
                    num = 2;
                }
                
                System.out.println("p = "+p);
                System.out.println("num = "+num);
                model.addAttribute("paths", path);
                model.addAttribute("parentPath", p);
                model.addAttribute("files",shared);
                model.addAttribute("dataAccess",link.getAccess());
                model.addAttribute("num",num+1);
                //D\01 - Heartless - Mashooqana (PakHeaven.Com).mp3<D\2016-03-05.png
                
            }
            else {
                List<String> shared = new ArrayList<>();
                String[] temp= path.replace("\\", "%").split("%");
                shared.add(temp[temp.length-1]);
                String p = path.replace("\\", "%");
                System.out.println("Before: p = "+p);
                int num=p.split("%").length;
                if(p.contains("%"))
                {
                    int lastIndex = p.lastIndexOf("%");
                    p = path.substring(0, lastIndex);
                    p = p.replace("\\", "_");
                    
                }
                
                if(p.contains("%"))
                {    
                    
                    num = p.split("%").length;
                    p = p.replace("%", "_");    
                }
                else{
                    if(path.length()==1)
                    {
                        p="";
                        num=1;
                    }
                        
                }
                System.out.println("p = "+p);
                System.out.println("num = "+num);
                model.addAttribute("paths", path);
                model.addAttribute("parentPath", p);
                model.addAttribute("files",shared);
                model.addAttribute("dataAccess",link.getAccess());
                model.addAttribute("num",num+1);
                
            }
            model.addAttribute("link",link.getLinkText());
        }
        return "sharedData";
    }
    
    @RequestMapping(value = "/viewOwnData",method = RequestMethod.GET)
    public String getData(HttpSession session,HttpServletResponse response,ModelMap model,HttpServletRequest request)
    {
        System.out.println(request.getParameter("pcId"));
        try
        {
            
            User user = (User)session.getAttribute("user");
            if(user==null)
            {
                response.sendRedirect("/MirrorWeb");
            }
            else
            {
                model.addAttribute("user",user);
                List<UserPC> allPcs = pcDao.getAllPcs(user.getId());
                
                if(allPcs==null)
                {
                    model.addAttribute("files", null);
                }
                else
                {
                    if(allPcs.size()==0)
                    {
                        model.addAttribute("files", null);
                        
                    }
                    else
                    {
                        model.addAttribute("pcs",allPcs);
                        List<String> immediateChilds=null;
                        if(request.getParameter("pcId")!=null)
                        {
                            model.addAttribute("pc_id",request.getParameter("pcId"));
                            model.addAttribute("thisPc",pcDao.getPcUsingPcId(Integer.parseInt(request.getParameter("pcId"))));
                            immediateChilds = fileDao.getDrives(Integer.parseInt(request.getParameter("pcId")));
                        }
                        else
                        {
                            immediateChilds = fileDao.getDrives(allPcs.get(0).getPcId());
                            model.addAttribute("thisPc",allPcs.get(0));
                            model.addAttribute("pc_id",allPcs.get(0).getPcId());
                        }
        //                List<String> immediateChilds = fileDao.getDrives(4);
                        if(immediateChilds==null)
                        {
                            model.addAttribute("files", null);
                        }
                        else
                        {
                            model.addAttribute("files",immediateChilds);
                        }
                    }
                }
                
//                SystemDetail details= systemDao.getDetails(user.getId());
//                model.addAttribute("system", details);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            if(ex.getMessage().contains("Last packet sent to the server was 0 ms ago."))
                model.addAttribute("errorMsg","Database Server isn't running");
            else
                model.addAttribute("errorMsg",ex.getMessage());
            return "error";
        }
        return "userData";
    }
    public static String decode(String path)
    {
        if(path.contains("&amp;"))
            path = path.replaceAll("&amp;", "&");
        return path;
    }
    @RequestMapping(value = "/share",method = RequestMethod.POST)
    public @ResponseBody String generateLinkSingle(@RequestParam("path") String path,@RequestParam("shareTo") String shareTo,@RequestParam("pc_id") int pcId,@RequestParam("userId") int userId,@RequestParam("access") int access,HttpSession session,HttpServletResponse response){
        System.out.println("Generating Link for path: "+path);
        path = decode(path);
        JSONObject json = new JSONObject();
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            json.accumulate("success", false);
            json.accumulate("error", "Not Logged In");
        }
        else
        {
            userId = user.getId();
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            if(pc==null)
            {
                json.accumulate("success", false);
                json.accumulate("error", "Invalid PC");
            }
            else{
                System.out.println("Logged in and PC Found");
                String linkText = null;
                SharedLink sharedLink = new SharedLink();
                String toEncode = null;
                if(path.equals("")){
                    
                    toEncode="FullPcSharing"+new Date().toString();
                    sharedLink.setPaths("full");
                }
                else{
                    if(path.length()>20){
                        toEncode = path.substring(0,20);
                        toEncode+=new Date().toString();
                    }
                    else
                        toEncode=path+new Date().toString();
                    sharedLink.setPaths(path);
                }
                byte[] base64Encoded = Base64.getEncoder().encode(toEncode.getBytes());
                linkText = new String(base64Encoded);
                sharedLink.setLinkText(linkText);
                sharedLink.setPcId(pcId);
                sharedLink.setAccess(access);
                sharedLink.setSharedTo(shareTo);
                sharedLinkDAO.create(sharedLink);
                json.accumulate("success", true);
                json.accumulate("link", linkText);
                if(!shareTo.equals("All")){
                    String [] ids = shareTo.split(" ");
                    ArrayList<User> usersToShare = new ArrayList<>();
                    for(String toSendId : ids){
                        User toSendUser = userDao.getUser(Integer.parseInt(toSendId));
                        usersToShare.add(toSendUser);
                    }
                    EmailThread emailThread = new EmailThread(linkText, usersToShare, user);
                    emailThread.start();
                }
                System.out.println("base64: "+linkText);
                
            }
        }
        return json.toString();
    }
    @RequestMapping(value = "/viewSharedDataAjax",method = RequestMethod.POST)
    public @ResponseBody String getSharedImmediateData(@RequestParam("path") String path,@RequestParam("num") int num,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response) throws IOException{
        path = decode(path);
        JSONObject json = new JSONObject();
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
            json.accumulateAll(map);
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
            json.accumulateAll(map);
    //        return json;
            
            }
        return json.toString();
    }
    @RequestMapping(value = "/viewDataAjax",method = RequestMethod.POST)
    public @ResponseBody String getImmediateData(@RequestParam("path") String path,@RequestParam("num") int num,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response) throws IOException
    {
        path = decode(path);
        JSONObject json = new JSONObject();
        int userId = 0;
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            json.accumulate("loggedIn", false);
            return json.toString();
        }
        else
        {
            json.accumulate("loggedIn", true);
            userId = user.getId();
        }
        
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
            json.accumulateAll(map);
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
            json.accumulateAll(map);
    //        return json;
            
            }
        return json.toString();
//        List<String> immediateChilds = fileDao.getImmediateChilds(path+"%",num,((User)session.getAttribute("user")).getId());
    }
    @RequestMapping(value = "/renameData",method = RequestMethod.POST)
    public @ResponseBody String renameData(@RequestParam("newName") String newName,@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        
        path = decode(path);
        System.out.println("Rename Requested: "+path);
        User user = (User)session.getAttribute("user");
        UserPC pc = pcDao.getPcUsingPcId(pcId);
        JSONObject responseJson = new JSONObject();
        if(user!=null)
        {
            responseJson.accumulate("loggedIn", true);
            if(pc!=null)
            {
                responseJson.accumulate("pcFound", true);
                PushedFileRecord pushedFileRecord = fileDao.getPushedFileRecord(path, pcId);
                if(pushedFileRecord==null)
                {
                    //Requested file is directory
                    responseJson.accumulate("success", false);
                    responseJson.accumulate("errorMsg", "Directories can't be renamed");
                }
                else
                {
                    
                    File file = new File(LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+path);
                    String parentPath = file.getParent();
                    String extension = file.getName().substring(file.getName().lastIndexOf("."));
                    String newFileName = newName+extension;
                    String actualParent = path.substring(0,path.lastIndexOf("\\"));
                    if(file.exists()){
                        File newFile = new File(parentPath+"\\"+newFileName);
                        file.renameTo(newFile);
                        setFileRecordToDeleted(pushedFileRecord, pcId);
                        pushedFileRecord = new PushedFileRecord();
                        pushedFileRecord.setPcId(pc.getPcId());
                        pushedFileRecord.setFileName(newFileName);
                        pushedFileRecord.setFilePath(actualParent+"\\"+newFileName);
                        pushedFileRecord.setDateModified(newFile.lastModified());
                        PushedFileRecord fetchedRecord = fileDao.getPushedFileRecord(pushedFileRecord.getFilePath(), pc.getPcId());
                        pushedFileRecord.setIsDirty(true);
                        if(fetchedRecord==null)
                        {
                            fileDao.create(pushedFileRecord,DirtyReason.NEW_FILE_CREATED);
                        }
                        else
                        {
                            pushedFileRecord.setDirtyReason("new file");
                            fileDao.update(fetchedRecord.getFileId(), pcId, pushedFileRecord);
                        }
                        responseJson.accumulate("success", true);
                    }
                }
            }
            else
            {
                responseJson.accumulate("pcFound", false);
                responseJson.accumulate("success", false);
            }
        }
        else
        {
            responseJson.accumulate("loggedIn", false);
            responseJson.accumulate("success", false);
        }
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
    private void deleteAllSharedFiles(File f,int pcId)
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
    private void deleteAllFilesInPc(File f)
    {
        File [] files = f.listFiles();
        for(File file : files)
        {
            
            if(!file.delete())
            {
                if(file.isDirectory())
                {
                    deleteAllFilesInPc(file);
                }
            }
        }
        f.delete();
    }
    @RequestMapping(value = "/deleteSingle",method = RequestMethod.POST)
    public @ResponseBody String deleteSingleFile(@RequestParam("path") String path,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        path = decode(path);
        User user = (User)session.getAttribute("user");
        System.out.println("File Delete Request: "+path);
        JSONObject responseJson = new JSONObject();
        if(user!=null)
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            responseJson.accumulate("loggedIn", true);
            if(pc!=null)
            {
                responseJson.accumulate("pcFound", true);
                try
                {
                    File f = new File(LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+path);
                    if(f.exists()) {
                        deleteAllFiles(f, pcId);
                        responseJson.accumulate("success", true);
                    }
                    else
                    {
                        responseJson.accumulate("success", false);
                        return responseJson.toString();
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    responseJson.accumulate("success", false);
                    responseJson.accumulate("error", "Exception: "+ex.getMessage());
                }
                
            }
            else
            {
                responseJson.accumulate("pcFound", false);
                return responseJson.toString();
            }
        }
        else
        {
            responseJson.accumulate("loggedIn", false);
            return responseJson.toString();
        }
        System.out.println("DELETING FILE STATUS: "+responseJson);
        if(responseJson.getBoolean("success"))
            System.out.println("File Deleted: "+path);
        return responseJson.toString();
    }
    
    @RequestMapping(value = "/deleteSingleShared",method = RequestMethod.POST)
    public @ResponseBody String deleteSingleSharedFile(@RequestParam("path") String path,@RequestParam("pc_id") int pcId,@RequestParam("link") String link,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        path = decode(path);
//        User user = (User)session.getAttribute("user");
        System.out.println("Shared File Delete Request: "+path);
        JSONObject responseJson = new JSONObject();
        UserPC pc = pcDao.getPcUsingPcId(pcId);
        
        if(pc!=null)
        {
            responseJson.accumulate("pcFound", true);
            User user = userDao.getUser(pc.getUserId());
            if(user!=null)
            {
                responseJson.accumulate("loggedIn", true);
                try
                {
                    File f = new File(LOCATION+user.getUname()+"\\"+pc.getPcName()+"\\"+path);
                    if(f.exists()) {
                        deleteAllSharedFiles(f, pcId);
                        SharedLink sharedLink = sharedLinkDAO.getLink(link);
                        String allPaths = sharedLink.getPaths();
                        String[] splittedPaths = allPaths.split("<");
                        String newPaths = "";
                        
                        for(int i = 0 ;i<splittedPaths.length;i++){
                            String s = splittedPaths[i];
                            if(s.equals(path)){
                                continue;
                            }
                            else
                            {
                                newPaths+=s;
                                if(i!=splittedPaths.length-1)
                                    newPaths+="<";
                            }
                        }
                        if(newPaths.length()!=0)
                            if(newPaths.charAt(newPaths.length()-1)=='<')
                                newPaths= newPaths.substring(0,newPaths.length()-1);
                        else
                                newPaths="No File";
                        sharedLink.setPaths(newPaths);
                        sharedLinkDAO.update(sharedLink.getLinkId(), sharedLink);
                        responseJson.accumulate("success", true);
                    }
                    else
                    {
                        responseJson.accumulate("success", false);
                        return responseJson.toString();
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    responseJson.accumulate("success", false);
                    responseJson.accumulate("error", "Exception: "+ex.getMessage());
                }
                
            }
            else
            {
                responseJson.accumulate("loggedIn", false);
                return responseJson.toString();
            }
        }
        else
        {
            responseJson.accumulate("pcFound", false);
            return responseJson.toString();
        }
        System.out.println("DELETING FILE STATUS: "+responseJson);
        if(responseJson.getBoolean("success"))
            System.out.println("File Deleted: "+path);
        return responseJson.toString();
    }
    public static String escapeChars(String fileName)
    {
        return fileName.contains("'")?fileName.replace("'", "?"):fileName;
    }
    public static String reverseEscape(String fileName)
    {
        return fileName.contains("?")?fileName.replace("?", "'"):fileName;
    }
    @RequestMapping(value = "/downloadShared",method = RequestMethod.POST)
    public void doDownloadShared(@RequestParam("sharedLink") String link,@RequestParam("path") String path,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException, Exception{
        path = decode(path);
        UserPC pc = pcDao.getPcUsingPcId(pcId);
        int userId = pc.getUserId();
        User owner = userDao.getUser(userId);
        String fullPath = null;
        if(path.contains(".zip"))
        {
            fullPath = path;
            File downloadFile = new File(fullPath);
            sendDownload(downloadFile, fullPath, request, response);
            downloadFile.delete();
        }
        else
        {
            fullPath = LOCATION+"\\"+owner.getUname()+"\\"+pc.getPcName()+"\\"+path;
            SharedLink sharedLink = sharedLinkDAO.getLink(link);
            File downloadFile = new File(fullPath);
            String parentPath = path.substring(0, path.lastIndexOf("\\"));
            String copyPath = LOCATION+"\\"+owner.getUname()+"\\"+pc.getPcName()+"\\"+parentPath+"\\(MirrorCloud)"+downloadFile.getName();
            if(sharedLink.getSharedTo().equals("All")){
                File newFile = new File(copyPath);
                boolean copied = copyFile(downloadFile, newFile);
                if(copied){
                    decrypt(newFile, owner.getUname(), owner.getPass());
                    sendDownload(newFile, fullPath, request, response);
                    newFile.delete();
                }
                else{
                    //Send Error downloading File
                    System.out.println("File Not Copied");
                }
            }   
            else{
                User user = (User)session.getAttribute("user");
                if(user==null){
                    response.sendRedirect("/MirrorWeb");
                }
                else{
                    File newFile = new File(copyPath);
                    boolean copied = copyFile(downloadFile, newFile);
                    if(copied){
                        decrypt(newFile, owner.getUname(), owner.getPass());
                        encrypt(newFile, user.getUname(), user.getPass());
                        sendDownload(newFile, fullPath, request, response);
                        newFile.delete();
                    }
                    else{
                        //Send Error downloading File
                        System.out.println("File Not Copied");
                    }
                }
            }
        }
    }
    @RequestMapping(value = "/download",method = RequestMethod.POST)
    public void doDownload(@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletResponse response,HttpServletRequest request) throws IOException
    {
        path = decode(path);
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            response.sendRedirect("/MirrorWeb");
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
    @RequestMapping(value = "/deletePc",method = RequestMethod.GET)
    public void deletePc(@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpServletResponse response) throws IOException{
        User owner = userDao.getUser(userId);
        if(owner!=null)
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            if(pc!=null)
            {
                String dir = LOCATION+owner.getUname()+"\\"+pc.getPcName();
                File directory = new File(dir);
                deleteAllFilesInPc(directory);
                pcDao.delete(pcId);
                response.sendRedirect("/MirrorWeb/viewOwnData");
            }
        }
    }
    @RequestMapping(value = "/downloadMultiple",method = RequestMethod.POST)
    public @ResponseBody String downloadMultiFiles(@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
    {
        path = decode(path);
        String resText = "";
        User owner = userDao.getUser(userId);
        if(owner!=null)
        {
            System.out.println("Path: "+path);
            String parentDir = "";
            if(!path.contains(" ## "))
            {
                parentDir = path.substring(0,path.lastIndexOf("\\"));
            } else {
                String aFile = path.split(" ## ")[0];
                System.out.println("First File: "+aFile);
                parentDir = aFile.substring(0,aFile.lastIndexOf("\\"));
            }
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            String SOURCE_FOLDER = LOCATION+owner.getUname()+"\\"+pc.getPcName()+"\\";
            System.out.println("SOURCE: "+SOURCE_FOLDER);
//            File file = new File(SOURCE_FOLDER);
            List<String> fileList= new ArrayList<>();
            String OUTPUT_ZIP_FILE = "MirrorCloud - "+System.currentTimeMillis()+".zip";
            System.out.println(OUTPUT_ZIP_FILE);
            
//            ########### GENERATECUSTOMFILELIST Code INCOMPLETE.......
            
            
//            ZipOutputStream zos = null;
//            FileOutputStream fos = null;
//            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
//            zos = new ZipOutputStream(fos);
//            generateFileListWithDecryption(new File(SOURCE_FOLDER),fileList,SOURCE_FOLDER,owner);
//         
//            zipIt(OUTPUT_ZIP_FILE,fileList,SOURCE_FOLDER,zos);
//            closeZipOutputEntry(zos);
//            encryptFiles(new File(SOURCE_FOLDER), owner);
//            encrypt(new File(OUTPUT_ZIP_FILE), owner.getUname(),owner.getPass());
//            resText="Success";
        }
        return resText;
    }
    public void generateCustomFileList(String sentPath,List<String> fileList,String SOURCE_FOLDER,User owner) throws Exception
    {
        String [] files = new String [1];
        if(sentPath.contains(" ## ")){
            files = sentPath.split(" ## ");
        }else{
            files[0] = sentPath;
        }
        for(String f : files){
            File node = new File(SOURCE_FOLDER+f);
            if (node.isFile())
            {
                  decrypt(node, owner.getUname(), owner.getPass());
                  fileList.add(generateZipEntry(node.toString(),SOURCE_FOLDER));

            }

            if (node.isDirectory())
            {
               String[] subNote = node.list();
               for (String filename : subNote)
               {
                  generateFileListWithDecryption(new File(node, filename),fileList,SOURCE_FOLDER,owner);
               }
            }
        }
    }
    @RequestMapping(value = "/downloadZippedDir",method = RequestMethod.POST)
    public @ResponseBody String singleFolderZipper(@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
    {
        path = decode(path);
        String resText = "";
        User owner = (User)session.getAttribute("user");
        if(owner==null)
        {
            response.sendRedirect("/MirrorWeb");
        }
        else
        {
            UserPC pc = pcDao.getPcUsingPcId(pcId);
            String SOURCE_FOLDER = LOCATION+owner.getUname()+"\\"+pc.getPcName()+"\\"+path;
            System.out.println("SOURCE: "+SOURCE_FOLDER);
            File file = new File(SOURCE_FOLDER);
            List<String> fileList= new ArrayList<>();
            String OUTPUT_ZIP_FILE = file.getName()+".zip";
            ZipOutputStream zos = null;
            FileOutputStream fos = null;
            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            zos = new ZipOutputStream(fos);
            generateFileListWithDecryption(new File(SOURCE_FOLDER),fileList,SOURCE_FOLDER,owner);
            zipIt(OUTPUT_ZIP_FILE,fileList,SOURCE_FOLDER,zos);
            closeZipOutputEntry(zos);
            encryptFiles(new File(SOURCE_FOLDER), owner);
            encrypt(new File(OUTPUT_ZIP_FILE), owner.getUname(),owner.getPass());
            resText="Success";
        }
        return resText;
    }
    @RequestMapping(value = "/downloadSharedZippedDir",method = RequestMethod.POST)
    public @ResponseBody String sharedFolderZipper(@RequestParam("sharedLink") String link,@RequestParam("path") String path,@RequestParam("userId") int userId,@RequestParam("pc_id") int pcId,HttpSession session,HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
    {
        path = decode(path);
        String resText = "";
        
        UserPC pc = pcDao.getPcUsingPcId(pcId);
        User owner = userDao.getUser(pc.getUserId());
        if(owner==null)
        {
            response.sendRedirect("/MirrorWeb");
        }
        else
        {
            SharedLink sharedLink = sharedLinkDAO.getLink(link);
            User user = (User)session.getAttribute("user");
            if(!sharedLink.getSharedTo().equals("All")){
                if(user==null){
                    response.sendRedirect("/MirrorWeb");//Not LOGGED IN SHOW ERROR
                }
                else {
                    boolean found = false;
                    String[] users = sharedLink.getSharedTo().split(" ");
                    for(String u : users){
                        if(Integer.parseInt(u)==user.getId()){
                            found=true;
                            break;
                        }
                    }
                    if(!found){
                        response.sendRedirect("/MirrorWeb");//Invalid USER IN SHOW ERROR
                    }
                }
            }
            
            String SOURCE_FOLDER = LOCATION+owner.getUname()+"\\"+pc.getPcName()+"\\"+path;
            System.out.println("SOURCE(Shared): "+SOURCE_FOLDER);
            File file = new File(SOURCE_FOLDER);
            List<String> fileList= new ArrayList<String>();
            String OUTPUT_ZIP_FILE = file.getName()+".zip";
            ZipOutputStream zos = null;
            FileOutputStream fos = null;
            fos = new FileOutputStream(OUTPUT_ZIP_FILE);
            zos = new ZipOutputStream(fos);
            generateFileListWithDecryption(new File(SOURCE_FOLDER),fileList,SOURCE_FOLDER,owner);
            zipIt(OUTPUT_ZIP_FILE,fileList,SOURCE_FOLDER,zos);
            closeZipOutputEntry(zos);
            encryptFiles(file, owner);
            if(!sharedLink.getSharedTo().equals("All")){
                encrypt(new File(OUTPUT_ZIP_FILE), user.getUname(), user.getPass());
            }
            resText="Success";
        }
        return resText;
    }
    
    
    public static void sendDownload(File downloadFile, String fullPath, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        ServletContext context = request.getServletContext();
        FileInputStream inputStream = new FileInputStream(downloadFile);

            // get MIME type of the file
            String mimeType = context.getMimeType(fullPath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }

            // set content attributes for the response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // get output stream of the response
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            // write bytes read from the input stream into the output stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
    }
    @RequestMapping(value = "/uploadWebFile",method = RequestMethod.POST)
    public @ResponseBody String uploadWebFiles(FileBucket fileBucket,@RequestParam("pc_id") int pcId,HttpServletRequest request,HttpSession session,HttpServletResponse response) throws IOException
    {
        int userId = 0;
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            response.sendRedirect("/MirrorWeb");
        }
        else
        {
            userId = user.getId();
            
        }
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
            encrypt(toUpload, userDao.getUser(pc.getUserId()).getUname(), userDao.getUser(pc.getUserId()).getPass());
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
    public static boolean dirExists(String path)
    {
        File file = new File(path);        
        return file.exists();
        
    }
    public static void createParentDirs(String path)
    {
        File f = new File(path);
        ArrayList<String> paths = new ArrayList<>();
        do {
        paths.add(f.getName());
        f = f.getParentFile();
    } while (f.getParentFile() != null);
        String newPath = f.getPath().substring(0,2);
        for(int i=paths.size()-1;i>=0;i--)
        {
            newPath+="\\"+paths.get(i);
            if(!dirExists(newPath))
                createDir(newPath);
        }
    }   
    public static boolean createDir(String path)
    {
        return new File(path).mkdir();
    }
    
    
    
    public static void zipIt(String zipFile,List<String> fileList,String SOURCE_FOLDER,ZipOutputStream zos)
        {
       byte[] buffer = new byte[1024];
       String source = "";
       try
       {
          try
          {
             source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
          }
         catch (Exception e)
         {
            source = SOURCE_FOLDER;
         }
         FileInputStream in = null;

         for (String file : fileList)
         {
             System.out.println("Zipping File : "+file);
            ZipEntry ze = new ZipEntry(source + File.separator + file);
            zos.putNextEntry(ze);
            try
            {
               in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
               int len;
               while ((len = in.read(buffer)) > 0)
               {
                  zos.write(buffer, 0, len);
               }
            }
            catch(FileNotFoundException ex)
            {
                System.out.println("File Not Found: " + file);
            }
            finally
            {
                if(in!=null)
                    in.close();
            }
         }
      }
      catch (IOException ex)
      {
         ex.printStackTrace();
      }
    }

    public static void closeZipOutputEntry(ZipOutputStream zos)
    {

        try
        {
            zos.closeEntry();
            zos.close();
        }
        catch (IOException e)
        {
           e.printStackTrace();
        }
    }
    public void encryptFiles(File node,User owner) throws Exception{
       if (node.isFile())
      {
            encrypt(node, owner.getUname(), owner.getPass());
      }

      if (node.isDirectory())
      {
         String[] subNote = node.list();
         for (String filename : subNote)
         {
            encryptFiles(new File(node, filename),owner);
         }
      } 
    }
    public void generateFileListWithDecryption(File node,List<String> fileList,String SOURCE_FOLDER,User owner) throws Exception
    {

      // add file only
      if (node.isFile())
      {
            decrypt(node, owner.getUname(), owner.getPass());
            fileList.add(generateZipEntry(node.toString(),SOURCE_FOLDER));

      }

      if (node.isDirectory())
      {
         String[] subNote = node.list();
         for (String filename : subNote)
         {
            generateFileListWithDecryption(new File(node, filename),fileList,SOURCE_FOLDER,owner);
         }
      }
    }

    private static String generateZipEntry(String file,String SOURCE_FOLDER)
    {
       return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }   
    public static void encrypt(File file,String uname,String pass)
    {
        System.out.println("Encrypting file : "+file.getAbsolutePath());
        File aesFile = new File("encFile");  
        try{
            
            FileInputStream fis;  
            FileOutputStream fos;  
            CipherInputStream cis;  
             //Creation of Secret key  
            String key = uname+pass;  
            int length=key.length();  
            if(length>16 && length!=16){  
                 key=key.substring(0, 16);  
            }  
            if(length<16 && length!=16){  
                 for(int i=0;i<16-length;i++){  
                      key=key+"0";  
                 }  
            }  
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"AES");  
             //Creation of Cipher objects  
            Cipher encrypt =Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");  
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);  
            // Open the Plaintext file  
            try {  
                 fis = new FileInputStream(file.getAbsolutePath());  
                 cis = new CipherInputStream(fis,encrypt);  
                 // Write to the Encrypted file  
                 fos = new FileOutputStream(aesFile);  
                 byte[] b = new byte[8];  
                 int i = cis.read(b);  
                 while (i != -1) {  

                      fos.write(b, 0, i);  
                      i = cis.read(b);  
                 }
                 encrypt.doFinal();
                 fos.flush();  
                 fos.close();  
                 cis.close();  
                 fis.close();
                 String filePath = file.getAbsolutePath();
                file.delete();
                File decryptedFile = new File("encFile");
                decryptedFile.renameTo(new File(filePath));
                aesFile.delete();
                System.out.println("File "+filePath+" Encrypted Successfully");
            } catch(IOException err) {  
                 System.out.println("Cannot open file!");
            }
       } catch(Exception e){  
            e.printStackTrace();  
       }
    }
    private void decrypt(File file,String uname,String pass ) 
    {
        try{
            File aesFile = new File(file.getAbsolutePath());  
            // if file doesnt exists, then create it  
            File aesFileBis = new File("decryptedFile");  
            FileInputStream fis;  
            FileOutputStream fos;  
            CipherInputStream cis;  
             //Creation of Secret key  
            String key = uname+pass;  
            int length=key.length();  
            if(length>16 && length!=16){  
                 key=key.substring(0, 16);  
            }  
            if(length<16 && length!=16){  
                 for(int i=0;i<16-length;i++){  
                      key=key+"0";  
                 }  
            }  
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),"AES");  
             //Creation of Cipher objects  
            Cipher decrypt =Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");  
            decrypt.init(Cipher.DECRYPT_MODE, secretKey);  
            // Open the Encrypted file  
            fis = new FileInputStream(aesFile);  
            cis = new CipherInputStream(fis, decrypt);
            // Write to the Decrypted file  
            
            fos = new FileOutputStream(aesFileBis);  
            byte[] b = new byte[(int)(aesFile.length())];  
            int i = cis.read(b);  
            boolean err = true;
            while (i != -1) {  
                try {
                 fos.write(b, 0, i);
                 i = cis.read(b);
                 err=false;
                } catch (Exception e) {
                    if(err)
                        System.out.println("Error in Decryption");
                    e.printStackTrace();
                    i = -1;
                } 
            }  
           // decrypt.doFinal();
            fos.flush();  
            fos.close();  
            cis.close();  
            fis.close();  
            String filePath = file.getAbsolutePath();
            file.delete();
            File decryptedFile = new File("decryptedFile");
            decryptedFile.renameTo(new File(filePath));
        }catch(Exception ex){
            System.out.println("Exception while decrypting");
            ex.printStackTrace();
        }
    }
    
    public boolean copyFile(File afile,File bfile){
        InputStream inStream = null;
	OutputStream outStream = null;
	boolean success=false;
    	try{	
    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);
        	
    	    byte[] buffer = new byte[1024];
    		
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = inStream.read(buffer)) > 0){
    	  
    	    	outStream.write(buffer, 0, length);
    	 
    	    }
    	 
    	    inStream.close();
    	    outStream.close();
            success=true;
    	    
    	}catch(IOException e){
            e.printStackTrace();
            success=false;
    	}
        return success;
    }
}
class EmailThread extends Thread{

    String link;
    ArrayList<User> recipents;
    User owner;
    public EmailThread(String link,ArrayList<User> recipents,User owner){
        this.link=link;
        this.recipents=recipents;
        this.owner=owner;
    }
    @Override
    public void run() {
        emailLink();
    }
    private void emailLink(){
      // Sender's email ID needs to be mentioned
      String from = "no-reply@gmail.com";//change accordingly
      final String username = "maddy050713";//change accordingly
      final String password = "iloveyoumm1";//change accordingly
      String host = "smtp.gmail.com";
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");
      // Get the Session object.
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         // Create a default MimeMessage object.
        for(User to:recipents)
        {
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to.getEmail()));

            // Set Subject: header field
            message.setSubject("Data Shared with You On Mirror Cloud");

            // Now set the actual message
            message.setText("Hello "+to.getFullName()+", You have some data shared with your Mirror Cloud Account "
                    + "by "+owner.getFullName()+"("+owner.getEmail()+").\n\n "
               + "You can view it anytime by visiting the link given below.\n\n"
                    + "http:\\\\localhost:8080/MirrorWeb/public?data="+link+"\n\n\n"
                    + "This is an auto generated Email and hence cannot be replied. \nThanks.\n\nMirror Cloud.");

            // Send message
            Transport.send(message);

            
          }
        System.out.println("Emails Sent Successfully");
      } catch (MessagingException e) {
            e.printStackTrace();
      }
    }
    
}