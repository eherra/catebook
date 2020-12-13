
package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.AlbumLike;
import catebook.objects.Photo;
import catebook.repositories.AccountRepository;
import catebook.repositories.AlbumLikeRepository;
import catebook.repositories.PhotoRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AlbumPageController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PhotoRepository photoRepository;
    
    @GetMapping("/albumpage/{username}")
    public String viewAlbumPage(Model model, @PathVariable String username) {
        return "redirect:/albumpage/" + username + "/0";
    }
    
    @GetMapping("/albumpage/{username}/{photoIndex}")
    public String getAlbumPage(Model model, @PathVariable String username, @PathVariable Long photoIndex) {
        String currentlyLogged = SecurityContextHolder.getContext().getAuthentication().getName();
        Account acc = accountRepository.findByUsername(username);
        int photoIndexInArray = photoIndex.intValue();
        boolean zeroIndexInAlbum = true;
        boolean maxIndexInAlbum = true;

        if (photoIndexInArray == 0) {
            photoIndexInArray = 0;
            zeroIndexInAlbum = false;
        }
        
        if (photoIndexInArray == acc.getAlbumPhotos().size() - 1 || photoIndexInArray == 9) {
            maxIndexInAlbum = false;
        }
        
                
        model.addAttribute("user", acc);
        model.addAttribute("photoIndex", photoIndex);
        model.addAttribute("currentlyLogged", currentlyLogged);
        
        if (!acc.getAlbumPhotos().isEmpty()) {
            Photo photo = acc.getAlbumPhotos().get(photoIndexInArray);
            model.addAttribute("photo", photo);
        }
        
        model.addAttribute("maxIndex", maxIndexInAlbum);
        model.addAttribute("zeroIndex", zeroIndexInAlbum);
        model.addAttribute("listEmpty", !acc.getAlbumPhotos().isEmpty());
        
        return "albumpage";
    }
    
    @PostMapping("/albumpage/previous/{username}/getPhoto/{photoIndex}")
    public String getPrevious(@PathVariable String username, @PathVariable Long photoIndex) {
        if (photoIndex > 0) photoIndex--;        
        return "redirect:/albumpage/{username}/" + photoIndex;
    }
    
    @PostMapping("/albumpage/next/{username}/getPhoto/{photoIndex}")
    public String getNextPhoto(@PathVariable String username, @PathVariable Long photoIndex) {
        if (photoIndex < 9) photoIndex++;
        return "redirect:/albumpage/{username}/" + photoIndex;
    }
    
    @GetMapping(path = "/albumpage/{id}/content/{username}", produces = "image/jpeg")
    @ResponseBody
    public byte[] getContent(@PathVariable Long id, @PathVariable String username) {
        Account acc = accountRepository.findByUsername(username);
        int index = id.intValue();
        
        if (acc.getAlbumPhotos().get(index) != null) {
            return acc.getAlbumPhotos().get(index).getContent();
        }
        return null;
    }
}
