package missingpets.controller;

import java.util.List;
import missingpets.entity.Dog;
import missingpets.entity.User;
import missingpets.service.DogService;
import missingpets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/pets")
public class DogController {

    private DogService dogService;
    private UserService userService;

    @Autowired
    public DogController(DogService dogService, UserService userService) {
        this.dogService = dogService;
        this.userService = userService;
    }

    //Muestra la lista de todos los perros
    //El boton del index "buscar un perro" utilizaria este metodo
    @GetMapping()
    public String showList(ModelMap model) {
        List<Dog> dogs = dogService.listDog();
        model.addAttribute("dogs", dogs);
        return "dog/dog-list.html";
    }

    //Para mostrar el formulario y llenar los datos
    //El boton del index "publicar un perro" utilizaria este metodo
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/dog-form")
    public String showForm(ModelMap model) {
        model.addAttribute("dog", new Dog());
        return "dog/dog-form.html";
    }

    //Para procesar el formulario de carga de un perro
    @PostMapping("/dog-form")
    public String save(MultipartFile archivo, @ModelAttribute Dog dog,
            ModelMap model, @RequestParam String ownerId) throws Exception {
        try {
            User owner = userService.findById(ownerId);
            dog.setOwner(owner);
            dogService.saveDog(archivo, dog);
            return "redirect:/pets";
        } catch (Exception e) {
            model.put("Error", e.getMessage());
            e.printStackTrace();
            return "dog/dog-form";
        }
    }

}
