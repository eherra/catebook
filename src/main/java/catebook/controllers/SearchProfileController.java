
package catebook.controllers;

import catebook.objects.Account;
import catebook.repositories.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchProfileController {
    private String searchString;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/lookup")
    public String view(Model model) {
        String loggedUsersname = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAcccount = accountRepository.findByUsername(loggedUsersname);
        
        if (searchString != null) {
            model.addAttribute("accounts", getUsersToView());
        } else {
            model.addAttribute("accounts", new ArrayList());
        }
        model.addAttribute("currentAccount", currentAcccount);
        model.addAttribute("friendList", getUsernamesFromFriends(loggedUsersname));
        searchString = null;
        
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String searchProfile(@RequestParam String name) {
        searchString = name;
        return "redirect:/lookup";
    }    
    
    @Transactional
    @PostMapping("/lookup/sendRequest/{id}")
    public String sendFriendRequest(@PathVariable Long id) {
        String loggedUsersname = SecurityContextHolder.getContext().getAuthentication().getName();
        Account requestinAcc = accountRepository.findByUsername(loggedUsersname);
        Account toReceivedRequest = accountRepository.getOne(id);
        
        if (!toReceivedRequest.getFriendRequests().contains(requestinAcc)
                && !toReceivedRequest.getFriends().contains(requestinAcc)) {
            toReceivedRequest.getFriendRequests().add(requestinAcc);
        }
        
        return "redirect:/lookup";
    }
    
    public List<String> getUsernamesFromFriends(String logged) {
        return accountRepository.findByUsername(logged).getFriends()
                .stream()
                .map(h -> h.getUsername())
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
    
    public List<Account> getUsersToView() {
        return accountRepository.findAll()
                .stream()
                .filter(h-> h.getProfileName().contains(searchString))
                .collect(Collectors 
                .toCollection(ArrayList::new)); 
    }
}
