package missingpets.controller;

import missingpets.entity.Dog;
import missingpets.entity.User;
import missingpets.service.DogService;
import missingpets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    private UserService userService;
    private DogService dogService;

    @Autowired
    public PhotoController(UserService userService, DogService dogService) {
        this.userService = userService;
        this.dogService = dogService;
    }

    @GetMapping("/user")
    public ResponseEntity<byte[]> userPhoto(@RequestParam String id) throws Exception {
        try {
            User user = userService.findById(id);

            if (user.getPhoto() == null) {
                throw new Exception("No se ha encontrado la foto de este usuario. ");
            }

            byte[] photo = user.getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dog")
    public ResponseEntity<byte[]> dogPhoto(@RequestParam String id) throws Exception {
        try {
            Dog dog = dogService.findById(id);

            if (dog.getPhoto() == null) {
                throw new Exception("No se ha encontrado la foto de este usuario. ");
            }

            byte[] photo = dog.getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
