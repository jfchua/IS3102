package application.domain;
import javax.persistence.*;


@Entity
@Table(name = "square")
public class Square {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	
	@Column(name = "positionleft", nullable = false)
    private int left;

    @Column(name = "positiontop", nullable = false)
    private int top;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "type", nullable = false)
    private String type;
    
    @OneToOne(fetch = FetchType.EAGER)
	private Icon icon;
    
    public Long getId() {
		return id;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Square [id=" + id + ", left=" + left + ", top=" + top + ", height=" + height + ", width=" + width
				+ ", color=" + color + ", type=" + type + ", icon=" + icon + "]";
	}

	
	
}
