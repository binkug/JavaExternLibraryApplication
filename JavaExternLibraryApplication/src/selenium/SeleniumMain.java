package selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			//크롬을 사용하기 위한 환경 설정
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\30404\\Documents\\driver\\chromedriver.exe");
			//크롬 실행 객체 만들기
			WebDriver driver = new ChromeDriver();
			//페에지 접속
			driver.get("https://www.naver.com");
			
		} catch (Exception e) {
			System.out.println("크롬 드라이버 실행 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
