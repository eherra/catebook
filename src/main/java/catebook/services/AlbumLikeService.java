package catebook.services;

import catebook.objects.AlbumLike;
import catebook.repositories.AlbumLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumLikeService {
    
    @Autowired
    private AlbumLikeRepository albumLikeRepository;
    
    public void saveAlbumLike(AlbumLike like) {
        albumLikeRepository.save(like);
    }
    
    public void deleteAlbumLike(AlbumLike like) {
        albumLikeRepository.delete(like);
    }
    
    public AlbumLike getAlbumLikeById(Long id) {
        return albumLikeRepository.findByPhotoId(id);
    }
}
