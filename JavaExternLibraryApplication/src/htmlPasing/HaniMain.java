package htmlPasing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HaniMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String html = null;
		try {
			//URL
			//1.주소만들기
			String addr = "http://www.hani.co.kr/";
			URL url = new URL(addr);
			
			
			//2.연결 객체 만들기 - header 추가 여부 확인
			//header가 있는 경우 api key 나 id나 비밀번호를 설정해야 하는 경우
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setUseCaches(false);
			
			//3.스트림을 만들어서 문자열 읽어오기
			//확인 한 후 한글이 깨지면 수정
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			//문자열을 임시로 저장할 인스턴스
			StringBuilder sb = new StringBuilder();
			//줄 단위로 읽어서 sb에 저장
			while(true) {
				String line = br.readLine();
				if(line == null) {
					break;
				}
				sb.append(line+"\n");
			}
			
			html = sb.toString();
			br.close();
			con.disconnect();
			
		} catch (Exception e) {
			System.out.println("다운로드실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		//데이터 확인 -- 제대로 읽어 왔는지 한글이 깨지는지 
		//System.out.println(html);
		
		if(html !=null && html.trim().length()>0) {
			//문서 구조 가져오기
			Document document = Jsoup.parse(html);
			
			//선택자를 이용해서 가져오기
			Elements elements = document.select("#news-navi > ul.navi-menu > li:nth-child(2) > h3 > a");
			
			//선택자를 이용한 것은 반복문을 수행
			for(int i=0;i<elements.size();i++) {
				//DOM 한개 가져오기
				Element element = elements.get(i);
				//태그 안의 내용을 가져오기
				System.out.println(element.text());
				//시작 태그 안의 href 라는 속성의 값을 가져오기
				System.out.println(element.attr("href"));
			}
		}else {
			System.out.println("읽어온 데이터가 없습니다.");
		}
		
		
	}

}
