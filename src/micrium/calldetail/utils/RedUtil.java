package micrium.calldetail.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class RedUtil {

	public static final int BIT_COUNT = 32;
	public static final int OBTETO_COUNT = 4;
	public static final int BIT_OBTETO_COUNT = 8;

	public static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static Logger log = Logger.getLogger(PrintUtil.class);

	public static boolean isIpOfRed(String ip, String redMasc) {
		if (StringUtil.isEmpty(ip) || StringUtil.isEmpty(redMasc)) {
			log.info("La ip " + ip + " o la red " + redMasc + " no son validos.");
			return Boolean.FALSE;
		}
		String redIp = StringUtil.substringBefore(redMasc, "/");
		String masc = StringUtil.substringAfter(redMasc, "/");
		String redOfIp = getRed(ip, NumberUtil.toInt(masc));
		boolean result = StringUtil.equalsIgnoreCase(redIp, redOfIp);
		log.info(result ? "La ip pertenece a la red " + redMasc : "La ip no pertenece a la red " + redMasc);
		return result;
	}

	public static boolean redMascValidate(String redMasc, String pattern) {
		if (StringUtil.isEmpty(redMasc)) {
			log.info("La red " + redMasc + " no es valido, esta vacio.");
			return Boolean.FALSE;
		}
		String redIp = StringUtil.substringBefore(redMasc, "/");
		if (!ipValidate(redIp, pattern)) {
			return Boolean.FALSE;
		}

		String masc = StringUtil.substringAfter(redMasc, "/");
		if (StringUtil.isEmpty(masc)) {
			log.info("La macara " + masc + " no es valido, esta vacio.");
			return Boolean.FALSE;
		} else if (!NumberUtil.isNumber(masc)) {
			log.info("La mascara " + masc + " no es valido, no es numerico.");
			return Boolean.FALSE;
		} else if (NumberUtil.toInt(masc) > BIT_COUNT) {
			log.info("La mascara " + masc + " no es valido, es superior a " + BIT_COUNT);
			return Boolean.FALSE;
		}

		log.info("La red " + redMasc + " es valido");
		return Boolean.TRUE;
	}

	public static boolean ipValidate(String ip, String ipPattern) {
		if (StringUtil.isEmpty(ip)) {
			log.info("La ip " + ip + " no es valido, esta vacio.");
			return Boolean.FALSE;
		}
		Pattern pattern = Pattern.compile(ipPattern);
		Matcher matcher = pattern.matcher(ip);
		boolean result = matcher.matches();
		log.info(result ? "La ip " + ip + " es valido." : "La ip " + ip + " no es valido.");
		return result;
	}

	public static String getRed(String ip, int masc) {
		BitSet[] bitsetsIp = ipToBitSet(ip);
		if (bitsetsIp == null) {
			return StringUtil.EMPTY;
		}
		System.out.println();
		BitSet[] bitsetsMasc = mascToBitSet(masc);
		and(bitsetsIp, bitsetsMasc);

		// System.out.println();
		// for (BitSet bitSet : bitsetsIp) {
		// System.out.print(toString(bitSet) + ".");
		// }
		String red = bitSetToIp(bitsetsIp);
		log.info("Obtenemos la red " + red + " haciendo un and entre la ip " + ip + " y la mascara " + masc);

		return red;
	}

	public static String bitSetToIp(BitSet[] bitsets) {
		byte[] binaryIP = new byte[OBTETO_COUNT];
		int k = 0;
		for (BitSet b : bitsets) {
			binaryIP[k] = bitSetToByte(b);
			k++;
		}

		try {
			InetAddress ip = InetAddress.getByAddress(binaryIP);
			return ip.getHostAddress();
		} catch (UnknownHostException e) {
			log.error("La ip no es valido.", e);
			return null;
		}

	}

	public static BitSet[] ipToBitSet(String ip) {
		try {
			InetAddress addres = InetAddress.getByName(ip);
			byte[] binaryIP = addres.getAddress();

			BitSet[] bitsets = new BitSet[binaryIP.length];
			int k = 0;
			for (byte b : binaryIP) {
				bitsets[k] = byteToBitSet(b);
				// System.out.print(toString(bitsets[k]) + ".");
				k++;
			}

			return bitsets;
		} catch (UnknownHostException e) {
			log.error("La ip no es valido.", e);
			return null;
		}
	}

	public static BitSet[] mascToBitSet(int masc) {
		BitSet[] bitsets = new BitSet[OBTETO_COUNT];
		int k = 0;
		BitSet bitSet = new BitSet();
		for (int i = 0; i < BIT_COUNT; i++) {

			bitSet.set(i % BIT_OBTETO_COUNT, (i < masc));

			if ((i + 1) % BIT_OBTETO_COUNT == 0) {
				bitsets[k] = bitSet;
				// System.out.print(toString(bitsets[k]) + ".");
				bitSet = new BitSet();
				k++;
			}

		}
		return bitsets;
	}

	public static void and(BitSet[] bitsets1, BitSet[] bitsets2) {
		int k = 0;
		for (BitSet bitSet1 : bitsets1) {
			BitSet bitSet2 = bitsets2[k];
			bitSet1.and(bitSet2);
			k++;
		}
	}



	public static BitSet byteToBitSet(byte b) {
		BitSet bits = new BitSet(8);
		for (int i = 0; i < 8; i++) {
			bits.set(i, ((b & (1 << i)) != 0));
		}
		return bits;
	}

	public static byte bitSetToByte(BitSet bits) {
		int value = 0;
		for (int i = 0; i < 8; i++) {
			if (bits.get(i) == true) {
				value = value | (1 << i);
			}
		}
		return (byte) value;
	}

	public static byte bitsToByte(boolean[] bits) {
		int value = 0;
		for (int i = 0; i < 8; i++) {
			if (bits[i] == true) {
				value = value | (1 << i);
			}
		}
		return (byte) value;
	}

	public static boolean[] byteToBits(byte b) {
		boolean[] bits = new boolean[8];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = ((b & (1 << i)) != 0);
		}
		return bits;
	}

	public static String toString(BitSet bits) {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			out.append(bits.get(i) ? "1" : "0");
		}
		return out.toString();
	}

	/*public static String getLocalHost() {
		String ip = StringUtil.EMPTY;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("Error al intentar obtener ip local", e);
		}
		return "172.29.81.51";
		//return ip;
	}*/


}
