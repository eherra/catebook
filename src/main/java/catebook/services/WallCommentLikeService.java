package catebook.services;

import catebook.modules.*;
import catebook.repositories.WallCommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WallCommentLikeService {
    
    @Autowired
    private WallCommentLikeRepository wallCommentLikeRepository;
    
    public WallCommentLike getWallCommentLikeById(Long id) {
        return wallCommentLikeRepository.findByCommentId(id);
    }
    
    public void saveWallCommentLike(WallCommentLike like) {
        wallCommentLikeRepository.save(like);
    }
    
    public boolean hasAccountAlreadyLikedComment(WallCommentLike commentLikeHelper, Account accountWhoLiked) {
        return commentLikeHelper.getWhoLiked().contains(accountWhoLiked);
    }
}
