package catebook;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String handleDefault() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.equals("anonymousUser")) {
            return "redirect:/register";        
        }
        return "redirect:/frontpage";
    }
}
