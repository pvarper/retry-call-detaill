package micrium.calldetail.utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class NumberUtil extends NumberUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(NumberUtil.class);

	public static boolean esNroTigo(String msisdn, String regex) {
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(msisdn);
		if (mat.find()) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(Integer number) {
		boolean result = false;

		if (number != null) {
			String str = String.valueOf(number);
			result = isNumber(str);
		}
		return result;
	}

	public static boolean isNumberId(Integer id) {
		return isNumber(id) && (id > 0);
	}

	public static void main1(String[] args) {
		List<Integer> lst = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		for (Integer numero : lst) {
			if (numero % 2 == 0) {
				continue;
			}
			System.out.println(numero);
		}

		String str = "1234567890";
		String substr = str.substring(0, str.length());
		System.out.println(substr);
	}

	public static int toInt(Object object) {
		String str = String.valueOf(object);
		return NumberUtil.toInt(str);
	}

	public static void main(String[] args) {
		Integer i = null;
		System.out.println(toInt(i));
	}
}
