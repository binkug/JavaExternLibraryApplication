package csvGetData;

import org.junit.Test;

public class TestApp {
	@Test
	public void DAOTest() {
		
		//JUnit을 이용해서 테스트 할 수 있도록 해주는 어노테이션

		TestDAO dao = new TestDAO();
		dao.test();
	}
}
