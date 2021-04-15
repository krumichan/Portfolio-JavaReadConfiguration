package jp.co.atelier.ReadConfiguration.template;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 서비스의 기저 클래스
 */
public abstract class AbstractService extends MonoBehaviour {

	/**
	 * 초기화 메소드
	 * @param config 설정 파일로부터 얻은 데이터
	 */
	public abstract void init(JsonNode configNode) throws Exception;
	
	/**
	 * 실제 서비스 동작
	 * @throws Exception 동작시 에러 발생
	 */
	public abstract void execute() throws Exception;
	
	/**
	 * 서비스 종료시 수행
	 */
	public abstract void onEnd();
}
