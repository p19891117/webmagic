package us.codecraft.webmagic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;


public class Configuer {
	public static String readForPath(String fileName) {
		if(StringUtils.isBlank(fileName))
			throw new IllegalArgumentException("文件名不能为空");
		File file = new File(fileName);
		if(file.exists())
			return file.getAbsolutePath();
		return Configuer.class.getClassLoader().getResource(fileName).getPath();
	}

	public static InputStream readForStream(String fileName){
		if(StringUtils.isBlank(fileName))
			throw new IllegalArgumentException("文件名不能为空");
		try {
			return new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			return Configuer.class.getClassLoader().getResourceAsStream(fileName);
		}
	}
}
