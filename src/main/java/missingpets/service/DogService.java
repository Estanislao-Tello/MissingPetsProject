package missingpets.service;

import java.util.List;
import javax.transaction.Transactional;
import missingpets.entity.Dog;
import missingpets.entity.Photo;
import missingpets.entity.User;
import missingpets.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DogService {

    public DogRepository dogRepository;
    public UserService userService;

    private PhotoService photoService;

    @Autowired
    public DogService(DogRepository dogRepository, PhotoService photoService, UserService userService) {
        this.dogRepository = dogRepository;
        this.photoService = photoService;
        this.userService = userService;
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = {Exception.class})
    public void saveDog(MultipartFile archivo, Dog dog) throws Exception {

        if (dog.getBreed() == null || dog.getBreed().trim().isEmpty()) {
            throw new Exception("El perro debe tener raza. ");
        }

        if (dog.getColour() == null || dog.getColour().trim().isEmpty()) {
            throw new Exception("El perro debe tener un color. ");
        }

        if (dog.getDescription() == null || dog.getDescription().trim().isEmpty()) {
            throw new Exception("El anuncio debe contecer una descripcion. ");
        }

        if (archivo == null) {
            throw new Exception("Es necesario que ingrese una foto del perro. ");
        }

        activateIfNew(dog);
        Photo photo = photoService.savePhoto(archivo);
        if (archivo.getContentType().equalsIgnoreCase("application/octet-stream")) {
            photo.setContent(null);
            photo.setMime("image/jpeg");
            dog.setPhoto(null);
        } else {
            dog.setPhoto(photo);
        }
        dogRepository.save(dog);
    }

    @Transactional
    public List<Dog> listDog() {
        return dogRepository.findAll();
    }

    @Transactional
    public Dog findById(String id) {
        return dogRepository.findById(id).get();
    }

    private void activateIfNew(Dog dog) {
        if (dog.getActive() == null) {
            dog.setActive(true);
        }
    }
}
