package catebook.repositories;

import catebook.modules.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByOwnerId(Long ownerId);
}
