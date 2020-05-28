package xmlpasing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeatherXMLParsing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Anonymous Classㄹ를 이용해서
		// Thread 클래스로부터 상속 받는 클래스의 인스턴스 생성

		Thread th = new Thread() {
			// 스레드로 실행할 내용

			public void run() {
				// run 메소드 안에서 예외가 발생 했을 때
				// return 하도록 만들면 스레드를 중지 시킬 수 있다.
				try {
					String weatherString = null;
					try {
						// 1.주소만들기
						String addr = "http://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108";
						URL url = new URL(addr);

						// 2.연결 객체 만들기 - header 추가 여부 확인
						HttpURLConnection con = (HttpURLConnection) url.openConnection();
						con.setConnectTimeout(30000);
						con.setUseCaches(false);
						// 헤더 설정

						// 3.스트림을 만들어서 문자열 읽어오기
						// 확인 한 후 한글이 깨지면 수정
						BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
						// 문자열을 임시로 저장할 인스턴스
						StringBuilder sb = new StringBuilder();

						while (true) {
							String line = br.readLine();
							if (line == null) {
								break;
							}

							sb.append(line + "\n");
						}
						weatherString = sb.toString();
						br.close();
						con.disconnect();
					} catch (Exception e) {
						System.out.println("다운로드 예외");
						System.out.println(e.getMessage());
						e.printStackTrace();
					}

					// 데이터 확인
//					System.out.println(weatherString);

					// 데이터를 저장할 자료구조 확인
					List<Map<String, Object>> list = new ArrayList<>();
					// 데이터를 파싱해서 list에 저장하기
					if (weatherString != null && weatherString.trim().length() > 0) {

						// 파싱을 수행해주는 DocumentBuilder 인스턴스를 생성하기 위한 DoucmentBuilderFactory 인스턴스 생성
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

						// 팩토리 인스턴스를 이용해서 DocumentBuilder 인스턴스를 생성
						DocumentBuilder builder = factory.newDocumentBuilder();
						// 파싱을 수행 - 메모리에 펼치기
						Document document = builder.parse(new ByteArrayInputStream(weatherString.getBytes()));
						// 루트 찾기
						Element root = document.getDocumentElement();

						// 도시이름 찾아오기
						List<String> cities = new ArrayList<>();
						NodeList cityList = root.getElementsByTagName("city");
//						NodeList tmEf = root.getElementsByTagName("tmEf");
//						NodeList tmn = root.getElementsByTagName("tmn");
//						NodeList tmx = root.getElementsByTagName("tmx");
//						NodeList wf = root.getElementsByTagName("wf");

						for (int i = 0; i < cityList.getLength(); i++) {
							Node node = cityList.item(i);
							Node city = node.getFirstChild();
							cities.add(city.getNodeValue());

						}

						// System.out.println(cities);
						// 날짜, 날씨, 최고온도, 최저온도를 가져오기
						NodeList tmEf = root.getElementsByTagName("tmEf");
						NodeList wf = root.getElementsByTagName("wf");
						NodeList tmx = root.getElementsByTagName("tmx");
						NodeList tmn = root.getElementsByTagName("tmn");

						List<String> list1 = new ArrayList<>();
						List<String> list2 = new ArrayList<>();
						List<String> list3 = new ArrayList<>();
						List<String> list4 = new ArrayList<>();

						for (int i = 0; i < tmEf.getLength(); i++) {

							Node node = tmEf.item(i);
							Node city = node.getFirstChild();
							list1.add(city.getNodeValue());

							node = wf.item(i + 1);
							city = node.getFirstChild();
							list2.add(city.getNodeValue());

							node = tmx.item(i);
							city = node.getFirstChild();
							list3.add(city.getNodeValue());

							node = tmn.item(i);
							city = node.getFirstChild();
							list4.add(city.getNodeValue());

						}
//						System.out.println(list1);
//						System.out.println(list2);
//						System.out.println(list3);
//						System.out.println(list4);

						// cityList와 list 1, list,2 list3, list4 의 데이터를 모아서
						// 하나의 List로 만들기
						// city 한개에 각 데이터가 13개씩 존재
						// i:0 -> j:0-12
						// i:1 -> j:13-25
						for (int i = 0; i < cities.size(); i++) {
							String city = cities.get(i);
							for (int j = 0; j < 13; j++) {
								String date = list1.get(i * 13 + j);
								String wf1 = list2.get(i * 13 + j);
								String tmx1 = list3.get(i * 13 + j);
								String tmn1 = list4.get(i * 13 + j);

								Map<String, Object> map = new HashMap<String, Object>();
								map.put("city", city);
								map.put("date", date);
								map.put("wf", wf1);
								map.put("tmx", tmx1);
								map.put("tmn", tmn1);

								list.add(map);

							}

						}
					} else {

						System.out.println("데이터가 없습니다.");
					}
					// System.out.println(list);
//					for(Map<String,Object> map : list) {
//						//map은 사용을 할 때는 항상 형변환을 해줘야한다.
//						if(((String)map.get("city")).equals("서울")){
//							System.out.println(map);
//						}
//					}

					// mysql에 저장
					try {
						// 데이터베이스 작업은
						// 1.드라이버 클래스 로드
						Class.forName("com.mysql.cj.jdbc.Driver");
						System.out.println("드라이버 로드 성공");
						// 2.데이터베이스 연결
						Connection con = DriverManager.getConnection(
								"jdbc:mysql://localhost:3306/baek?userUnicode=true&characterEncoding=utf8&serverTimezone=UTC",
								"root", "123456789");
						// 3.sql 실행
						for (Map<String, Object> map : list) {
							PreparedStatement pstmt = con.prepareStatement(
									"insert into weather (weather_city, weather_date, weather_wf, weather_tmx, weather_tmn) "
											+ "values(?,?,?,?,?)");

							pstmt.setString(1, (String) map.get("city"));
							pstmt.setString(2, (String) map.get("date"));
							pstmt.setString(3, (String) map.get("wf"));
							pstmt.setString(4, (String) map.get("tmx"));
							pstmt.setString(5, (String) map.get("tmn"));

							pstmt.executeLargeUpdate();

							pstmt.close();

						}
						// 4.사용한 객체 정리
					} catch (Exception e) {
						System.out.println("데이터 저장 실패");
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				} catch (Exception e) {

					return;
				}

			}
		};
		// 스레드 시작
		th.start();
	}

}
