
package catebook.controllers;

import catebook.objects.Account;
import catebook.objects.Photo;
import catebook.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AlbumPageController {
    private boolean maxIndexInAlbum, zeroIndexInAlbum;
    
    @Autowired
    private AccountService accountService;

    @GetMapping("/albumpage/{username}")
    public String viewAlbumPage(Model model, @PathVariable String username) {
        return "redirect:/albumpage/" + username + "/0";
    }
    
    @GetMapping("/albumpage/{username}/{photoIndex}")
    public String getAlbumPage(Model model, @PathVariable String username, @PathVariable Long photoIndex) {
        String currentlyLogged = accountService.getCurrentlyLoggedUsername();
        Account acc = accountService.getAccountWithUsername(username);
        int photoIndexInArray = photoIndex.intValue();
        zeroIndexInAlbum = true;
        maxIndexInAlbum = true;

        if (photoIndexInArray == 0) zeroIndexInAlbum = false;
        if (photoIndexInArray == acc.getAlbumPhotos().size() - 1 || photoIndexInArray == 9) maxIndexInAlbum = false;
        
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
        Account acc = accountService.getAccountWithUsername(username);
        int index = id.intValue();
        
        if (acc.getAlbumPhotos().get(index) != null) {
            return acc.getAlbumPhotos().get(index).getContent();
        }
        return null;
    }
}
