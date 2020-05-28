package csvGetData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class AlcoholMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (CsvBeanReader icb = new CsvBeanReader(
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream("C:\\Users\\30404\\Documents\\csv\\경찰청_음주운전적발기록_20200518.csv"))),CsvPreference.STANDARD_PREFERENCE)){
			
			//헤더만들기
			//첫번재 행이 열 제목이러고 설정하고 그 내용을 headers에 저장
			//csv의 파일은 메모장으로 연 다음 다름이름으로 저장해서 utf-8로 저장하면 된다.
			icb.getHeader(true);
			String [] headers = {"num","sex","cnt","age","alcohol","date","police"};
			
			
//			for(String title : headers) {
//				System.out.println(title);
//			}
			//데이터 읽어오기
			/*
			 * 제약조건을 생성 - 각열의 제약 조건 설정
			 * 필수 : new NotNull()
			 * 선택 : new Optional()
			 * 정수 : new ParseInt()
			 * 날짜 : new ParseDate("날짜 서식")
			 * CellProcessor [] 제약조건배열 = new CellProcessor[]{
			 * 
			 * }
			 * 
			 */
			CellProcessor[] processors = new CellProcessor[] {
					new ParseInt(),
					new NotNull(),
					new ParseInt(),
					new NotNull(),
					new NotNull(),
					new ParseDate("yyyy-mm-dd HH:mm"),
					new NotNull()
			//앞에서 만든 reader 객체 read(DTO클래스.class,열이름배열,제약 조건)을 호출 하면 DTO클래스의
			//인스턴스를 순서대로 리턴하는데 더이상 읽을 데이터가 없으면 NULL을 리턴
			};
			List<Alcohol> list = new ArrayList<Alcohol>();
			//순회하면서 데이터를 읽어서 list에 추가 
			while(true) {
				Alcohol ac = icb.read(Alcohol.class, headers, processors);
				
				if(ac == null) {
					break;
				}
				
				//읽은 데이터가 있으면 List에 저장
				list.add(ac);
			}
			
			//list 출력
			for(Alcohol ac : list) {
				System.out.println(ac);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("csv 파일 읽기 실패");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
			
	}

}
