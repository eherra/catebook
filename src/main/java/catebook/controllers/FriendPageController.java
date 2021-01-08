
package catebook.controllers;

import catebook.modules.Account;
import catebook.services.AccountService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FriendPageController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/friendpage/{username}")
    public String viewpage(Model model, @PathVariable String username) {
        Account whosePageAtAccount = accountService.getAccountWithUsername(username);
        Account currentlyLoggedAccount = accountService.getCurrentlyLoggedAccount();

        model.addAttribute("user", whosePageAtAccount);
        model.addAttribute("currentlyLogged", currentlyLoggedAccount);
                
        return "friendpage";
    }
    
    @Transactional
    @PostMapping("/friendpage/accept/{id}/{username}")
    public String acceptFriend(@PathVariable Long id, @PathVariable String username) {
        accountService.acceptAndUpdateFriendLists(id);
        return "redirect:/friendpage/{username}";
    }
    
    @Transactional
    @PostMapping("/friendpage/decline/{id}/{username}")
    public String declineFriend(@PathVariable Long id) {
        accountService.declineFriendRequestAndRemove(id);
        return "redirect:/friendpage/{username}";
    }
    
}
