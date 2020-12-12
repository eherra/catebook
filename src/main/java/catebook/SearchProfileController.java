
package casebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchProfileController {
    private List<Account> toViewAccounts;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/lookup")
    public String view(Model model) {
        String loggedUsersname = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("accounts", toViewAccounts);
        model.addAttribute("currentlyLogged", loggedUsersname);
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String searchProfile(@RequestParam String name) {
        String loggedUsersname = SecurityContextHolder.getContext().getAuthentication().getName();

        toViewAccounts = accountRepository.findAll()
                .stream()
                .filter(h-> h.getProfileName().contains(name))
                .collect(Collectors 
                            .toCollection(ArrayList::new)); 
        
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
}
