
package casebook;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontpageController {
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/frontpage")
    public String viewRegisterPage(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "frontpage";
    }
}
