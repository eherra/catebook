package catebook.services;

import catebook.modules.Account;
import catebook.modules.AlbumLike;
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
    
    public void deleteAlbumLike(Long photoIdToDelete) {
        AlbumLike albumLikeToDelete = albumLikeRepository.findByPhotoId(photoIdToDelete);
        albumLikeRepository.delete(albumLikeToDelete);
    }
    
    public AlbumLike getAlbumLikeById(Long id) {
        return albumLikeRepository.findByPhotoId(id);
    }
    
    public void initializeAlbumLikePossibility(Long photoId) {
        AlbumLike newLike = new AlbumLike();
        newLike.setPhotoId(photoId);
        albumLikeRepository.save(newLike);
    }
    
    public boolean hasAccountAlreadyLikedPhoto(AlbumLike albumLikeHelper, Account accountWhoLiked) {
        return albumLikeHelper.getWhoLiked().contains(accountWhoLiked);
    }
}
