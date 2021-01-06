
package catebook.repositories;

import catebook.modules.AlbumLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlbumLikeRepository extends JpaRepository<AlbumLike, Long> {
    AlbumLike findByPhotoId(Long photoId);
}
