
package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.AlbumLike;
import catebook.objects.Photo;
import catebook.services.AccountService;
import catebook.services.AlbumLikeService;
import catebook.services.PhotoService;
import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private AccountService accountService;
 
    @Autowired
    private AlbumLikeService albumLikeService;
    
    @Transactional
    @PostMapping("/addPhoto")
    public String save(@RequestParam("file") MultipartFile file, @RequestParam String photoComment) throws IOException {
        Photo photo = new Photo();
        photo.setContent(file.getBytes());
        photo.setPhotoText(photoComment);
        photo.setLikes(0);
        photoService.savePhoto(photo);
        
        Account currentAccount = accountService.getCurrentlyLoggedAccount();
        currentAccount.getAlbumPhotos().add(photo);
        
        AlbumLike newLike = new AlbumLike();
        newLike.setPhotoId(photo.getId());
        albumLikeService.saveAlbumLike(newLike);
        
        return "redirect:/profilepage";
    }
    
    @Transactional
    @PostMapping("/deletephoto/{photoIndex}/{username}")
    public String delete(@PathVariable Long photoIndex, @PathVariable String username) {
        Account acc = accountService.getAccountWithUsername(username);
        int indexAsInt = photoIndex.intValue();
        
        Photo photoToDelete = acc.getAlbumPhotos().get(indexAsInt);
        AlbumLike albumLikeToDelete = albumLikeService.getAlbumLikeById(photoToDelete.getId());
        
        // deleting photo related information
        albumLikeService.deleteAlbumLike(albumLikeToDelete);
        photoService.deletePhoto(photoToDelete);
        acc.getAlbumPhotos().remove(indexAsInt);
        
        if (acc.getIndexOfProfilePhoto() == photoIndex || acc.getAlbumPhotos().isEmpty()) acc.setIndexOfProfilePhoto(-1);
        if (acc.getIndexOfProfilePhoto() > indexAsInt) acc.setIndexOfProfilePhoto(acc.getIndexOfProfilePhoto() - 1);
        
        return "redirect:/albumpage/" + username;
    }
    
    @Transactional
    @PostMapping("/likephoto/{photoId}/{username}/{photoIndex}") 
    public String addLike(@PathVariable Long photoId, @PathVariable String username, @PathVariable int photoIndex) {
        Account accountWhoLiked = accountService.getCurrentlyLoggedAccount();
        AlbumLike albumLikeHelper = albumLikeService.getAlbumLikeById(photoId);
        
        if (albumLikeHelper.getWhoLiked().isEmpty() || !albumLikeHelper.getWhoLiked().contains(accountWhoLiked)) {
            Photo pho = photoService.getPhotoById(photoId);
            pho.setLikes(pho.getLikes() + 1);
            albumLikeHelper.getWhoLiked().add(accountWhoLiked);
        } 
        
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    @Transactional
    @PostMapping("/changeprofilephoto/{photoIndex}/{username}") 
    public String changeProfilePhoto(@PathVariable int photoIndex, @PathVariable String username) {
        Account acc = accountService.getAccountWithUsername(username);
        acc.setIndexOfProfilePhoto(photoIndex);
        return "redirect:/albumpage/{username}/{photoIndex}";
    }
    
}
