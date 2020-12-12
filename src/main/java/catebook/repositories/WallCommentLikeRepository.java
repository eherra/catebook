package catebook.repositories;

import catebook.objects.WallCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WallCommentLikeRepository extends JpaRepository<WallCommentLike, Long> {
    WallCommentLike findByCommentId(Long commentId);
}
