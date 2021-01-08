
package catebook.services;

import catebook.modules.*;
import java.time.*;
import java.util.*;
import catebook.repositories.CommentRepository;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private WallCommentLikeService wallCommentLikeService;
        
    public long getCount() {
        return commentRepository.count();
    }
    
    public int getLikes() {
        return commentRepository.findAll().stream()
                .mapToInt(h -> h.getLikes())
                .sum();
    }
    
    public Comment getCommentWithId(Long id) {
        return commentRepository.getOne(id);
    }
    
    @CacheEvict(value = { "accounts", "comments" }, allEntries = true)
    public Comment saveCommentAndReturn(Long idToBelong, String comment, String profileName) {
        Comment comm = new Comment();
        comm.setText(comment);
        comm.setDate(getDateString());
        comm.setCommentor(profileName);
        comm.setLikes(0);
        comm.setBelonginTo(idToBelong);
        
        return commentRepository.save(comm);
    }
   
    @CacheEvict(value = { "accounts", "comments" }, allEntries = true)
    public void addCommentToAlbum(Long idToBelong, String comment, String whoseWallAt) {
        Account accountWhoseWall = accountService.getAccountWithUsername(whoseWallAt);
        String whoCommentedUsername = accountService.getCurrentlyLoggedUsername();
        
        Comment comm = saveCommentAndReturn(idToBelong, comment, whoCommentedUsername);
        accountWhoseWall.getCommentsOfUser().add(comm);
    }
    
    @CacheEvict(value = { "accounts", "comments" }, allEntries = true)
    public void addCommentToWall(Long belongTo, String comment, String username) {
        Account whoseWallAccount = accountService.getAccountWithUsername(username);
        String whoCommentedUsername = accountService.getCurrentlyLoggedUsername();

        Comment comm = saveCommentAndReturn(-1L, comment, whoCommentedUsername);
        whoseWallAccount.getCommentsOfUser().add(comm);
        
        WallCommentLike newLike = new WallCommentLike();
        newLike.setCommentId(comm.getId());
        wallCommentLikeService.saveWallCommentLike(newLike);
    }
    
    @CacheEvict(value = { "accounts", "comments" }, allEntries = true)
    public void addLikeToCommentIfNotLikedAlready(Long id) {
        Account accountWhoLiked = accountService.getCurrentlyLoggedAccount();
        WallCommentLike commentLikeHelper = wallCommentLikeService.getWallCommentLikeById(id);
        
        if (!wallCommentLikeService.hasAccountAlreadyLikedComment(commentLikeHelper, accountWhoLiked)) {
            Comment comm = getCommentWithId(id);
            comm.setLikes(comm.getLikes() + 1);
            commentLikeHelper.getWhoLiked().add(accountWhoLiked);        
        } 
    }
    
    @Cacheable("comments")
    @CacheEvict(value = "accounts", allEntries = true)
    public List<Comment> getWallComments(String username) {
        return accountService.getAccountWithUsername(username).getCommentsOfUser()
                .stream()
                .filter(h -> h.getBelonginTo().equals(-1L))
                .sorted((h1,h2) -> h2.getDate().compareTo(h1.getDate()))
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
    
    @Cacheable("comments")
    @CacheEvict(value = "accounts", allEntries = true)
    public List<Comment> getAlbumComments(Long photoId, String username) {
        return accountService.getAccountWithUsername(username).getCommentsOfUser()
                .stream()
                .filter(h -> h.getBelonginTo().equals(photoId))
                .sorted((h1,h2) -> h2.getDate().compareTo(h1.getDate()))
                .collect(Collectors 
                .toCollection(ArrayList::new));
    }
    
    public String getDateString() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Helsinki"));  
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");  
        String formattedDate = now.format(format);  
        return formattedDate;
    }
}
