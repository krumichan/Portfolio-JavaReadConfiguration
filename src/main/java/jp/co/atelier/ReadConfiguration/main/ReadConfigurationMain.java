package jp.co.atelier.ReadConfiguration.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.ReadConfiguration.constant.Constants;
import jp.co.atelier.ReadConfiguration.template.AbstractService;
import jp.co.atelier.ReadConfiguration.template.MonoBehaviour;
import jp.co.atelier.ReadConfiguration.utility.ClassUtil;
import jp.co.atelier.ReadConfiguration.utility.json.JsonUtil;

public class ReadConfigurationMain {

	/**
	 * 해당 프로그램이 실행시킬 서비스 리스트
	 */
	private static List<MonoBehaviour> serviceList =
			new ArrayList<MonoBehaviour>();
	
	/**
	 * 서비스가 있는 package명
	 */
	private static final String SERVICE_PACAKGE = "jp.co.atelier.ReadConfiguration.service";
	
	/**
	 * 설정 파일의 데이터 얻은 노드
	 */
	private static JsonNode configNode = null;
	
	/**
	 * 중복 종료 처리 수행 방지를 위한 플래그
	 */
	private volatile static boolean isEnded = false;
	
	public static void main(String[] args) {
		
		// 설정 파일 존재 여부 확인
		if (!checkArguments(args)) {
			System.exit(0);
		}
		
		// 설정 파일로부터 JsonNode 흭득
		if (!readAppConf(args[0])) {
			System.exit(0);
		}
		
		try {
			
			// shutdown hook 설정
			//(Ctrl + C 종료시 자동 호출)
			Runtime.getRuntime().addShutdownHook(new Thread(Constants.APPLICATION_NAME + " shutdown hook") {
				@Override
				public void run() {
					onEnd();
				}
			});
			
			// 모든 서비스 습득
			List<Class<?>> classes = null;
			try {
				classes = ClassUtil.getClasses(SERVICE_PACAKGE);
			} catch (Exception e) {
				throw new Exception("Fail to get all services." + "\n" + "package : " + SERVICE_PACAKGE);
			}
			
			// 서비스 실행
			JsonNode services = configNode.get("services");
			for (JsonNode service : services) {
				String name = service.asText();

				// service package로부터 name에 해당하는 클래스 검색
				Class<?> serClazz = null;
				for (Class<?> clazz : classes) {
					String clazzFullName = clazz.getName();
					String clazzName = clazzFullName.substring(clazzFullName.lastIndexOf(".") + 1);
					
					if (clazzName.equals(name)) {
						serClazz = clazz;
						break;
					}
				}
				
				// service class가 존재하지 않으면 프로그램 종료
				if (Objects.isNull(serClazz)) {
					String msg = name + " class does not exists in service package." + "\n"
							+ "package name : " + SERVICE_PACAKGE;
					throw new Exception(msg);
				}
				
				// 서비스 생성
				AbstractService instance = null;
				try {
					instance = (AbstractService)serClazz.newInstance();
				} catch (Exception e) {
					String msg = name + "Can not create instance." + "\n"
							+ "class name : " + serClazz.getName();
					throw new Exception(msg);
				}
				
				// 서비스 실행 및 리스트에 담기
				try {
					instance.init(configNode);
					instance.execute();
					serviceList.add(instance);
				} catch (Exception e) {
					String msg = name + "Fail to execute service." + "\n"
							+ "service name : " + serClazz.getName() + "\n"
							+ "fail cause : " + e.getMessage();
					throw new Exception(msg);
				}
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Failed to execute " + Constants.APPLICATION_NAME);
		} finally {
			onEnd();
		}
	}
	
	/**
	 * 프로그램 종료시의 동작 수행
	 */
	private static void onEnd() {
		if (isEnded) {
			return;
		}
		isEnded = true;
		
		// service들의 종료 처리를 수행
		for (MonoBehaviour service : serviceList) {
			service.sendMessage(service, "onEnd");
		}
	}
	
	/**
	 * 인수를 확인
	 * @param args 인수
	 * @return 확인 결과
	 */
	private static boolean checkArguments(String[] args) {
		if (Objects.isNull(args) ||
			args.length != 1) {
			System.out.println("Failed to read the configuration file.");
			return false;
		}
		return true;
	}
	
	/**
	 * 설정 파일로부터 json 형식의 데이터를 json node 형식으로 습득
	 * @param filePath 설정 파일 위치
	 * @param node 습득한 node를 담을 변수
	 * @return 습득 여부
	 */
	private static boolean readAppConf(String filePath) {
		JsonUtil ju = new JsonUtil();
		try {
			configNode = ju.jsonRead(filePath, JsonNode.class);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
