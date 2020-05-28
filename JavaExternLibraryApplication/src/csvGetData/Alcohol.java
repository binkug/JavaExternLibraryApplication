package csvGetData;

import java.util.Date;

public class Alcohol {
	private int num;
	private String sex;
	private int cnt;
	private String age;
	private String alcohol;
	private Date date;
	private String police;
	
	
	
	@Override
	public String toString() {
		return "Alcohol [num=" + num + ", sex=" + sex + ", cnt=" + cnt + ", age=" + age + ", alcohol=" + alcohol
				+ ", date=" + date + ", police=" + police + "]";
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAlcohol() {
		return alcohol;
	}
	public void setAlcohol(String alcohol) {
		this.alcohol = alcohol;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPolice() {
		return police;
	}
	public void setPolice(String police) {
		this.police = police;
	}
	
	
	
	

	
}
