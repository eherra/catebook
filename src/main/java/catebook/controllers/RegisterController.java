
package catebook.controllers;

import catebook.objects.Account;
import catebook.repositories.AccountRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {
    @Autowired
    private AccountRepository accountRepository;
    
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
    public String addAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "register";
        }
        
        // if username exist already
        if (checkIfUserNameExist(account.getUsername())) {
            account.setUsername("username already exists. Choose other one.");
            return "register";

        }
        
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return "redirect:/login";
    }
    
    public boolean checkIfUserNameExist(String username) {
        return accountRepository.findAll()
                .stream()
                .anyMatch(h -> h.getUsername().equals(username));
    }
}
