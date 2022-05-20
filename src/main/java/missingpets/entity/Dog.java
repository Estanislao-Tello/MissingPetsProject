package missingpets.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Dog {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String breed;
    private String size;
    private String colour;
    private String gender;
    private Boolean active;
    private String description;

    @OneToOne
    private Photo photo;
    
    @ManyToOne
    private User owner;

    public Dog() {
    }

    public Dog(String name, String breed, String size, String colour, String gender, Boolean active, String description, Photo photo) {
        this.name = name;
        this.breed = breed;
        this.size = size;
        this.colour = colour;
        this.gender = gender;
        this.active = active;
        this.description = description;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Dog{" + "id=" + id + ", name=" + name + ", breed=" + breed + ", size=" + size + ", colour=" + colour + ", gender=" + gender + ", active=" + active + ", description=" + description + ", photo=" + photo + '}';
    }

}
