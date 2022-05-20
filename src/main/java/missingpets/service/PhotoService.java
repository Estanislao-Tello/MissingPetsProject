package missingpets.service;

import java.util.Optional;
import javax.transaction.Transactional;
import missingpets.entity.Photo;
import missingpets.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Transactional
    public Photo savePhoto(MultipartFile archivo) throws Exception {
        
        if (archivo != null) {
            
            try {
                Photo photo = new Photo();

                photo.setMime(archivo.getContentType());
                photo.setName(archivo.getName());
                photo.setContent(archivo.getBytes());

                return photoRepository.save(photo);

            } catch (Exception e) {
                throw new Exception("Ha ocurrido un error. ");
            }
        }
        return null;
    }
    
    @Transactional
    public Photo editPhoto(String idPhoto, MultipartFile archivo) throws Exception{
        if (archivo != null) {
            Photo photo = new Photo();
            if (idPhoto != null) {
                Optional<Photo> respuesta = photoRepository.findById(idPhoto);
                if (respuesta.isPresent()) {
                    photo = respuesta.get();
                }
            }
            photo.setMime(archivo.getContentType());
            photo.setName(archivo.getName());
            photo.setContent(archivo.getBytes());
            
            return photoRepository.save(photo);            
        } else {
            return null;
        }
    }
    
    
    
}
