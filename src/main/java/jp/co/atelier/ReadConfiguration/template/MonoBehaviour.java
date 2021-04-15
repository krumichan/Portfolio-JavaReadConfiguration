package jp.co.atelier.ReadConfiguration.template;

import java.lang.reflect.Method;

public class MonoBehaviour {

	/**
	 * 임의의 메소드를 실행
	 * @param instance 실행시 사용할 인스턴스
	 * @param methodName 실행할 메소드 이름
	 */
	public void sendMessage(MonoBehaviour instance, String methodName) {
		try {
			Method m = instance.getClass().getMethod(methodName);
			m.invoke(instance);
		} catch (Exception e) {
		}
		
	}
	
	/**
	 * 임의의 메소드를 실행
 	 * @param instance 실행시 사용할 인스턴스
	 * @param methodName 실행할 메소드 이름
	 * @param params 실행시 사용할 인수
	 * @throws Exception 실행실패
	 */
	public void sendMessage(MonoBehaviour instance, String methodName, Object... params) {
		try {
			Method m = instance.getClass().getMethod(methodName, params.getClass());
			Object conObj = params;
			m.invoke(instance, conObj);
		} catch (Exception e) {
		}
	}
}
