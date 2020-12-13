package catebook.controllers;

import catebook.objects.Account;
import catebook.services.AccountService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchProfileController {
    private String searchString, stringForStayingAtPage;

    @Autowired
    private AccountService accountService;
    
    @GetMapping("/lookup")
    public String view(Model model) {
        Account currentAcccount = accountService.getCurrentlyLoggedAccount();
        
        if (searchString != null) {
            model.addAttribute("accounts", getUsersToView(searchString));
        } else {
            model.addAttribute("accounts", new ArrayList());
        }
        
        model.addAttribute("currentAccount", currentAcccount);
        model.addAttribute("friendList", accountService.getUsernamesFromFriends(currentAcccount.getUsername()));
        searchString = null;
        
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String searchProfile(@RequestParam String name) {
        searchString = name;
        stringForStayingAtPage = name;
        return "redirect:/lookup";
    }    
    
    @Transactional
    @PostMapping("/lookup/sendRequest/{id}")
    public String sendFriendRequest(@PathVariable Long id) {
        Account requestinAcc = accountService.getCurrentlyLoggedAccount();
        Account toReceivedRequest = accountService.getAccountWithId(id);
        
        if (!toReceivedRequest.getFriendRequests().contains(requestinAcc)
                && !toReceivedRequest.getFriends().contains(requestinAcc)) {
            toReceivedRequest.getFriendRequests().add(requestinAcc);
        }
        searchString = stringForStayingAtPage;
        return "redirect:/lookup";
    }
    
    public List<Account> getUsersToView(String searchString) {
        return accountService.getAllAccounts()
                .stream()
                .filter(h-> h.getProfileName().contains(searchString))
                .collect(Collectors 
                .toCollection(ArrayList::new)); 
    }
}
