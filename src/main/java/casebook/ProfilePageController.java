package casebook;

import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilePageController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @GetMapping("/profilepage")
    public String viewPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("wallComments", accountRepository.findByUsername(auth.getName()).getWallComments());
        model.addAttribute("user", accountRepository.findByUsername(auth.getName()).getFullName());
        model.addAttribute("username", auth.getName());
        
        return "profilepage";
    }
   
    @PostMapping("/profilepage")
    public String addComment(@RequestParam String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
                
        Account acc = accountRepository.findByUsername(username);
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate("12.12.2020");
        comm.setCommentor(username);
        commentRepository.save(comm);
        acc.getWallComments().add(comm);
        
        accountRepository.save(acc);
        return "redirect:/profilepage";
    }
}
