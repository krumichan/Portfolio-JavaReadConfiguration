package jp.co.atelier.ReadConfiguration.service.B;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import jp.co.atelier.ReadConfiguration.model.B.BServiceConfigurationModel;
import jp.co.atelier.ReadConfiguration.utility.CheckUtil;
import jp.co.atelier.ReadConfiguration.utility.json.JsonUtil;

final class BServiceInitialization {
	
	private static final String ERROR_CONFIG_NULL = "설정 파일이 부정하거나 존재하지 않습니다.";
	
	private static final String ERROR_HEIGHT_ILLEGER = "키가 부정한 숫자입니다.";
	
	private static final String ERROR_WEIGHT_ILLEGER = "몸무게가 부정한 숫자입니다.";
	
	/**
	 * 설정 내용을 검사하는 메소드
	 * @param configNode 대상 설정 파일 내용
	 * @return 검사가 끝난 설정 파일 내용
	 * @throws Exception 설정 파일 내용이 바르지 않음
	 */
	public BServiceConfigurationModel checking(JsonNode configNode) throws Exception {
		
		JsonUtil ju = new JsonUtil();
		BServiceConfigurationModel config = null;
		try {
			config = ju.convert(configNode, BServiceConfigurationModel.class);
		} catch (Exception e) {
		}
		
		if (Objects.isNull(config)) {
			throw new Exception(ERROR_CONFIG_NULL);
		}
		
		if (CheckUtil.isZeroOrMinus(config.getHeight())) {
			throw new Exception(ERROR_HEIGHT_ILLEGER);
		}
		
		if (CheckUtil.isZeroOrMinus(config.getWeight())) {
			throw new Exception(ERROR_WEIGHT_ILLEGER);
		}
		
		return config;
	}
}
