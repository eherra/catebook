
package catebook.controllers;

import catebook.repositories.AccountRepository;
import catebook.repositories.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontpageController {
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CommentRepository commentRepositroy;
        
    @GetMapping("/frontpage")
    public String viewFontPage(Model model) {
        model.addAttribute("accounts", accountRepository.count());
        model.addAttribute("comments", commentRepositroy.count());
//        model.addAttribute("photos", getPhotosAmount());
        model.addAttribute("likes", getLikes());
        return "frontpage";
    }
    
    public int getLikes() {
        return commentRepositroy.findAll().stream()
                .mapToInt(h -> h.getLikes())
                .sum();
    }
    
    public int getPhotosAmount() {
        return accountRepository.findAll().stream()
                .map(h -> h.getAlbumPhotos())
                .mapToInt(h -> h.size())
                .sum();
    }
}
