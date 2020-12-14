package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.Comment;
import catebook.objects.WallCommentLike;
import catebook.services.AccountService;
import catebook.services.CommentService;
import catebook.services.WallCommentLikeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilePageController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private WallCommentLikeService wallCommentLikeService;
    
    @GetMapping("/profilepage")
    public String viewPage(Model model) {
        return "redirect:/profilepage/" + accountService.getCurrentlyLoggedUsername();
    }
    
    @GetMapping("/profilepage/{username}") 
    public String getProfilePage(Model model, @PathVariable String username) {
        model.addAttribute("wallComments", commentService.getMax25Comments(username));
        model.addAttribute("user", accountService.getAccountWithUsername(username));
        model.addAttribute("currentlyLogged", accountService.getCurrentlyLoggedUsername());

        return "profilepage";
     }
   
    @Transactional
    @PostMapping("/profilepage/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String comment) {
        Account accuntWhoCommented = accountService.getCurrentlyLoggedAccount();
        Account whoseWall = accountService.getAccountWithId(id);
        
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate(getDateString());
        comm.setCommentor(accuntWhoCommented.getProfileName());
        comm.setLikes(0);
        
        commentService.saveComment(comm);
        whoseWall.getWallComments().add(comm);
        
        WallCommentLike newLike = new WallCommentLike();
        newLike.setCommentId(comm.getId());
        wallCommentLikeService.saveWallCommentLike(newLike);
                
        return "redirect:/profilepage/" + whoseWall.getUsername();
    }
    
    @Transactional
    @PostMapping("/profilepage/like/{id}/user/{username}") 
    public String addLike(@PathVariable Long id, @PathVariable String username) {
        Account accountWhoLiked = accountService.getCurrentlyLoggedAccount();
        WallCommentLike commentLikeHelper = wallCommentLikeService.getWallCommentLikeById(id);
        
        if (commentLikeHelper.getWhoLiked().isEmpty() || !commentLikeHelper.getWhoLiked().contains(accountWhoLiked)) {
            Comment comm = commentService.getCommentWithId(id);
            comm.setLikes(comm.getLikes() + 1);
            commentLikeHelper.getWhoLiked().add(accountWhoLiked);
        } 
        
        return "redirect:/profilepage/{username}";
    }
    
    public String getDateString() {
        LocalDateTime now = LocalDateTime.now();  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");  
        String formattedDate = now.format(format);  
        return formattedDate;
    }
}
