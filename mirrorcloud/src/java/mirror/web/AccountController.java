/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ahmed
 */
@Controller
public class AccountController {
    
    @Autowired
    private UserDAOImpl userDAO;
    
    @RequestMapping(value = "/processLogin", method = RequestMethod.POST)
    public String getLoginParams(@ModelAttribute("user")User user,HttpSession session,ModelMap model)
    {
        try
        {
            String uname = user.getUname();
            String pass = user.getPass();
            if(!uname.equals("") && !pass.equals(""))
            {
                User temp = userDAO.getUser(uname, 1);
                System.out.println("Temp: "+temp);
                System.out.println("getUser: "+userDAO.getUser(uname, 1));
                if(temp!=null)
                {
                    if(temp.getPass().equals(pass))
                    {
                        session.setAttribute("user", temp);
                        return "redirect:/viewOwnData";
                    }
                    else
                    {
                        model.addAttribute("errors","Invalid UserName / Password");
                        model.addAttribute("command", user);
                        return "index";
                    }
                }
                else
                {
                        model.addAttribute("errors","Invalid UserName / Password");
                        model.addAttribute("command", user);
                        return "index";
                }
            }
            else
            {
                if(uname.equals(""))
                {
                    model.addAttribute("errors","Invalid UserName / Password");
                    model.addAttribute("command", user);
                }
                if(pass.equals(""))
                {
                    model.addAttribute("errors","Invalid UserName / Password");
                    model.addAttribute("command", user);
                }
            }
            return "index";
        }
        catch(CannotGetJdbcConnectionException ex)
        {
            System.err.println("DB Server Not Running");
            return "redirect:/";
        }
        
    }
    @RequestMapping(value = "/processRegister", method = RequestMethod.POST)
    public String getRegisterParams(@ModelAttribute("user")User user,HttpSession session,ModelMap model)
    {
        System.out.println("Register Request");
        try {
            String uname = user.getUname();
            String pass = user.getPass();
            String fullName = user.getFullName();
            String email = user.getEmail();
            if(!uname.equals("") && !pass.equals("") && !fullName.equals("") && !email.equals(""))
            {
                User temp = userDAO.getUser(email, 2);
                if(temp!=null)
                {
                    
                    model.addAttribute("errors","Email already in use");
                    model.addAttribute("command", user);
                    return "register";
                }
                else
                {
                    temp = userDAO.getUser(uname, 1);
                    if(temp!=null)
                    {
                        model.addAttribute("errors","Username already in use");
                        model.addAttribute("command", user);
                        return "register";
                    }
                    else
                    {
                        user.setType("Master");
                        userDAO.create(user);
                        session.setAttribute("user", user);
                        new File(DataController.LOCATION+uname).mkdir();
                        return "redirect:/";
                    }
                }
            }
            else
            {
                if(uname.equals(""))
                {
                    model.addAttribute("unameError","UserName Cannot Be Empty");
                }
                if(pass.equals(""))
                {
                    model.addAttribute("passError","Password Cannot be Empty");
                }
                if(fullName.equals(""))
                {
                    model.addAttribute("fullNameError","Full Name Cannot be Empty");
                }
                if(email.equals(""))
                {
                    model.addAttribute("emailError","Email Cannot be Empty");
                }

                model.addAttribute("command", user);
            }
            return "register";
        }
        catch(CannotGetJdbcConnectionException ex)
        {
            System.err.println("DB Server Not Running");
            return "redirect:/";
        }
    }
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session,ModelMap model)
    {
        session.invalidate();
        return "redirect:/";
    }
    @RequestMapping(value = "/getSimilarUsers",method = RequestMethod.POST)
    public @ResponseBody String getSimilarUsers(@RequestParam("pattern") String pattern,HttpSession session,HttpServletResponse response) throws IOException{
        JSONObject json = new JSONObject();
        User user = (User)session.getAttribute("user");
        if(user==null)
        {
            json.accumulate("success", false);
            json.accumulate("error", "Not Logged In");
        }
        else
        {
            List<User> listUsers = userDAO.listUsers(pattern+"%");
            json.accumulate("success", true);
            if(listUsers!=null){
                for(int i=0;i<listUsers.size();i++){
                    User me = listUsers.get(i);
                    if(me.getUname().equals(user.getUname())){
                        listUsers.remove(me);
                        break;
                    }
                }
                json.accumulate("users", listUsers);
            }
            json.accumulate("hasUsers", listUsers!=null && listUsers.size()>0);
            
                
        }
        return json.toString();
    }
}
