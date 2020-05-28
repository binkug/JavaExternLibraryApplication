package csvGetData;
import java.util.Date;

public class Cycle {
	private String name;
	private String point;
	private String lastPoint;
	private String km;
	private String organ;
	private Date date;
	
	
	
	@Override
	public String toString() {
		return "Cycle [name=" + name + ", point=" + point + ", lastPoint=" + lastPoint + ", km=" + km + ", organ="
				+ organ + ", date=" + date + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getLastPoint() {
		return lastPoint;
	}
	public void setLastPoint(String lastPoint) {
		this.lastPoint = lastPoint;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
}
