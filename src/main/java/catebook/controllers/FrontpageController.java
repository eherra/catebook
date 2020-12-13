
package catebook.controllers;

import catebook.services.AccountService;
import catebook.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontpageController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CommentService commentService;
            
    @GetMapping("/frontpage")
    public String viewFontPage(Model model) {
        model.addAttribute("accounts", accountService.getAmountOfAccounts());
        model.addAttribute("comments", commentService.getCount());
        model.addAttribute("likes", commentService.getLikes());
        return "frontpage";
    }
}
