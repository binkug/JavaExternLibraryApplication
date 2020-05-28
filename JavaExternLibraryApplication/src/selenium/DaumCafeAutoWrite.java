package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DaumCafeAutoWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			//크롬을 사용하기 위한 환경 설정
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\30404\\Documents\\driver\\chromedriver.exe");
			//크롬 실행 객체 만들기
			WebDriver driver = new ChromeDriver();
			//페에지 접속
			driver.get("https://logins.daum.net/accounts/signinform.do?url=https%3A%2F%2Fwww.daum.net%2F");
			
			//아이디 입력란을 찾기
			WebElement id = driver.findElement(By.xpath("//*[@id=\"id\"]"));
			id.sendKeys("binkug");
			
			WebElement pw = driver.findElement(By.xpath("//*[@id=\"inputPwd\"]"));
			pw.sendKeys("tmdgns33@@");
			
			WebElement login = driver.findElement(By.xpath("//*[@id=\"loginBtn\"]"));
			login.click();
			
			//페이지 이동이 많을 때는 과부하를 방지하기 위해서
			//중간중간 sleep을 추가
			Thread.sleep(3000);
			
			//카페로 이동
			driver.get("http://cafe.daum.net/samhak7");
			
			driver.switchTo().frame("down");
			
			//글을 입력
			WebElement memo = driver.findElement(By.xpath("//*[@id=\"memoForm__memo\"]/div/table/tbody/tr[1]/td[1]/div/textarea"));
			memo.sendKeys("댓글 입력ㅇㅇ");
			
			WebElement write = driver.findElement(By.xpath("//*[@id=\"memoForm__memo\"]/div/table/tbody/tr[1]/td[2]/a[1]/span[2]"));
			write.click();
		} catch (Exception e) {
			System.out.println("네이버 카페 글 쓰기 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
