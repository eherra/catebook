
package catebook.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import catebook.modules.Account;
import catebook.services.AccountService;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class SearchProfileController {
    private String searchString, searchStringForStayingAtPage;

    @Autowired
    private AccountService accountService;
    
    @GetMapping("/search")
    public String view(Model model) {
        Account currentlyLoggedAccount = accountService.getCurrentlyLoggedAccount();
        
        if (searchString != null) {
            model.addAttribute("searchAccounts", accountService.getUsersToViewOnSearch(searchString));
        } else {
            model.addAttribute("searchAccounts", new ArrayList());
        }
        
        model.addAttribute("currentlyLoggedAccount", currentlyLoggedAccount);
        model.addAttribute("friendList", accountService.getUsernamesFromFriends(currentlyLoggedAccount.getUsername()));
        searchString = null;
        
        return "search";
    }
    
    @PostMapping("/search")
    public String searchProfile(@RequestParam String name) {
        searchString = name.toLowerCase();
        searchStringForStayingAtPage = name.toLowerCase();
        return "redirect:/search";
    }    
    
    @Transactional
    @PostMapping("/search/sendRequest/{id}")
    public String sendFriendRequest(@PathVariable Long id) {
        Account requestinAccount = accountService.getCurrentlyLoggedAccount();
        Account toReceiveRequestAccount = accountService.getAccountWithId(id);
        
        if (eligibleForFriendRequest(requestinAccount, toReceiveRequestAccount)) {
            toReceiveRequestAccount.getFriendRequests().add(requestinAccount);
        }
        
        searchString = searchStringForStayingAtPage;
        return "redirect:/search";
    }
    
    public boolean eligibleForFriendRequest(Account requestinAcc, Account toReceiveRequest) {
        return !toReceiveRequest.getFriendRequests().contains(requestinAcc) 
                && !toReceiveRequest.getFriends().contains(requestinAcc);
    }
}
