package catebook.services;

import catebook.modules.*;
import catebook.repositories.PhotoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;
    
    public Photo savePhotoAndReturn(MultipartFile file, String photoComment, Account acc) throws IOException {
        if (isImageCorrectType(file)) {
            Photo photo = new Photo();
            photo.setContent(file.getBytes());
            photo.setLikes(0);
            photo.setDescription(photoComment);
            photo.setOwnerId(acc.getId());
            return photoRepository.save(photo);
        }
        return null;
    }
    
    public boolean isImageCorrectType(MultipartFile file) {
        return file.getContentType().equals("image/jpg")
            || file.getContentType().equals("image/jpeg")
            || file.getContentType().equals("image/png");
    }
    
    // configuration to make it work on Heroku
    public void deletePhoto(Long photoId) {
        Photo pho = photoRepository.getOne(photoId);
        pho.setOwnerId(-1L);
        //photoRepository.deleteById(photoId);
    }
    
    public Photo getPhotoById(Long id) {
        return photoRepository.getOne(id);
    }
    
    // Heroku workaround configuration to make photo uploading work.
    public String getEncodedProfilePhoto(Long photoId) {
        Photo pho = photoRepository.getOne(photoId);
        
        try {
            byte[] encode = Base64.getEncoder().encode(pho.getContent());
            return new String(encode, "UTF-8");
        } catch (Exception e) {
            System.out.println("Couldn't fetch profile photo");
        }
        
        return "";
    }
    
    public void addLikeToPhoto(Long photoId, AlbumLike albumLikeHelper, Account accountWhoLiked) {
        Photo pho = photoRepository.getOne(photoId);
        pho.setLikes(pho.getLikes() + 1);
        albumLikeHelper.getWhoLiked().add(accountWhoLiked);
    }
    
    // Heroku workaround configuration to make photo uploading work.
    public List<PhotoEncoded> getPhotosEncodedList(Account acc) {
        List<Photo> photosOfAccount = photoRepository.findByOwnerId(acc.getId());
        List<PhotoEncoded> toReturnList = new ArrayList();

        for (Photo pho : photosOfAccount) {
            try {
                byte[] encode = Base64.getEncoder().encode(pho.getContent());
                PhotoEncoded encoded = new PhotoEncoded(new String(encode, "UTF-8"), pho.getId());
                toReturnList.add(encoded);
            } catch (Exception e) {
                System.out.println("pekele");
            }
        }
        return toReturnList;
    }
    
    
    
    
}
