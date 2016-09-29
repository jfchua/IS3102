package application.domain;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "building")
public class Building {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    //must be the same as the one referenced from  the SecurityConfig
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "address", nullable = false, unique = true)
    private String address;
    
    @Column(name = "postal_code", nullable = false, unique = true)
    private int postalCode;
    
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "num_floor", nullable = false)
    private int numFloor;
    
    @Column(name = "pic_path", nullable = false)
    private String picPath;
   
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Level> levels = new HashSet<Level>();
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public int getNumFloor() {
        return numFloor;
    }

    public void setNumFloor(int numFloor) {
        this.numFloor = numFloor;
    }
    
    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
    
    public Set<Level> getLevels(){
        return levels;
    }
    
    public void setLevels(Set<Level> levels){
    	this.levels = levels;
    }
    
    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", name=" + name +
                ", address=" + address +
                ", postal code=" + postalCode +
                ", city=" + city +
                ", number of floors=" + numFloor + 
                ", picture file path=" + picPath +
                "}";
    }

}
