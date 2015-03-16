package micrium.calldetail.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class EmailUtil implements Serializable {

	private static final long serialVersionUID = -1658782155554302587L;

	// private static String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]{2,}+)$";

	public static boolean isEmailValid(String email, String emailPattern) {
		boolean result = true;
		Pattern maskEmail = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotEmpty(email)) {
			Matcher matcher = maskEmail.matcher(email);
			if (!matcher.matches()) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	public static List<String> stringToArray(String str, String separator) {
		List<String> lst = new ArrayList<String>();
		if (str != null) {
			str = str.trim();
			if (!StringUtils.isEmpty(str)) {
				if (str.endsWith(separator)) {
					str = str.substring(0, str.length() - 1);
				}

				if (str.contains(separator)) {
					String[] array = str.split(separator);
					lst = Arrays.asList(array);
				} else {
					lst.add(str);
				}
			}
		}
		return lst;
	}

	public static boolean allEmailValid(List<String> emails, String emailPattern) {
		for (String email : emails) {
			if (!isEmailValid(email, emailPattern)) {
				return false;
			}
		}
		return true;
	}

	/*public static void main(String[] args) {
		String str = "mario@hotmail.com,marioae.inf@gmail.com,marioae.inf@hotmail.com,   ";
		System.out.println(stringToArray(str, ","));
	}
*/
}
