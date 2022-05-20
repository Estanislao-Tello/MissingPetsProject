package missingpets.controller;

import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;
import missingpets.entity.User;
import missingpets.repository.UserRepository;
import missingpets.service.PhotoService;
import missingpets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user")
public class UserController {

    public UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Para mostrar el formulario y llenar los datos para INICIAR SESION
    //El boton del index "iniciar sesion" utilizaria este metodo
    @GetMapping("/login")
    public String showFormLogin(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "Mail o clave inválidos.");
        }

        return "user/user-login.html";
    }


    @GetMapping("/signup-form")
    public String showFormSignup(ModelMap model) {
        model.addAttribute("user", new User());
        return "user/user-signup.html";
    }

    @PostMapping("/signup-form")
    public String procesarForm(MultipartFile archivo, @ModelAttribute User user, ModelMap model) throws Exception {
        try {
            userService.saveUser(archivo, user);
            return "redirect:/user/login";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "user/user-signup";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list-user")
    public String userList(ModelMap model, @RequestParam(required = false) String name) {

        List<User> users = Collections.EMPTY_LIST;
        if (name == null || name.isEmpty()) {
            users = userService.listUsers();
        } else {
            users = userService.findByName(name);
        }

        model.addAttribute("users", users);
        return "user/list-users";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/user-shutdown/{id}")
    public String userShutdown(@PathVariable("id") String id) {
        User user = userService.findById(id);

        userService.userShutdown(user);
        return "redirect:/user/list-user";
    }

    @GetMapping("/edit-profile")
    public String editProfile(HttpSession session, ModelMap model) {
        try {
            User user = (User) session.getAttribute("session");
            model.addAttribute("profile", user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "user/edit-profile.html";
    }

    @PostMapping("/edit-profile")
    public String modifieProfile(ModelMap model, HttpSession session, MultipartFile archivo, @ModelAttribute(name = "profile") User user) {
        try {
            userService.saveUser(archivo, user);
            session.setAttribute("session", user);

            return "redirect:/";
        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "user/edit-profile";
        }
    }

    @GetMapping("/logout")
    public String logOut() {
        return "redirect:/";
    }

}
