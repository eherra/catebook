
package catebook.services;

import catebook.objects.Comment;
import catebook.repositories.CommentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
    
    public Comment getCommentWithId(Long id) {
        return commentRepository.getOne(id);
    }
    
    public List<Comment> getMax25Comments(String username) {
        List<Comment> comments = accountService.getAccountWithUsername(username).getWallComments();
        if (comments.size() > 25){
            int startingIndex = comments.size() - 25;
            ArrayList<Comment> lengthFixedComments = new ArrayList();
            for (int i = startingIndex; i < comments.size(); i++) {
                lengthFixedComments.add(comments.get(i));
            }
            Collections.reverse(lengthFixedComments);
            return lengthFixedComments;

        }    
        Collections.reverse(comments);
        return comments;
    }
}
