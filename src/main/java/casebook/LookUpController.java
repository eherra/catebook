
package casebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LookUpController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @GetMapping("/lookup")
    public String view() {
        return "lookup";
    }
    
    @PostMapping("/lookup")
    public String searchProfile() {
        // modeliniin lista ketä löyty tällä haulla ja htmln se
        return "redirect:/lookup";
    }
    
    
}
