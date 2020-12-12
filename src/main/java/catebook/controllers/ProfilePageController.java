package catebook.controllers;

import catebook.objects.Account;
import catebook.repositories.AccountRepository;
import catebook.objects.Comment;
import catebook.repositories.CommentRepository;
import catebook.repositories.WallCommentLikeRepository;
import catebook.objects.WallCommentLike;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    
    @Autowired
    private WallCommentLikeRepository wallCommentLikeRepository;
    
    @GetMapping("/profilepage")
    public String viewPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/profilepage/" + auth.getName();
    }
    
    @GetMapping("/profilepage/{username}") 
    public String getProfilePage(Model model, @PathVariable String username) {
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        
        model.addAttribute("wallComments", getMax25Comments(username));
        model.addAttribute("user", accountRepository.findByUsername(username));
        model.addAttribute("currentlyLogged", currentlyLogged);

        return "profilepage";
     }
   
    @Transactional
    @PostMapping("/profilepage/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account accuntWhoCommented = accountRepository.findByUsername(username);
        Account whoseWall = accountRepository.getOne(id);
        
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate(getDateString());
        comm.setCommentor(accuntWhoCommented.getUsername());
        comm.setLikes(0);
        commentRepository.save(comm);
        whoseWall.getWallComments().add(comm);
        
        WallCommentLike newLike = new WallCommentLike();
        newLike.setCommentId(comm.getId());
        wallCommentLikeRepository.save(newLike);
                
        return "redirect:/profilepage/" + whoseWall.getUsername();
    }
    
    @Transactional
    @PostMapping("/profilepage/like/{id}/user/{username}") 
    public String addLike(@PathVariable Long id, @PathVariable String username) {
        String whoLikedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Account accountWhoLiked = accountRepository.findByUsername(whoLikedUsername);
        WallCommentLike commentLikeHelper = wallCommentLikeRepository.findByCommentId(id);
        
        if (commentLikeHelper.getWhoLiked().isEmpty() || !commentLikeHelper.getWhoLiked().contains(accountWhoLiked)) {
            Comment comm = commentRepository.getOne(id);
            comm.setLikes(comm.getLikes() + 1);
            commentLikeHelper.getWhoLiked().add(accountWhoLiked);
        } 
        
        return "redirect:/profilepage/{username}";
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
