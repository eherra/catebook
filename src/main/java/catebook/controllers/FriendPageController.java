
package catebook.controllers;

import catebook.objects.Account;
import catebook.services.AccountService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FriendPageController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/friendpage/{userId}")
    public String viewpage(Model model, @PathVariable Long userId) {
        Account acc = accountService.getAccountWithId(userId);
        Account accLogged = accountService.getCurrentlyLoggedAccount();

        model.addAttribute("user", acc);
        model.addAttribute("currentlyLogged", accLogged);
                
        return "friendpage";
    }
    
    @Transactional
    @PostMapping("/friendpage/accept/{id}")
    public String acceptFriend(@PathVariable Long id) {
        Account accCurrently = accountService.getCurrentlyLoggedAccount();
        Account whoSendRequest = accountService.getAccountWithId(id);
        
        accCurrently.getFriendRequests().remove(whoSendRequest);
        accCurrently.getFriends().add(whoSendRequest);
        whoSendRequest.getFriends().add(accCurrently);

        return "redirect:/friendpage/" + accCurrently.getId();
    }
    
    @Transactional
    @PostMapping("/friendpage/decline/{id}")
    public String declineFriend(@PathVariable Long id) {
        Account accCurrently = accountService.getCurrentlyLoggedAccount();
        Account whoSendRequest = accountService.getAccountWithId(id);
        
        accCurrently.getFriendRequests().remove(whoSendRequest);

        return "redirect:/friendpage/" + accCurrently.getId();
    }
    
}
