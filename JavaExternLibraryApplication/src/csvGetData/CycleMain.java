package csvGetData;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

public class CycleMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try(CsvBeanReader reader = new CsvBeanReader(
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream("C:\\Users\\30404\\Documents\\csv\\제주특별자치도_서귀포시_자전거도로현황_20160631.csv"))),CsvPreference.STANDARD_PREFERENCE);) {
			//System.out.println(reader);
			//첫번째 줄이 헤더인지 여부를 결정
			//첫번째 줄은 읽지 않음
			reader.getHeader(true);
			
			String [] headers = {"name","point","lastPoint","km","organ","date"};

			CellProcessor [] processors = new CellProcessor [] {
				new NotNull(),
				new NotNull(),
				new NotNull(),
				new NotNull(),
				new NotNull(),
				new ParseDate("yyyy-mm-dd")
			};
			//읽은 데이터를 저장할 DTO의 List를 생성
			List<Cycle> list = new ArrayList<Cycle>();
			
			//list를 반복문 돌려서 출력
			while(true) {
				//데이터를 1개 읽음 
				Cycle cycle = reader.read(Cycle.class, headers,processors);
				//더이상 읽지 못하면 종료
				if(cycle == null) {
					break;
				}
				
				//읽은 경우 list에 추가
				list.add(cycle);
			}
			//데이터 사용
			for(Cycle cycle : list) {
				System.out.println(cycle);
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
