package bookadvisor.bookadvisor.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/signin") 
    public String showLogin() { 
        return "signinView";   
     } 
 
    @GetMapping("/signout") 
    public String showLogout() { 
        return "signoutView";  
     } 
}
