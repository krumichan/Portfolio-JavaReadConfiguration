package jp.co.atelier.ReadConfiguration.utility;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class Utility
 */
public final class ClassUtil {
	private ClassUtil() {
	}
	
	/**
	 * 임의의 패키지 내부의 모든 클래스 습득
	 * @param packageName 임의의 패키지
	 * @return 습득한 클래스 리스트
	 * @throws Exception 습득 실패
	 */
	public static List<Class<?>> getClasses(String packageName) throws Exception {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		
		try {
			
			// 현재의 Thread로부터 Class Loader 습득
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            
            // 패키지 명을 path 형식으로 변환
            String path = packageName.replace('.', '/');
            
            // Class Loader로부터 모든 URL을 file: ... .jar! 형식으로 습득
            // getResources 메소드를 사용하면, 일반 실행에서는 directory를 습득할 수 있지만,
            // batch file로 실행할 경우는 directory를 습득할 수 없습니다.
            Enumeration<URL> resources = ClassLoader.getSystemResources(path);
            
            // resource로부터 얻은 URL들로부터 폴더를 습득
            List<String> directories = new ArrayList<String>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                directories.add(resource.getFile());
            }

            // 습득한 폴더로부터 class name을 추출
            TreeSet<String> classes = new TreeSet<String>();
            for (String directory : directories) {
                classes.addAll(findClasses(directory, packageName));
            }

            // 추출한 class name을 통해 class를 습득
            for (String clazz : classes) {
                classList.add(Class.forName(clazz));
            }
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
		
		return classList;
	}
	
	/**
	 * 임의의 폴더에서 package에 해당하는 클래스들의 이름을 습득
	 * @param path 임의의 폴더 위치
	 * @param packageName 패키지 명
	 * @return 습득한 클래스 이름 셋
	 * @throws Exception 습득 실패
	 */
	private static TreeSet<String> findClasses(String path, String packageName) throws Exception {
		TreeSet<String> classes = new TreeSet<String>();

		// path에 file: 및 jar!의 !가 포함되어 있는지 확인
        if (path.startsWith("file:") && path.contains("!")) {

        	// !를 기준으로 jar 위치와 package 위치를 분리
            String[] split = path.split("!");
            URL jar = new URL(split[0]);

            // jar로부터 zip 형식의 stream 습득
            // (jar을 분해하기 위함)
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry;

            // 분해한 jar로부터 모든 entry 탐색
            while ((entry = zip.getNextEntry()) != null) {

            	// entry가 class인지를 확인
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                        .replaceAll("[$].*", "")
                        .replaceAll("[.]class", "")
                        .replace('/', '.');

                    // 해당 class가 임의의 package에 포함되어 있는지를 확인하여 습득
                    if (className.startsWith(packageName)) 
                            classes.add(className);
                }

            }

        }

        // path가 일반 path일 경우, 해당 폴더가 존재하지 않으면 탐색 종료
        File dir = new File(path);
        if (!dir.exists()) {
            return classes;
        }

        // 해당 폴더내에 있는 모든 파일 습득
        File[] files = dir.listFiles();
        for (File file : files) {
            
        	// 만약 탐색하는 파일이 폴더의 경우, 재귀 호출을 통해 내부를 재 탐색하여 습득
        	if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file.getAbsolutePath()
                                    , packageName + "." + file.getName()));
                
            // 만약 탐색하는 파일이 일반 파일의 경우, class인지, 임의의 package에 포함되는지를 확인하여 습득
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file
                                            .getName()
                                            .substring(0, file.getName().length() - 6);
                classes.add(className);
            }
        }

     return classes;
    }
}
