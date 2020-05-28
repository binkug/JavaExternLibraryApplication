package jsonparsing;

import java.io.BufferedReader;
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
import org.json.JSONArray;
import org.json.JSONObject;



public class MovieParsing {

	public static void main(String[] args) {
		//다운로드 받는 코드 작성
		//다운로드 받을 URL을 생성 > HttpURLConnection 인스턴스를 생성하고 옵션을 설정
		// > Stream을 생성해서 다운로드
		/*문자열을 한번에 읽어내기 힘들기 때문에 줄 단위로 읽어서 작업을 하는데 단위로 읽어서 합칠 때는 String
		 * 대신에 StringBuilder를 사용한다.
		 * String은 자신에게 편집 작업을 할 수 없기 때문에 복사해서 수행하므로 메모리 낭비가 발생할 수 있지만
		 * StringBuiler는 자신에게 문자열을 추가 할 수 있음
		 */
		
		//다운로드 받을 문자열을 저장할 변수
		String jsonString = null;
		
		//웹에서 다운로드 받기
		try {
			//URL에 한글이 있으면 한글 부분은 UTF-8로 인코딩
			//URLEncoder.encode("한글문자열","utf-8);
			String addr = "http://swiftapi.rubypaper.co.kr:2029/hoppin/movies?version=1&page=1&count=20&genreId=&order=releasedatease";
			URL url = new URL(addr);
			//httpURLConnection 생성
			//openConnection은 URLConection 이라는 추상 클래스 타입으로 리턴하므로 강제 형변환을 해서 일반 클래스 타입으로 변경
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(30000); //최대연결시간 설정
			con.setUseCaches(false); //이전에 받은 데이터 사용 여부
			
			//문자열을 읽을 스트림을 생성
			//읽은 내용이 깨질때는 con.getInputStream
			//다음에 , "인코딩방식"을 추가
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			//문자열을 읽기 위한 임시변수를 생성
			StringBuilder sb = new StringBuilder();
			
			while(true) {
				//한 줄 읽어오기
				String line = br.readLine();
				//읽은 데이터가 없으면 종료
				if(line == null) {
					break;
				}
				//읽은 데이터가 있으면 sb에 추가
				sb.append(line + "\n");
			}
			
			br.close();
			con.disconnect();
			jsonString = sb.toString();
			
		} catch (Exception e) {
			System.err.println("다운로드 실패");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println(jsonString);
		
		//파싱 결과를 저장할 변수를 생성
		//tilte과
		List<Map<String,Object>> list = new ArrayList<>();
		try {
			//텍스트가 존재한느 경우에만 수행 &&만들 때는 null 체크를 먼저한다.
			if(jsonString != null && jsonString.trim().length() > 0 ) {
				
				//첫번째 JSON 객체
				JSONObject mainData = new JSONObject(jsonString);
				//hoppin이라는 key의 값을 객체로 가져오기
				JSONObject hoppin = mainData.getJSONObject("hoppin");
				//System.out.println(hoppin);
				JSONObject movies = hoppin.getJSONObject("movies");
//				System.out.println(movies);
				
				JSONArray movie = movies.getJSONArray("movie");
				//System.out.println(movie);
				
				for (int i = 0; i < movie.length(); i = i + 1) {
					JSONObject imsi = 
							movie.getJSONObject(i);
					//System.out.println(imsi);
					
					//데이터를 추출
					String title = imsi.getString("title");
					String ratingAverage = imsi.getString("ratingAverage");
					//Map으로 생성
					Map<String, Object> map = 
						new HashMap<>();
					map.put("title", title);
					map.put("ratingAverage", Double.parseDouble(ratingAverage));
					//list에 추가
					list.add(map);
				}
				
			}else {
				System.out.println("다운받은 문자열이 없음");
				System.exit(0);
			}
		} catch (Exception e) {
			System.err.println("파싱 실패");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		//데이터 파싱
		System.out.println(list);
		
		try {
			//드라이버 클래스 로드
			Class.forName("com.mysql.cj.jdbc.Driver");
			//이 메세지가 출력 안되면 MySQL Driver를 프로젝트에 삽입하지 않았거나 드러이버 클래스 이름이 잘못됨
			//System.out.println("로드성공");
			//MySQL 과 java의 driver와 버전이 맞지 않아서 timezone 에러가 발생하는 경우 추가
			//&serverTimezone=UTC
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/baek?userUnicode=true&characterEncoding=utf8&serverTimezone=UTC", "root", "123456789");
			//System.out.println(con);
			
			for(Map<String,Object> map : list) {
				PreparedStatement pstmt = con.prepareStatement("insert into movie(title,ratingaverage) values(?,?)");
				pstmt.setString(1, (String) map.get("title"));
				pstmt.setDouble(2, (double) map.get("ratingAverage"));
				
				//sql 실행
				pstmt.executeUpdate();
				
				pstmt.close();
			}
			
			//연결객체 닫기

			con.close();
		} catch (Exception e) {
			System.err.println("데이터베이스 작업 실패");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
