package jp.co.atelier.ReadConfiguration.service.A;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.ReadConfiguration.model.A.AServiceConfigurationModel;
import jp.co.atelier.ReadConfiguration.utility.CheckUtil;
import jp.co.atelier.ReadConfiguration.utility.json.JsonUtil;

final class AServiceInitialization {

	private static final String ERROR_CONFIG_NULL = "설정 파일이 부정하거나 존재하지 않습니다.";
	
	private static final String ERROR_NAME_NULLOREMPTY = "이름이 null이거나 비어있습니다.";
	
	private static final String ERROR_AGE_ILLEGER = "나이가 부정한 숫자입니다.";
	
	/**
	 * 설정 내용을 검사하는 메소드
	 * @param configNode 대상 설정 파일 내용
	 * @return 검사가 끝난 설정 파일 내용
	 * @throws Exception 설정 파일 내용이 바르지 않음
	 */
	public AServiceConfigurationModel checking(JsonNode configNode) throws Exception {
		
		JsonUtil ju = new JsonUtil();
		AServiceConfigurationModel config = null;
		try {
			config = ju.convert(configNode, AServiceConfigurationModel.class);
		} catch (Exception e) {
		}
		
		if (Objects.isNull(config)) {
			throw new Exception(ERROR_CONFIG_NULL);
		}
		
		if (CheckUtil.isNullOrEmpty(config.getName())) {
			throw new Exception(ERROR_NAME_NULLOREMPTY);
		}
		
		if (CheckUtil.isZeroOrMinus(config.getAge())) {
			throw new Exception(ERROR_AGE_ILLEGER);
		}
		
		return config;
	}
}
