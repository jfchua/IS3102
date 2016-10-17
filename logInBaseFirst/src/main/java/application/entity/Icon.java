package application.entity;
import java.util.*;
import javax.persistence.*;

import application.enumeration.IconType;

@Entity
@Table(name = "icon")
public class Icon {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	 @Column(name = "iconPath", nullable = false)
	 private String iconPath;
	 
	 @Enumerated(EnumType.STRING)
	 private IconType iconType;
	 
	 public Icon() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public IconType getIconType() {
		return iconType;
	}

	public void setIconType(IconType iconType) {
		this.iconType = iconType;
	}

	@Override
	public String toString() {
		return "Icon [id=" + id + ", iconPath=" + iconPath + "]";
	}
}
