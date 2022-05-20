
package missingpets.entity;

import missingpets.enums.Role;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class User {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String name;
    private String email;
    private Long phone;
    private String password;
    private Boolean active;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @OneToOne
    private Photo photo;
    
    public User() {
    }

    public User(String name, String email, Long phone, String password, Boolean active, Role role, Photo photo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.active = active;
        this.role = role;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", password=" + password + ", active=" + active + ", role=" + role + ", photo=" + photo + '}';
    }

}
