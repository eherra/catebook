
package catebook.controllers;

import catebook.modules.Account;
import catebook.repositories.AccountRepository;
import catebook.services.AccountService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String viewRegisterPage(@ModelAttribute Account account, Model model) {
        return "register";
    }
    
    @GetMapping("/register/button")
    public String goToLogin() {
        return "/login";
    }
    
    @PostMapping("/register")
    public String addAccount(@Valid @ModelAttribute Account accountToRegister, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        
        if (accountService.isUsernameUsed(accountToRegister.getUsername())) {
            accountToRegister.setUsername("Username already exists. Choose other one.");
            return "register";
        }
        
        accountToRegister.setPassword(passwordEncoder.encode(accountToRegister.getPassword()));
        accountService.saveAccount(accountToRegister);
        return "redirect:/login";
    }
}
