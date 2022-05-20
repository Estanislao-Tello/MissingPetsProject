package missingpets.service;

import missingpets.enums.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import missingpets.entity.Photo;
import missingpets.entity.User;
import missingpets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService implements UserDetailsService {

    public UserRepository userRepository;

    private PhotoService photoService;

    @Autowired
    public UserService(UserRepository userRepository, PhotoService photoService) {
        this.userRepository = userRepository;
        this.photoService = photoService;
    }

    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = {Exception.class})
    public void saveUser(MultipartFile archivo, User user) throws Exception {
       
        validate(user);
        activateIfNew(user);
        Photo photo = photoService.savePhoto(archivo);
        user.setPhoto(photo);

        userRepository.save(user);
    }

    public void validate(User user) throws Exception {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new Exception("Debe ingresar nombre de usuario. ");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new Exception("Debe ingresar dirección de email. ");
        }

        if (user.getPhone() == null) {
            throw new Exception("Debe ingresar un número de teléfono. ");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new Exception("Debe ingresar una contraseña. ");
        }
        
        if (user.getEmail().equals(userRepository.findByEmail(user.getEmail()))) {
            throw new Exception("El mail ingresado ya se encuentra registrado. ");
        }
        
    }

    private void activateIfNew(User user) {
        if (user.getActive() == null) {
            user.setActive(true);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setRole(Role.USER);
        }
    }

    @Transactional
    public User findById(String id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void userShutdown(User user) {
        user.setActive(false);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        System.out.println("USER RANCIO: " + user.toString());

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        List<GrantedAuthority> permissions = new ArrayList<>();
        GrantedAuthority rolePermissions = new SimpleGrantedAuthority("ROLE_" + user.getRole().toString());
        permissions.add(rolePermissions);

        System.out.println("loadbyusername" + user.toString());

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("session", user);

        org.springframework.security.core.userdetails.User userdetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), permissions);

        return userdetails;
    }

}
