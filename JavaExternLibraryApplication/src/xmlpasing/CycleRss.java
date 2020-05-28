package xmlpasing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
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

public class CycleRss {

	public static void main(String[] args) {
		//다운로드 받은 문자열을 저장할 변수
		String xmlString = null;
		
		//title 과 lisk 여러 개를 저장할 자료구조를 생성
		//List는 처음 만들 때 인스턴스를 생성해야 한다.
		//List는 특별한 이변이 없는 한 반복문을 돌려야 한다.
		//List가 null을 가지면 반복문에서 예외가 발생합니다.
		//출력할 때 예외가 발생하지 않도록 하기 위해서 인스턴스를 생성하고 시작한다.
		List<Map<String, Object>> list = new ArrayList<>();
		
		//파일 경로 생성
		String filepath = "./updatefile.dat";
		File f = new File(filepath);
		if(f.exists()) {
			//파일이 있으면 파일의 내용을 읽어오기 
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)));
				//한줄을 읽어서 오늘 날짜인지 확인
				String line = br.readLine();
				Date date = new Date(System.currentTimeMillis());
				//오늘 날짜면 프로그램 종료
				if(date.toString().equals(line)) {
					System.out.println("이미 다운로드 받음");
					System.exit(0);
				}
				
				br.close();
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
			System.out.println("다운로드");
			//다운로드 받을 날짜를 기록
			Date date = new Date(System.currentTimeMillis());
			
			//파일에 기록
			PrintWriter pw = new PrintWriter(filepath);
			pw.print(date.toString());
			pw.flush();
			pw.close();
			
			//1.주소만들기
			String addr = "https://openapi.gg.go.kr/Tbbcycltfcacdarm";
			URL url = new URL(addr);
			
			
			//2.연결 객체 만들기 - header 추가 여부 확인
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(30000);
			con.setUseCaches(false);
			//헤더 설정
			
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
				sb.append(line + "\n");
			}
			
			xmlString = sb.toString();
			//4.정리 하기
			br.close();
			con.disconnect();
			
			
		} catch (Exception e) {
			System.out.println("다운로드 실패");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		//데이터 확인
		//System.out.println(xmlString);
		
		
		//다운로드 받은 문자열이 있을때만 파싱을 한다.
		if(xmlString != null && xmlString.trim().length() != 0) {
			
			//루트태그를 찾기
			try {
				//파싱을 수행해주는 DocumentBuilder 인스턴스를 생성하기 위한 DoucmentBuilderFactory 인스턴스 생성
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


				//팩토리 인스턴스를 이용해서 DocumentBuilder 인스턴스를 생성
				DocumentBuilder builder = factory.newDocumentBuilder();
				//파싱을 수행 - 메모리에 펼치기
				Document document = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
				//루트 찾기
				Element root = document.getDocumentElement();
				
				//원하는 태그 찾기
				NodeList nodeList = root.getElementsByTagName("LOC_INFO");
				
				//원하는 태그를 추출 : 여러 개의 태그의 값을 가져오기
				NodeList occur = root.getElementsByTagName("OCCUR_CNT");
				NodeList sltinjry = root.getElementsByTagName("SLTINJRY_INDVDL_CNT");
				
				//노드리스트를 순회
				for(int i=0;i<nodeList.getLength();i++) {
					//하나의 태그를 가져옴
					Map<String,Object> map = new HashMap<>();
					//LOC 저장
					Node node = nodeList.item(i);
					Node loc = node.getFirstChild();
					map.put("LOC_INFO",loc.getNodeValue());
					
					node = occur.item(i);
					loc = node.getFirstChild();
					map.put("OCCUR_CNT",loc.getNodeValue());
					
					node = sltinjry.item(i);
					loc = node.getFirstChild();
					map.put("SLTINJRY_INDVDL_CNT", loc.getNodeValue());
					
					list.add(map);
					
					
				}
				
				for(Map<String,Object> map : list) {
					System.out.println(map);
				}
			} catch (Exception e) {
				
			}

			
		}else {
			System.out.println("다운로드 받은 데이터가 없습니다.");
			System.exit(0);
		}
	}

}
