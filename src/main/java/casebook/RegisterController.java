
package casebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String viewRegisterPage(@ModelAttribute Account account) {
        return "register";
    }
    
    @GetMapping("/register/button")
    public String goToLogin() {
        return "/login";
    }
    
    @PostMapping("/register")
    public String addAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return "redirect:/login";
    }
    
}
