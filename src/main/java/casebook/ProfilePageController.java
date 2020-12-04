package casebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilePageController {
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/profilepage")
    public String viewPage() {
        return "profilepage";
    }
}
