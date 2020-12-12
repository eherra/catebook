
package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.Photo;
import catebook.repositories.AccountRepository;
import catebook.repositories.PhotoRepository;
import java.io.IOException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Transactional
    @PostMapping("/addPhoto")
    public String save(@RequestParam("file") MultipartFile file, @RequestParam String photoComment) throws IOException {
        Photo photo = new Photo();
        
        photo.setContent(file.getBytes());
        photo.setPhotoText(photoComment);
        photoRepository.save(photo);
        
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountRepository.findByUsername(currentlyLogged);
        currentAccount.getAlbumPhotos().add(photo);
        
        return "redirect:/profilepage";
    }
    
    @Transactional
    @PostMapping("/deletephoto/{photoIndex}/{username}")
    public String delete(@PathVariable Long photoIndex, @PathVariable String username) {
        Account acc = accountRepository.findByUsername(username);
        int indexAsInt = photoIndex.intValue();
        
        acc.getAlbumPhotos().remove(indexAsInt);
        
        return "redirect:/albumpage/" + username;
    }
}
