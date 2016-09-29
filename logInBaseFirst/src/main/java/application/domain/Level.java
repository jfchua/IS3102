package application.domain;
import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "level")
public class Level {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    //must be the same as the one referenced from  the SecurityConfig
    @Column(name = "levelNum", nullable = false)
    private int levelNum;

    @Column(name = "length", nullable = false)
    private int length;
    
    @Column(name = "width", nullable = false)
    private int width;
    
    @Column(name = "filePath", nullable = true)
    private String filePath;
    
    //@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    @OneToMany(mappedBy ="level", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JsonIgnore
    private Set<Unit> units = new HashSet<Unit>();
    
    @ManyToOne
    @JsonIgnore
    private Building building;
    
    public Long getId() {
        return id;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
     
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Set<Unit> getUnits(){
        return units;
    }
    
    public void setUnits(Set<Unit> units){
    	this.units = units;
    }
    
    public Building getBuilding(){
        return building;
    }
    
    public void setBuilding(Building building){
    	this.building = building;
    }
    
    public int getNumUnits(Set<Unit> units){
    	return units.size();
    }
    
    public int getDimensions(){
    	return this.length* this.width;
    }
    
    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", level number=" + levelNum +
                ", length=" + length +
                ", width=" + width +
                ", file path=" + filePath +
                '}';
    }
}
