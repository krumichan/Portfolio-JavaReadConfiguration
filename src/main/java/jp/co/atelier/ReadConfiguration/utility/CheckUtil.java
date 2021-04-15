package jp.co.atelier.ReadConfiguration.utility;

import java.util.Objects;

/**
 * check utility
 */
public final class CheckUtil {
	private CheckUtil() { }
	
	/**
	 * 문자열이 null 또는 빈 문자열인지 검사
	 * @param str 대상 문자열
	 * @return 검사 결과
	 */
	public static boolean isNullOrEmpty(String str) {
		return Objects.isNull(str) || str.isEmpty();
	}
	
	/**
	 * 실수가 0 또는 minus인지 검사
	 * @param flo 대상 실수
	 * @return 검사 결과
	 */
	public static boolean isZeroOrMinus(Float flo) {
		return flo <= 0f;
	}
	
	/**
	 * 정수가 0 또는 minus인지 검사
	 * @param inte 대상 정수
	 * @return 검사 결과
	 */
	public static boolean isZeroOrMinus(Integer inte) {
		return inte <= 0f;
	}
}
