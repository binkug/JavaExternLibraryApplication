package htmlPasing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DongaCrawling {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String html = null;
		try {
			//URL
			//1.주소만들기
			//query 에 들어가는 내용을 인코딩해서 변환 시켜주고 쿼리 값을 넘겨준다.
			String query = "백승훈";
			query = URLEncoder.encode(query,"UTF-8");
			String addr = "https://www.donga.com/news/search?p=1&query="+query+"&check_news=6&more=1&sorting=1&search_date=1&v1=&v2=&range=1";
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
		
		//데이터 건수를 저장할 변수
		int cnt = -1;
		
		try {
			//html parsing을 할 때는 텍스트를 메모리에 펼치기
			Document document = Jsoup.parse(html);
			Elements elements = document.select("#content > div.searchContWrap > div.searchCont > h2 > span:nth-child(1)");
			
			for(int i=0;i<elements.size();i++) {
				Element element = elements.get(i);
				String content = element.ownText();
				//System.out.println(content);
				
				//문자를 가져왔을 때 가져와서 숫자만 가져오기를 하려면 split > (어떤것을 기준으로 자를 것인가 )또는 substring (어디서부터 자를 것인가) 을 이용해서 나눠 가져와야 한다.
				//substring은 정해진 위치에서 변경이 없을 경우만 쓸 수 있고 나머지는 split으로 사용해야 한다.
				
				//기사 건수만 찾아오기
				//공백을 기준으로 분할
				String [] ar = content.split(" ");
				//문자열을 숫자로 바꾸는 거 와 숫자를 문자열로 바꾸는 것은 중요하다.
				cnt = Integer.parseInt(ar[1]);
				//System.out.println(cnt);
			}
		} catch (Exception e) {
			System.out.println("데이터 불러오기 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//페이지당 데이터 개수
		int perPageCnt = 15;
		//페이지 개수 계싼
		int pageCnt = cnt / perPageCnt;
		//페이지 개수 계산
		//전체 데이터 개수를 페이지당 데이터 개수로 나누고 나머지가 있으면 페이지 개수를 1개를 추가
		if(cnt % perPageCnt != 0) {
			pageCnt = pageCnt+1;
		}
		//System.out.println(pageCnt);
		
		//기사의 링크를 저장할 변수
		List<String> list = new ArrayList<>();
		
		try {
			//반복문 안에서 예외가 발생했을 때 다음 반복으로 넘어가고자 하면 반복문 안에서 예외처리를 해야한다.
			for(int i=0;i<pageCnt;i++) {
				try {
					String query = "백승훈";
					query = URLEncoder.encode(query,"UTF-8");
					String addr = "https://www.donga.com/news/search?p="+((i*perPageCnt)+1)+"&query="+query+"&check_news=6&more=1&sorting=1&search_date=1&v1=&v2=&range=1";
					URL url = new URL(addr);
					
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setReadTimeout(10000);
					con.setUseCaches(false);
					
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
					StringBuilder sb = new StringBuilder();
					
					while(true) {
						String line = br.readLine();
						
						if(line == null) {
							break;
						}
						sb.append(line + "\n");
					}
					
					html = sb.toString();
					br.close();
					con.disconnect();
					
					//다운로드 되었는지 한글은 깨지지 않는지 확인
					//System.out.println(html);
					
					
					//링크 수집을 위해서 html 파싱
					Document doc = Jsoup.parse(html);
					//선택자가 너무 길 때는 앞 쪽은 생략해도 된다.
					Elements elements = doc.select("div.t > p.tit > a");
					
					for(int j=0;j<elements.size();j++) {
						Element element = elements.get(j);
						
						//a태그의 href 속성을 list에 저장
						list.add(element.attr("href"));
					}
					//System.out.println(list);
					
				} catch (Exception e) {
					System.out.println("현재 페이지 읽어오기 실패");
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("기사 리스트 저장 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//현재 디렉토리에 백승훈.txt 파일에 기사 내용 저장
		//try()안에 만든 객체는 close를 호출 할 필요가 없다.
		try(PrintWriter pw = new PrintWriter("./백승훈.txt")) {
			for(String link : list) {
				try {
					URL url = new URL(link);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setReadTimeout(10000);
					con.setUseCaches(false);
					
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
					StringBuilder sb = new StringBuilder();
					while(true) {
						String line = br.readLine();
						
						if(line == null) {
							break;
						}
						sb.append(line + "\n");
					}
					
					html = sb.toString();
					br.close();
					con.disconnect();
					
					//System.out.println(html);
					
					Document document = Jsoup.parse(html);
					Elements elements = document.select("#contents > div > div.list_content > div:nth-child(8) > div");
					for(int k=0;k<elements.size();k++) {
						Element element = elements.get(k);
						pw.println(element.text());
						pw.flush();
					}
					
				} catch (Exception e) {
					System.out.println("기사 가져오기 실패");
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			
		} catch (Exception e) {
			System.out.println("기사 저장 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
