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
    @PostMapping("/profilepage/comment/{username}")
    public String addComment(@PathVariable String username, @RequestParam String comment) {
        commentService.addCommentToWall(-1L, comment, username);
        return "redirect:/profilepage/{username}";
    }
    
    @Transactional
    @PostMapping("/profilepage/like/{id}/user/{username}") 
    public String addLike(@PathVariable Long id, @PathVariable String username) {
        commentService.addLikeToCommentIfNotLikedAlready(id);
        return "redirect:/profilepage/{username}";
    }
}
