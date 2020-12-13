
package catebook.services;

import catebook.objects.Photo;
import catebook.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;
    
    public void savePhoto(Photo photo) {
        photoRepository.save(photo);
    }
    
    public void deletePhoto(Photo photo) {
        photoRepository.delete(photo);
    }
    
    public Photo getPhotoById(Long id) {
        return photoRepository.getOne(id);
    }
    
  
}
