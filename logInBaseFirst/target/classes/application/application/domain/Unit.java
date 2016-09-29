package application.domain;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "unit")
public class Unit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	@ManyToOne
    private Level level;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		public Level getLevel(){
	        return level;
	    }
	    
	    public void setLevel(Level level){
	    	this.level = level;
	    }

}
