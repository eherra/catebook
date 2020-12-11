
package casebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FriendPageController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/friendpage/{userId}")
    public String viewpage(Model model, @PathVariable Long userId) {
        Account acc = accountRepository.getOne(userId);
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
       

        model.addAttribute("user", acc);
        model.addAttribute("currentlyLogged", currentlyLogged);
                
        return "friendpage";
    }
    
}
