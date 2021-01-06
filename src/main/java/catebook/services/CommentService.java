
package catebook.services;

import catebook.modules.*;
import java.time.*;
import java.util.*;
import catebook.repositories.CommentRepository;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private AccountService accountService;
    
    public long getCount() {
        return commentRepository.count();
    }
    
    public int getLikes() {
        return commentRepository.findAll().stream()
                .mapToInt(h -> h.getLikes())
                .sum();
    }
    
    public Comment saveCommentAndReturn(Long idToBelong, String comment, String profileName) {
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate(getDateString());
        comm.setCommentor(profileName);
        comm.setLikes(0);
        comm.setBelonginTo(idToBelong);
        
        return commentRepository.save(comm);
    }
    
    public String getDateString() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Helsinki"));  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");  
        String formattedDate = now.format(format);  
        return formattedDate;
    }
    
    public Comment getCommentWithId(Long id) {
        return commentRepository.getOne(id);
    }
    
    public void addLikeToComment(Long id, WallCommentLike commentLikeHelper, Account accountWhoLiked) {
        Comment comm = getCommentWithId(id);
        comm.setLikes(comm.getLikes() + 1);
        commentLikeHelper.getWhoLiked().add(accountWhoLiked);
    }
    
    public List<Comment> getWallComments(String username) {
        return accountService.getAccountWithUsername(username).getWallComments()
                .stream()
                .filter(h -> h.getBelonginTo().equals(-1L))
                .sorted((h1,h2) -> h2.getDate().compareTo(h1.getDate()))
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
    
    public List<Comment> getAlbumComments(Long photoId, String username) {
        return accountService.getAccountWithUsername(username).getWallComments()
                .stream()
                .filter(h -> h.getBelonginTo().equals(photoId))
                .sorted((h1,h2) -> h2.getDate().compareTo(h1.getDate()))
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
}
