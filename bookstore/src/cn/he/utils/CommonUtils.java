package cn.he.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

public class CommonUtils {

	/**
	 * 获取随机生成不重复的32位大写字符串，包含数字以及英文字母
	 * @return
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}
	
	/**
	 * 把map装换为bean对象返回
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T getBeanFromMap(Map map, Class<T> clazz) {
		try {
			T bean = clazz.newInstance();
			ConvertUtils.register(new DateConverter(), java.util.Date.class);
			BeanUtils.populate(bean, map);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把处理get请求获得的parameterMap里面中文乱码问题处理好
	 * @param map
	 * @return
	 */
	public static Map<String, String[]> correctMessyParamsMap(Map<String, String[]> map) {
		Set<Entry<String, String[]>> entrySet = map.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++ ) {
				String value = "";
				try {
					value = new String(values[i].getBytes("iso-8859-1"), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				values[i] = value;
			}
			map.replace(key, values);
		}
		return map;
	}
	
}
