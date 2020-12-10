
package casebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LookUpController {
    private List<Account> toViewAccounts;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/lookup")
    public String view(Model model) {
        model.addAttribute("accounts", toViewAccounts);
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String searchProfile(@RequestParam String name) {
        toViewAccounts = accountRepository.findAll()
                .stream()
                .filter(h-> h.getFullName().contains(name))
                .collect(Collectors 
                            .toCollection(ArrayList::new)); 
        
        return "redirect:/lookup";
    }
    
    
}
