package casebook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        model.addAttribute("wallComments", getMax25Comments(auth.getName()));
        model.addAttribute("user", accountRepository.findByUsername(auth.getName()));
        
        return "profilepage";
    }
    
    @GetMapping("/profilepage/{username}") 
    public String getProfilePage(Model model, @PathVariable String username) {
        model.addAttribute("wallComments", getMax25Comments(username));
        model.addAttribute("user", accountRepository.findByUsername(username));
        return "userprofile";
     }
   
    @PostMapping("/profilepage/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Account accuntWhoCommented = accountRepository.findByUsername(username);
        Account whoseWall = accountRepository.getOne(id);
        
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate(getDateString());
        comm.setCommentor(accuntWhoCommented.getUsername());
        comm.setLikes(0);
        commentRepository.save(comm);
        whoseWall.getWallComments().add(comm);
        
        accountRepository.save(accuntWhoCommented);
        accountRepository.save(whoseWall);
        
        if (accuntWhoCommented.getId() == whoseWall.getId()) {
            return "redirect:/profilepage";
        }
        
        return "redirect:/profilepage/" + whoseWall.getUsername();
    }
    
    @PostMapping("/profilepage/like/{id}") 
    public String addLike(@PathVariable Long id) {
        Comment comm = commentRepository.getOne(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
                
        comm.setLikes(comm.getLikes() + 1);
        
        commentRepository.save(comm);           
        
        if (comm.getCommentor() == username) {
            return "redirect:/profilepage";
        }
        
        return "redirect:/profilepage";
    }
    
    public String getDateString() {
        LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");  
        String formattedDate = now.format(format);  
        return formattedDate;
    }
    
    public List<Comment> getMax25Comments(String username) {
        List<Comment> comments = accountRepository.findByUsername(username).getWallComments();
        if (comments.size() > 25){
            int index = comments.size() - 25;
            ArrayList<Comment> comments2 = new ArrayList();
            for (int i = index; i < comments.size(); i++) {
                comments2.add(comments.get(i));
            }
            return comments2;
        }            
        return comments;
    }
}
