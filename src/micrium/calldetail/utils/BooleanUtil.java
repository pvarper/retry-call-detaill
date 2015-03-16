package micrium.calldetail.utils;

import java.io.Serializable;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

public class BooleanUtil extends BooleanUtils implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(BooleanUtil.class);

	public static boolean isBoolean(String str) {
		if (StringUtil.isEmpty(str)) {
			return false;
		}
		return "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str);
	}
}
