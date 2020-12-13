
package catebook.repositories;

import catebook.objects.AlbumLike;
import catebook.objects.WallCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {
        AlbumLike findByPhotoId(Long photoId);
}
