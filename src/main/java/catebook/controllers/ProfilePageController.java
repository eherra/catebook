package catebook.controllers;

import catebook.modules.*;
import catebook.services.*;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class ProfilePageController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private WallCommentLikeService wallCommentLikeService;
    
    @Autowired
    private PhotoService photoService;
    
    @GetMapping("/profilepage")
    public String viewPage(Model model) {
        return "redirect:/profilepage/" + accountService.getCurrentlyLoggedUsername();
    }
    
    @GetMapping("/profilepage/{username}") 
    public String getProfilePage(Model model, @PathVariable String username) {
        Account profileToShow = accountService.getAccountWithUsername(username);
        boolean profilePhotoHasBeenSet = accountService.isProfilePhotoSetted(profileToShow);
        boolean isOwnerOfThePage = accountService.isOwnerOfThePage(username);

        model.addAttribute("wallComments", commentService.getWallComments(username));
        model.addAttribute("user", profileToShow);
        model.addAttribute("isOwnerPage", isOwnerOfThePage);
        model.addAttribute("profilePhotoHasBeenSet", profilePhotoHasBeenSet);

        if (profilePhotoHasBeenSet) {
            model.addAttribute("pictureString", photoService.getEncodedProfilePhoto(profileToShow.getProfilePhotoId()));
        }

        return "profilepage";
     }
   
    @Transactional
    @PostMapping("/profilepage/{id}")
    public String addComment(@PathVariable Long id, @RequestParam String comment) {
        Account accountWhoCommented = accountService.getCurrentlyLoggedAccount();
        Account whoseWall = accountService.getAccountWithId(id);
        
        Comment comm = commentService.saveCommentAndReturn(-1L, comment, accountWhoCommented.getProfileName());
        
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
        
        if (!wallCommentLikeService.hasAccountAlreadyLikedComment(commentLikeHelper, accountWhoLiked)) {
            commentService.addLikeToComment(id, commentLikeHelper, accountWhoLiked);
        } 
        
        return "redirect:/profilepage/{username}";
    }
}
