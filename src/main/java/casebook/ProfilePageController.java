package casebook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        comm.setDate(getDateString());
        comm.setCommentor(username);
        comm.setLikes(0);
        commentRepository.save(comm);
        acc.getWallComments().add(comm);
        
        accountRepository.save(acc);
        return "redirect:/profilepage";
    }
    
    @PostMapping("/profilepage/{id}") 
    public String addLike(@PathVariable Long id) {
        Comment comm = commentRepository.getOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        comm.setLikes(comm.getLikes() + 1);
        commentRepository.save(comm);
        
        return "redirect:/profilepage";
    }
    
    public String getDateString() {
        LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");  
        String formatDateTime = now.format(format);  
        
        return formatDateTime;
    }
}
