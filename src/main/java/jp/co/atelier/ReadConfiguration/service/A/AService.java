package jp.co.atelier.ReadConfiguration.service.A;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.ReadConfiguration.model.A.AServiceConfigurationModel;
import jp.co.atelier.ReadConfiguration.template.AbstractService;

public class AService extends AbstractService {

	/**
	 * 설정 파일로부터 얻은 데이터
	 */
	private static AServiceConfigurationModel config = null;
	
	/**
	 * 초기화 메소드
	 * @param config 설정 파일로부터 얻은 데이터
	 */
	@Override
	public void init(JsonNode configNode) throws Exception {
		
		AServiceInitialization initialization = 
				new AServiceInitialization();
		try {
			config = initialization.checking(configNode);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 실제 서비스 동작
	 * @throws Exception 동작시 에러 발생
	 */
	@Override
	public void execute() throws Exception {
		
		System.out.println(config);
	}
	
	/**
	 * 서비스 종료시 수행
	 */
	@Override
	public void onEnd() {
		// 종료 이벤트 수행
	}
}
