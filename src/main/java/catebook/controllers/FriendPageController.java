
package catebook.controllers;

import catebook.objects.Account;
import catebook.repositories.AccountRepository;
import javax.transaction.Transactional;
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
        String currentlyLoggedusername = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accLogged = accountRepository.findByUsername(currentlyLoggedusername);

        model.addAttribute("user", acc);
        model.addAttribute("currentlyLogged", accLogged);
                
        return "friendpage";
    }
    
    @Transactional
    @PostMapping("/friendpage/accept/{id}")
    public String acceptFriend(@PathVariable Long id) {
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accCurrently = accountRepository.findByUsername(currentlyLogged);
        Account whoSendRequest = accountRepository.getOne(id);
        
        accCurrently.getFriendRequests().remove(whoSendRequest);
        accCurrently.getFriends().add(whoSendRequest);
        whoSendRequest.getFriends().add(accCurrently);

        return "redirect:/friendpage/" + accCurrently.getId();
    }
    
    @Transactional
    @PostMapping("/friendpage/decline/{id}")
    public String declineFriend(@PathVariable Long id) {
        String currentlyLoggedusername = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accCurrently = accountRepository.findByUsername(currentlyLoggedusername);
        Account whoSendRequest = accountRepository.getOne(id);
        
        accCurrently.getFriendRequests().remove(whoSendRequest);

        return "redirect:/friendpage/" + accCurrently.getId();
    }
    
}
