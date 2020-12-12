
package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.Photo;
import catebook.repositories.AccountRepository;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

        if (photoIndexInArray <= 0) {
            photoIndexInArray = 0;
            zeroIndexInAlbum = false;
        }
        
//        if (photoIndexInArray == acc.getAlbumPhotos().size() - 1) {
//            maxIndexInAlbum = false;
//        }
                
        model.addAttribute("user", acc);
        model.addAttribute("photoIndex", photoIndex);
        model.addAttribute("currentlyLogged", currentlyLogged);
        try {
            model.addAttribute("photo", getContent(photoIndex, username));
        } catch (Exception e) {
            model.addAttribute("photo", "paska");
        }
        
        model.addAttribute("maxIndex", maxIndexInAlbum);
        model.addAttribute("zeroIndex", zeroIndexInAlbum);
        
        return "albumpage";
    }
    
    @PostMapping("/albumpage/previous/{username}/getPhoto/{photoIndex}")
    public String getPrevious(@PathVariable String username, @PathVariable Long photoIndex) {
        if (photoIndex > 0) photoIndex--;        
        return "redirect:/albumpage/{username}/" + (photoIndex);
    }
    
    @PostMapping("/albumpage/next/{username}/getPhoto/{photoIndex}")
    public String getNextPhoto(@PathVariable String username, @PathVariable Long photoIndex) {
        return "redirect:/albumpage/{username}/" + (photoIndex + 1);
    }
    
    public String getContent(Long id, String username) throws UnsupportedEncodingException {
        Account acc = accountRepository.findByUsername(username);
        int index = id.intValue();
        int i = 0;
        for (Photo pho :  acc.getAlbumPhotos()) {
            if (i == index) {
                byte[] encode = Base64.getEncoder().encode(pho.getContent());
                return new String(encode, "UTF-8");
            }
            i++;
        }
        
        return null;
    }
}
