
package casebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String viewRegisterPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String addAccount(@RequestParam String username, @RequestParam String fullName, @RequestParam String profileName, @RequestParam String password) {
        Account a = new Account(username, fullName, profileName, passwordEncoder.encode(password));
        accountRepository.save(a);
        System.out.println(a.getFullName());
        System.out.println("käyttäjäluetu");
        return "redirect:/";
    }
    
}
