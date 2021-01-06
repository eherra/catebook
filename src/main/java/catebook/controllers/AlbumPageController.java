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
    private boolean maxIndexInAlbum, zeroIndexInAlbum;
    
    // Heroku workaround configuration to make photo uploading work.
    private List<PhotoEncoded> albumPhotos; 
    
    @Autowired
    private AccountService accountService;
        
    @Autowired
    private PhotoService photoService;
 
    @Autowired
    private AlbumLikeService albumLikeService;
    
    @Autowired
    private CommentService commentService;

    @GetMapping("/albumpage/{username}")
    public String viewAlbumPage(Model model, @PathVariable String username) {
        albumPhotos = photoService.getPhotosEncodedList(accountService.getAccountWithUsername(username));
        return "redirect:/albumpage/" + username + "/0";
    }
    
    @GetMapping("/albumpage/{username}/{photoIndex}")
    public String getAlbumPage(Model model, @PathVariable String username, @PathVariable int photoIndex) throws UnsupportedEncodingException {
        String currentlyLoggedUsername = accountService.getCurrentlyLoggedUsername();
        Account whosePageAcc = accountService.getAccountWithUsername(username);
        
        boolean isOwnerOfThePage = currentlyLoggedUsername.equals(whosePageAcc.getUsername());
        boolean userHasPhotos = !albumPhotos.isEmpty();
        
        setVisibleOfAlbumNavigatingButtons(photoIndex);
        
        model.addAttribute("user", whosePageAcc);
        model.addAttribute("photoIndex", photoIndex);
        model.addAttribute("isOwnerPage", isOwnerOfThePage);
        model.addAttribute("userHasPhotos", userHasPhotos);
        model.addAttribute("maxIndex", maxIndexInAlbum);
        model.addAttribute("zeroIndex", zeroIndexInAlbum);
                
        if (userHasPhotos) {
            initializePhotoAlbumShow(model, photoIndex, username);
        }

        return "albumpage";
    }
    
    @Transactional
    @PostMapping("/deletephoto/{username}/{photoIndex}")
    public String delete(@PathVariable String username, @PathVariable int photoIndex) {        
        if (!accountService.requestMakerIsAuthorized(username)) {
            return "redirect:/albumpage/{username}/{photoIndex}"; 
        }
        
        Account currentlyLoggedAccount = accountService.getCurrentlyLoggedAccount();
        Long photoToDeleteId = albumPhotos.get(photoIndex).getPhotoId();
        
        removePhotoRelatedInformation(photoToDeleteId, photoIndex);
        
        if (accountService.deletedPhotoWasProfilephoto(currentlyLoggedAccount, photoToDeleteId)) {
            currentlyLoggedAccount.setProfilePhotoId(-1L);
        }
        
        return "redirect:/albumpage/" + currentlyLoggedAccount.getUsername();
    }
    
    @Transactional
    @PostMapping("/likephoto/{photoId}/{username}/{photoIndex}") 
    public String addLike(@PathVariable Long photoId, @PathVariable String username, @PathVariable int photoIndex) {
        Account accountWhoLiked = accountService.getCurrentlyLoggedAccount();
        AlbumLike albumLikeHelper = albumLikeService.getAlbumLikeById(photoId);
        
        if (!albumLikeService.hasAccountAlreadyLikedPhoto(albumLikeHelper, accountWhoLiked)) {
            photoService.addLikeToPhoto(photoId, albumLikeHelper, accountWhoLiked);
        } 
        
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
    @Transactional
    @PostMapping("/addAlbumComment/{photoId}/{username}/{photoIndex}")
    public String addCommentToAlbum(@RequestParam String comment, @PathVariable Long photoId, @PathVariable String username, @PathVariable int photoIndex) {
        Account accountWhoComment = accountService.getCurrentlyLoggedAccount();
        Account accountWhoseWall = accountService.getAccountWithUsername(username);
        
        Comment comm = commentService.saveCommentAndReturn(photoId, comment, accountWhoComment.getProfileName());
        accountWhoseWall.getWallComments().add(comm);
        
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
    @Transactional
    @PostMapping("/changeprofilephoto/{username}/{photoIndex}/{photoId}") 
    public String changeProfilePhoto(@PathVariable String username, @PathVariable Long photoId) {
        if (!accountService.requestMakerIsAuthorized(username)) {
            return "redirect:/albumpage/{username}/{photoIndex}"; 
        }
        
        Account currentlyLoggedAccount = accountService.getCurrentlyLoggedAccount();
        currentlyLoggedAccount.setProfilePhotoId(photoId);
        return "redirect:/albumpage/{username}/{photoIndex}";
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
    
    public void removePhotoRelatedInformation(Long photoToDeleteId, int photoIndex) {
        albumLikeService.deleteAlbumLike(photoToDeleteId);
        photoService.deletePhoto(photoToDeleteId);
        albumPhotos.remove(photoIndex);
    }
    
    public void initializePhotoAlbumShow(Model model, int photoIndex, String username) {
        PhotoEncoded photoEnc = albumPhotos.get(photoIndex);
            
        Photo photo = photoService.getPhotoById(photoEnc.getPhotoId());
        model.addAttribute("photo", photo);
        model.addAttribute("pictureString", photoEnc.getEncodedString());
        model.addAttribute("albumComments", commentService.getAlbumComments(photoEnc.getPhotoId(), username));
    }
    
    public void setVisibleOfAlbumNavigatingButtons(int photoIndexInArray) {
        zeroIndexInAlbum = photoIndexInArray == 0 ? false : true;
        maxIndexInAlbum = photoIndexInArray == albumPhotos.size() - 1 ? false : true;
    }
}
