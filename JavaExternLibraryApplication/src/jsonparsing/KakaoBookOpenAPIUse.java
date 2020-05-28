package jsonparsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class KakaoBookOpenAPIUse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String jsonString = null;
		String keyword = null;

		try {

			Scanner sc = new Scanner(System.in);
			System.out.print("조회할 도서명 : ");
			keyword = sc.nextLine();
			// 영문과 숫자 이외의 데이터가 있을 수 있으므로 인코딩
			keyword = URLEncoder.encode(keyword, "utf-8");
			/*
			 * curl -v -X GET "https://dapi.kakao.com/v3/search/book?target=title" \
			 * --data-urlencode "query=미움받을 용기" \ -H
			 * "Authorization: KakaoAK kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"
			 * 
			 * 여기서 "https://dapi.kakao.com/v3/search/book?target=title" <- URL 주소 +
			 * "query=미움받을 용기" <- 값 + 위에 만들 입력 받을 값을 입력하면 된다.
			 */
			String addr = "https://dapi.kakao.com/v3/search/book?target=title&query=" + keyword;

			URL url = new URL(addr);

			// URL에 연결 객체 생성
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setUseCaches(false);

			// 헤더 설정
//			-H "Authorization: <- 이것이 헤더 h가 있으면 헤더이다.         KakaoAK kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk"  <- 이것은 카카오 app key를 입력하면 된다.

			con.addRequestProperty("Authorization", "KakaoAK 1f13869342d1f927ce808d84e5bb975b");
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			// 문자열 읽어오기
			while (true) {
				String line = br.readLine();

				if (line == null) {
					break;
				}

				sb.append(line + "\n");

			}
			
			jsonString = sb.toString();
			
			br.close();
			sc.close();
			con.disconnect();

		} catch (Exception e) {
			System.err.println("다운로드 실패");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		//System.out.println(jsonString);
		
		//읽어온 데이터에서 조회된 데이터 건수 찾아오기
		//조회할 데이터 개수를 저장할 변수
		int total_count = -1;
		
		try {
			//문자열을 JSONOBject로 변경
			JSONObject json = new JSONObject(jsonString);
			//meta 속성의 내용을 JSONObject로 가져오기
			JSONObject meta = json.getJSONObject("meta");
			
			//System.out.println(meta);
			//total_count 속성의 값을 정수로 가져오기]
			total_count = meta.getInt("total_count");
			//System.out.println(total_count);
			
		} catch (Exception e) {
			System.out.println("데이터 개수 가져오기 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//3.페이지 개수 구하기
		//페이지당 출력할 개수 구하기
		int perPage =  50;
		//페이지 개수를 계산 전체 데이터 개수(total_count)와 페이지(perPage) 당 출력 개수를 이용
		//서버 프로그래밍에서는 출력할 페이지 개수를 설정하는데 이용하고
		//클라이언트 프로그래밍에서는 읽어야할 페이지 개수를 설정하는데 이용
		int pageCnt = (int)((double)total_count/perPage + (double)(perPage - 1)/perPage);
		//System.out.println(pageCnt);
		
		//4.전체 페이지를 순회하면서 문자열을 전부 읽어오기
		//페이지 별로 읽어서 파싱해도 된다.
		//웹서버에서 읽어올 때는 파라미터 구성을 잘 파악하고 해야 한다.
		
//		int imageCnt = 10;
		//image1.png,image2.png,image3.png .... image10.png
//		for(int i=1;i<=imageCnt;i++) {
//			//System.out.println("image"+i+".png");
//		}
		
		List<Map<String , Object>> list = new ArrayList<>();
		
		try {
			//반복해서 데이터를 가져와서 파싱
			//예외가 발생한 경우 다음 작업을 수행하도록 하는 경우에는 반복문 안에 예외 처리 구문을 만들어야 한다.
			//웹의 데이터를 수집하는 경우 이런 방식으로 처리하지 않으면 처음 조회할 때는 있었는데 수집할 때 없어지면
			//예외가 발생해서 데이터를 수집하지 못하는 경우가 발생
			for(int i=1;i<=pageCnt; i++) {
				try {
					//다운 받을 url을 생성 - 파라미터에 한글이 있는지 확인을 꼭 해야 한다.
					String addr = "https://dapi.kakao.com/v3/search/book?target=title&query=" + keyword+"&page="+i+"&size="+perPage;
					
					URL url = new URL(addr);
					//연결 객체 만들고 옵션 설정
					//문자열을 읽어서 출력해보고 한글이 깨지는지 확인
					//헤더 설정을 해야 하는지 확인을 한다.
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(30000);
					con.setUseCaches(false);
					con.addRequestProperty("Authorization", "KakaoAK 1f13869342d1f927ce808d84e5bb975b");
					
					//문자열 가져오기
					String imsiString = null;
					StringBuffer sb = new StringBuffer();
					BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
					
					while(true) {
						String line = br.readLine();
						if(line == null) {
							break;
						}
						sb.append(line+"\n");
					}
					
					imsiString = sb.toString();
					//System.out.println(imsiString);
					br.close();
					con.disconnect();
					
					JSONObject obj = new JSONObject(imsiString);
					//documents 속성의 내용을 배열로 가져오기
					JSONArray documents = obj.getJSONArray("documents");
					
					for(int j=0;j<documents.length();j++) {
						
						JSONObject document = documents.getJSONObject(i);
						
						String title = document.getString("title");
						String urlStr = document.getString("url");
						int price = document.getInt("price");
						
						//map으로 위의 데이터 묶기
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("title", title);
						map.put("url", urlStr);
						map.put("price", price);
						
						list.add(map);
					}
				} catch (Exception e) {
					System.out.println("실패하면 다음 페이지로 이동");
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			System.out.println("페이지 별 가져오기 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		//가져온 데이터 출력
		for(Map<String,Object> map : list) {
			System.out.println(map.get("title"));
		}
	}

}
