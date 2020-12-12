
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
    public String save(@RequestParam("file") MultipartFile file) throws IOException {
        Photo photo = new Photo();
        
        photo.setContent(file.getBytes()); // the actual photo
        photo.setName(file.getOriginalFilename());
        photo.setMediaType(file.getContentType());
        photo.setSize(file.getSize());
        
        photoRepository.save(photo);
        
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Account currentAccount = accountRepository.findByUsername(currentlyLogged);
        currentAccount.getAlbumPhotos().add(photo);
        
        return "redirect:/profilepage";
    }
}
