package catebook.controllers;

import catebook.modules.*;
import catebook.services.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import catebook.repositories.PhotoRepository;
import java.io.UnsupportedEncodingException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class AlbumPageController {
    private boolean nextButtonVisible, previousButtonVisible;
    
    // Heroku workaround configuration to make photo uploading work.
    private List<PhotoEncoded> albumPhotos; 
    
    @Autowired
    private AccountService accountService;
        
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping("/albumpage/{username}/{photoIndex}")
    public String viewAlbumPage(Model model, @PathVariable String username, @PathVariable int photoIndex) throws UnsupportedEncodingException {
        Account whosePageAcc = accountService.getAccountWithUsername(username);
                
        setVisibleOfAlbumNavigatingButtons(photoIndex);
        model.addAttribute("isNextVisible", nextButtonVisible);
        model.addAttribute("isPreviousVisible", previousButtonVisible);
        
        model.addAttribute("user", whosePageAcc);
        model.addAttribute("photoIndex", photoIndex);
        model.addAttribute("isOwnerPage", accountService.isOwnerOfThePage(whosePageAcc.getUsername()));
        
        boolean userHasPhotos = !albumPhotos.isEmpty();
        model.addAttribute("userHasPhotos", userHasPhotos);
                
        if (userHasPhotos) {
            initializePhotoAlbumShow(model, photoIndex, username);
        }

        return "albumpage";
    }
    
    @GetMapping("/albumpage/{username}")
    public String getAlbumPage(Model model, @PathVariable String username) {
        albumPhotos = photoService.getPhotosEncodedList(accountService.getAccountWithUsername(username));
        return "redirect:/albumpage/{username}/0";
    }
        
    @Transactional
    @PostMapping("/likephoto/{photoId}/{username}/{photoIndex}") 
    public String addLikeToPhoto(@PathVariable Long photoId, @PathVariable String username, @PathVariable int photoIndex) {
        photoService.addLikeToPhotoIfNotLikedAlready(photoId);
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
    @Transactional
    @PostMapping("/addAlbumComment/{photoId}/{username}/{photoIndex}")
    public String addCommentToAlbum(@RequestParam String comment, @PathVariable Long photoId, @PathVariable String username, @PathVariable int photoIndex) {
        commentService.addCommentToAlbum(photoId, comment, username);
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
    @Transactional
    @PostMapping("/changeprofilephoto/{username}/{photoIndex}/{photoId}") 
    public String changeProfilePhoto(@PathVariable String username, @PathVariable Long photoId) {
        if (!accountService.requestMakerIsAuthorized(username)) {
            return "redirect:/albumpage/{username}/{photoIndex}"; 
        }
        
        accountService.setProfilePhoto(photoId);
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
    @Transactional
    @PostMapping("/deletephoto/{username}/{photoIndex}")
    public String deletePhoto(@PathVariable String username, @PathVariable int photoIndex) {        
        if (!accountService.requestMakerIsAuthorized(username)) {
            return "redirect:/albumpage/{username}/{photoIndex}"; 
        }
                
        Long photoToDeleteId = albumPhotos.get(photoIndex).getPhotoId();
        albumPhotos.remove(photoIndex);
        photoService.deletePhotoAndRelatedInformation(photoToDeleteId);
                        
        return "redirect:/albumpage/{username}";
    }
    
    @GetMapping("/albumpage/previous/{username}/getPhoto/{photoIndex}")
    public String getPrevious(@PathVariable String username, @PathVariable Long photoIndex) {
        if (photoIndex > 0) photoIndex--;        
        return "redirect:/albumpage/{username}/" + photoIndex;
    }
    
    @GetMapping("/albumpage/next/{username}/getPhoto/{photoIndex}")
    public String getNextPhoto(@PathVariable String username, @PathVariable Long photoIndex) {
        return "redirect:/albumpage/{username}/" + (photoIndex + 1);
    }
    
    public void initializePhotoAlbumShow(Model model, int photoIndex, String username) {
        PhotoEncoded photoEnc = albumPhotos.get(photoIndex);
            
        Photo photo = photoService.getPhotoById(photoEnc.getPhotoId());
        model.addAttribute("photo", photo);
        model.addAttribute("pictureString", photoEnc.getEncodedString());
        model.addAttribute("albumComments", commentService.getAlbumComments(photoEnc.getPhotoId(), username));
    }
    
    public void setVisibleOfAlbumNavigatingButtons(int photoIndexInArray) {
        previousButtonVisible = photoIndexInArray != 0 ? true : false;
        nextButtonVisible = photoIndexInArray != albumPhotos.size() - 1 ? true : false;
    }
}
