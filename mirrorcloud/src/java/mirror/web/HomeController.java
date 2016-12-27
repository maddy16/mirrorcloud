/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ahmed
 */
@Controller

public class HomeController {
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String home(HttpSession session,ModelMap model)
    {
        if(session.getAttribute("user")==null)
        {
            model.addAttribute("uname", "Guest");
            model.addAttribute("linkAction", "login");
            model.addAttribute("linkText", "Login");
            model.addAttribute("regShowClass", " ");
            model.addAttribute("command", new User());
        }
        else
        {
            
            User user = (User)session.getAttribute("user");
            model.addAttribute("uname", user.getFullName());
            model.addAttribute("linkAction", "logout");
            model.addAttribute("linkText", "Logout");
            model.addAttribute("regShowClass", "hide");
            model.addAttribute("command", new User());
        }
        
        return "index";
    }
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String showlogin(ModelMap model,HttpSession session)
    {
        if(session.getAttribute("user")!=null)
        {
            return "redirect:/";
        }
        else
        {
            model.addAttribute("command", new User());
            return "index";
        }
        
    }
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String showRegister(ModelMap model,HttpSession session)
    {
        if(session.getAttribute("user")!=null)
        {
            return "redirect:/";
        }
        else
        {
            model.addAttribute("command", new User());
            return "register";
        }
        
    }
    
    
}
