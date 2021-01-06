package catebook.controllers;

import catebook.modules.*;
import catebook.services.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public String savePhoto(@RequestParam("file") MultipartFile file, @RequestParam String photoComment) throws IOException {
        Account currentAccount = accountService.getCurrentlyLoggedAccount();
        Photo photo = photoService.savePhotoAndReturn(file, photoComment, currentAccount);
        
        if (photo != null) {
            albumLikeService.initializeAlbumLikePossibility(photo.getId());
        }
        return "redirect:/profilepage";
    }
}
