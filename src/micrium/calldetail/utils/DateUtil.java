package micrium.calldetail.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Months;

public class DateUtil extends org.apache.commons.lang.time.DateUtils {

	public static final String FORMAT_DATE_YMD = "yyyyMMdd";

	public static final String FORMAT_DATE_GUION_YMD = "yyyy-MM-dd";
	public static final String FORMAT_DATE_SPLASH_YMD = "yyyy/MM/dd";

	public static final String FORMAT_DATETIME_GUION_YMDHMS_24 = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DATETIME_GUION_YMDHM_24 = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_DATETIME_GUION_YMDH_24 = "yyyy-MM-dd HH";

	public static final String FORMAT_DATETIME_GUION_YMDHMS_12 = "yyyy-MM-dd hh:mm:ss";
	public static final String FORMAT_DATETIME_GUION_YMDHM_12 = "yyyy-MM-dd hh:mm";
	public static final String FORMAT_DATETIME_GUION_YMDH_12 = "yyyy-MM-dd hh";

	public static final String FORMAT_DATETIME_GUION_DMYHMS_24 = "dd-MM-yyyy HH:mm:ss";
	public static final String FORMAT_DATETIME_GUION_DMYHM_24 = "dd-MM-yyyy HH:mm";
	public static final String FORMAT_DATETIME_GUION_DMYH_24 = "dd-MM-yyyy HH";

	public static final String FORMAT_DATETIME_GUION_DMYHMS_12 = "dd-MM-yyyy hh:mm:ss";
	public static final String FORMAT_DATETIME_GUION_DMYHM_12 = "dd-MM-yyyy hh:mm";
	public static final String FORMAT_DATETIME_GUION_DMYH_12 = "dd-MM-yyyy hh";

	public static final String FORMAT_DATE_GUION_DMY = "dd-MM-yyyy";

	public static final String FORMAT_DATETIME_SPLASH_YMDHMS_24 = "yyyy/MM/dd HH:mm:ss";
	public static final String FORMAT_DATETIME_SPLASH_YMDHM_24 = "yyyy/MM/dd HH:mm";
	public static final String FORMAT_DATETIME_SPLASH_YMDH_24 = "yyyy/MM/dd HH";

	public static final String FORMAT_DATETIME_SPLASH_YMDHMS_12 = "yyyy/MM/dd hh:mm:ss";
	public static final String FORMAT_DATETIME_SPLASH_YMDHM_12 = "yyyy/MM/dd hh:mm";
	public static final String FORMAT_DATETIME_SPLASH_YMDH_12 = "yyyy/MM/dd hh";

	public static final String FORMAT_DATETIME_SPLASH_DMYHMS_24 = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMAT_DATETIME_SPLASH_DMYHM_24 = "dd/MM/yyyy HH:mm";
	public static final String FORMAT_DATETIME_SPLASH_DMYH_24 = "dd/MM/yyyy HH";

	public static final String FORMAT_DATETIME_SPLASH_DMYHMS_12 = "dd/MM/yyyy hh:mm:ss";
	public static final String FORMAT_DATETIME_SPLASH_DMYHM_12 = "dd/MM/yyyy hh:mm";
	public static final String FORMAT_DATETIME_SPLASH_DMYH_12 = "dd/MM/yyyy hh";

	public static final String FORMAT_DATE_SPLASH_DMY = "dd/MM/yyyy";

	public static final String FORMAT_TIME_HMS = "HHmmss";
	public static final String FORMAT_TIME_HMS_24 = "HH:mm:ss";
	public static final String FORMAT_TIME_HMS_12 = "hh:mm:ss";
	public static final String END_TIME = "23:59:59";
	public static final String BEGIN_TIME = "00:00:00";

	private static TimeZone timezone = TimeZone.getTimeZone(Parameter.SYSTEM_TIMEZONE);

	// private static TimeZone timezone = TimeZone.getTimeZone("GMT-4");

	// DATE TO STRING

	public static String dateToStringHMS_24(Date date) {
		return dateToString(date, FORMAT_TIME_HMS_24);
	}

	public static String dateToStringHMS_12(Date date) {
		return dateToString(date, FORMAT_TIME_HMS_12);
	}

	public static String dateToStringSDMY(Date date) {
		return dateToString(date, FORMAT_DATE_SPLASH_DMY);
	}

	public static String dateToStringSDMYHMS_24(Date date) {
		return dateToString(date, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static String dateToStringSDMYHMS_12(Date date) {
		return dateToString(date, FORMAT_DATETIME_SPLASH_DMYHMS_12);
	}

	public static String dateToStringGDMY(Date date) {
		return dateToString(date, FORMAT_DATE_GUION_DMY);
	}

	public static String dateToStringGDMYHMS_24(Date date) {
		return dateToString(date, FORMAT_DATETIME_GUION_DMYHMS_24);
	}

	public static String dateToStringGDMYHMS_12(Date date) {
		return dateToString(date, FORMAT_DATETIME_GUION_DMYHMS_12);
	}

	public static String dateToString(Date fecha, String format) {
		if (fecha == null) {
			return StringUtil.EMPTY;
		}
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(timezone);
		return formatter.format(fecha);
	}

	public static String dateToStringYMD(Date date) {
		if (date == null) {
			return StringUtil.EMPTY;
		}
		DateFormat formatter = new SimpleDateFormat(FORMAT_DATE_YMD);
		return formatter.format(date);
	}

	public static String dateToStringHHMMSS(Date date) {
		if (date == null) {
			return StringUtil.EMPTY;
		}
		DateFormat formatter = new SimpleDateFormat(FORMAT_TIME_HMS);
		return formatter.format(date);
	}

	// CALENDAR TO STRING

	public static String calendarToStringHMS_24(Calendar calendar) {
		return calendarToString(calendar, FORMAT_TIME_HMS_24);
	}

	public static String calendarToStringHMS_12(Calendar calendar) {
		return calendarToString(calendar, FORMAT_TIME_HMS_12);
	}

	public static String calendarToStringSDMY(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATE_SPLASH_DMY);
	}

	public static String calendarToStringSDMYHMS_24(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static String calendarToStringSDMYHMS_12(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATETIME_SPLASH_DMYHMS_12);
	}

	public static String calendarToStringGDMY(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATE_GUION_DMY);
	}

	public static String calendarToStringGDMYHMS_24(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATETIME_GUION_DMYHMS_24);
	}

	public static String calendarToStringGDMYHMS_12(Calendar calendar) {
		return calendarToString(calendar, FORMAT_DATETIME_GUION_DMYHMS_12);
	}

	public static String calendarToString(Calendar calendar, String format) {
		return dateToString(calendar.getTime(), format);
	}

	// TIMESTAMP TO STRING

	public static String timetampToStringHMS_24(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_TIME_HMS_24);
	}

	public static String timetampToStringHMS_12(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_TIME_HMS_12);
	}

	public static String timetampToStringSDMY(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATE_SPLASH_DMY);
	}

	public static String timetampToStringSDMYHMS_24(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static String timetampToStringSDMYHMS_12(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATETIME_SPLASH_DMYHMS_12);
	}

	public static String timetampToStringYMD(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATE_GUION_YMD);
	}

	public static String timetampToStringGDMYHMS_24(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static String timetampToStringGDMYHMS_12(Timestamp timestamp) {
		return timetampToString(timestamp, FORMAT_DATETIME_GUION_YMDHMS_12);
	}

	public static String timetampToString(Timestamp fecha, String format) {
		if (fecha == null) {
			return StringUtil.EMPTY;
		}
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setTimeZone(timezone);
		return formatter.format(fecha);
	}

	// STRING TO DATE

	public static Date stringSDMYToDate(String dateString) {
		return stringToDate(dateString, FORMAT_DATE_SPLASH_DMY);
	}

	public static Date stringSDMYHMS_24ToDate(String datetimeString) {
		return stringToDate(datetimeString, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static Date stringSYMDToDate(String dateString) {
		return stringToDate(dateString, FORMAT_DATE_SPLASH_YMD);
	}

	public static Date stringSYMDHMS_24ToDate(String datetimeString) {
		return stringToDate(datetimeString, FORMAT_DATETIME_SPLASH_YMDHMS_24);
	}

	public static Date stringGYMDToDate(String dateString) {
		return stringToDate(dateString, FORMAT_DATE_GUION_YMD);
	}

	public static Date stringGYMDHMS_24ToDate(String datetimeString) {
		return stringToDate(datetimeString, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Date stringGDMYToDate(String dateString) {
		return stringToDate(dateString, FORMAT_DATE_GUION_DMY);
	}

	public static Date stringGDMYHMS_24ToDate(String datetimeString) {
		return stringToDate(datetimeString, FORMAT_DATETIME_GUION_DMYHMS_24);
	}

	public static Date stringToDate(String dateString, String format) {
		Calendar calendar = stringToCalendar(dateString, format);
		if (calendar == null) {
			return null;
		}

		return new Date(calendar.getTimeInMillis());
	}

	// STRING TO CALENDAR

	public static Calendar stringSYMDToCalendar(String dateString) {
		return stringToCalendar(dateString, FORMAT_DATE_SPLASH_YMD);
	}

	public static Calendar stringSYMDHMS_24ToCalendar(String datetimeString) {
		return stringToCalendar(datetimeString, FORMAT_DATETIME_SPLASH_YMDHMS_24);
	}

	public static Calendar stringSDMYToCalendar(String dateString) {
		return stringToCalendar(dateString, FORMAT_DATE_SPLASH_DMY);
	}

	public static Calendar stringSDMYHMS_24ToCalendar(String datetimeString) {
		return stringToCalendar(datetimeString, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static Calendar stringGYMDToCalendar(String dateString) {
		return stringToCalendar(dateString, FORMAT_DATE_GUION_YMD);
	}

	public static Calendar stringGYMDHMS_24ToCalendar(String datetimeString) {
		return stringToCalendar(datetimeString, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Calendar stringGDMYToCalendar(String dateString) {
		return stringToCalendar(dateString, FORMAT_DATE_GUION_DMY);
	}

	public static Calendar stringGDMYHMS_24ToCalendar(String datetimeString) {
		return stringToCalendar(datetimeString, FORMAT_DATETIME_GUION_DMYHMS_24);
	}

	public static Calendar stringToCalendar(String dateString, String format) {
		Calendar cal = null;

		if (StringUtil.isEmpty(dateString.trim())) {
			return null;
		}

		try {
			DateFormat formatter = new SimpleDateFormat(format);
			Date date = (Date) formatter.parse(dateString);
			cal = Calendar.getInstance();
			cal.setTimeZone(timezone);
			cal.setTime(date);
		} catch (ParseException e) {
			System.out.println(e);
		}

		return cal;
	}

	// STRING TO TIMESTAMP

	public static Timestamp stringSYMDToTimestamp(String dateString) {
		return stringToTimestamp(dateString, FORMAT_DATE_SPLASH_YMD);
	}

	public static Timestamp stringSYMDHMS_24ToTimestamp(String datetimeString) {
		return stringToTimestamp(datetimeString, FORMAT_DATETIME_SPLASH_YMDHMS_24);
	}

	public static Timestamp stringSDMYToTimestamp(String dateString) {
		return stringToTimestamp(dateString, FORMAT_DATE_SPLASH_DMY);
	}

	public static Timestamp stringSDMYHMS_24ToTimestamp(String datetimeString) {
		return stringToTimestamp(datetimeString, FORMAT_DATETIME_SPLASH_DMYHMS_24);
	}

	public static Timestamp stringGYMDToTimestamp(String dateString) {
		return stringToTimestamp(dateString, FORMAT_DATE_GUION_YMD);
	}

	public static Timestamp stringGYMDHMS_24ToTimestamp(String datetimeString) {
		return stringToTimestamp(datetimeString, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Timestamp stringGDMYToTimestamp(String dateString) {
		return stringToTimestamp(dateString, FORMAT_DATE_GUION_DMY);
	}

	public static Timestamp stringGDMYHMS_24ToTimestamp(String datetimeString) {
		return stringToTimestamp(datetimeString, FORMAT_DATETIME_GUION_DMYHMS_24);
	}

	public static Timestamp stringToTimestamp(String dateString, String format) {
		Calendar calendar = stringToCalendar(dateString, format);
		if (calendar == null) {
			return null;
		}
		return new Timestamp(calendar.getTimeInMillis());
	}

	// CALENDAR

	public static Calendar dateToCalendar(Date date) {
		if (date == null) {
			return null;
		}

		Calendar cal = null;
		try {
			cal = Calendar.getInstance();
			cal.setTimeZone(timezone);
			cal.setTime(date);
		} catch (Exception e) {
			System.out.println(e);
		}
		return cal;
	}

	public static Calendar timestampToCalendar(Timestamp time) {
		if (time == null) {
			return null;
		}
		Date date = new Date(time.getTime());
		return dateToCalendar(date);
	}

	// DATE TO TIMESTAMP

	public static Timestamp dateToTimetamp(Date date) {
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	public static Timestamp dateToTimetamp(Date date, String format) {
		if (date == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(format);
		String str = formatter.format(date);
		return stringToTimestamp(str, format);
	}

	public static Timestamp dateToTimetampEndTime(Date date) {
		return dateToTimetampEndTime(date, FORMAT_DATE_GUION_YMD);
	}

	public static Timestamp dateToTimetampEndTime(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date) + " " + END_TIME;
		return stringToTimestamp(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Timestamp dateToTimetampBeginTime(Date date) {
		return dateToTimetampBeginTime(date, FORMAT_DATE_GUION_YMD);
	}

	public static Timestamp dateToTimetampBeginTime(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date) + " " + BEGIN_TIME;
		return stringToTimestamp(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	// TIMESTAMP TO DATE

	public static Date timestampToDate(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}
		Date date = new Date(timestamp.getTime());
		return date;
	}

	public static Date timetampToDateEndTime(Timestamp timestamp) {
		return timetampEndTime(timestamp, FORMAT_DATE_GUION_YMD);
	}

	public static Date timetampToDateEndTime(Timestamp timestamp, String dateFormat) {
		if (timestamp == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(timestamp) + " " + END_TIME;
		return stringToDate(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Date timetampToDateBeginTime(Timestamp timestamp) {
		return timetampBeginTime(timestamp, FORMAT_DATE_GUION_YMD);
	}

	public static Date timetampToDateBeginTime(Timestamp timestamp, String dateFormat) {
		if (timestamp == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(timestamp) + " " + BEGIN_TIME;
		return stringToDate(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	// TIMESTAMP TO TIMESTAMP

	public static Timestamp timetampBeginTime(Timestamp timestamp) {
		return timetampBeginTime(timestamp, FORMAT_DATE_GUION_YMD);
	}

	public static Timestamp timetampBeginTime(Timestamp timestamp, String dateFormat) {
		if (timestamp == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(timestamp) + " " + BEGIN_TIME;
		return stringToTimestamp(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Timestamp timetampEndTime(Timestamp timestamp) {
		return timetampEndTime(timestamp, FORMAT_DATE_GUION_YMD);
	}

	public static Timestamp timetampEndTime(Timestamp timestamp, String dateFormat) {
		if (timestamp == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(timestamp) + " " + END_TIME;
		return stringToTimestamp(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Timestamp calendarToTimestamp(Calendar cal) {
		return dateToTimetamp(cal.getTime());
	}

	// DATE TO DATE

	public static Date dateWithTimeAtual(Date date) {
		return date1WithTimeOfDate2(date, new Date());
	}

	public static Date date1WithTimeOfDate2(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		Calendar cal_actual = Calendar.getInstance();
		cal_actual.setTime(date2);
		cal.set(Calendar.HOUR_OF_DAY, cal_actual.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal_actual.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal_actual.get(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date dateEndTime(Date date) {
		return dateEndTime(date, FORMAT_DATE_GUION_YMD);
	}

	public static Date dateEndTime(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date) + " " + END_TIME;
		return stringToDate(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Date dateBeginTime(Date date) {
		return dateBeginTime(date, FORMAT_DATE_GUION_YMD);
	}

	public static Date dateBeginTime(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		String str = formatter.format(date) + " " + BEGIN_TIME;
		return stringToDate(str, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static Time stringHMSToTime(String timeString) {
		if (StringUtil.isEmpty(timeString.trim())) {
			return null;
		}
		return Time.valueOf(timeString);
	}

	public static boolean isMayor(Date fMayor, Date fMenor) {
		Calendar fa = Calendar.getInstance();
		fa.setTime(fMayor);

		Calendar fb = Calendar.getInstance();
		fb.setTime(fMenor);

		return fa.after(fb);
	}

	public static boolean isMayorIgual(Date fMayor, Date fMenor) {
		Calendar fa = Calendar.getInstance();
		fa.setTime(fMayor);

		Calendar fb = Calendar.getInstance();
		fb.setTime(fMenor);

		return fa.after(fb) || fa.equals(fb);
	}

	public static boolean isMenor(Date fMenor, Date fMayor) {
		Calendar fa = Calendar.getInstance();
		fa.setTime(fMenor);

		Calendar fb = Calendar.getInstance();
		fb.setTime(fMayor);

		return fa.before(fb);
	}

	public static boolean isMenorIgual(Date fMenor, Date fMayor) {
		Calendar fa = Calendar.getInstance();
		fa.setTime(fMenor);

		Calendar fb = Calendar.getInstance();
		fb.setTime(fMayor);

		return fa.before(fb) || fa.equals(fb);
	}

	public static boolean isIgual(Date fMenor, Date fMayor) {
		Calendar fa = Calendar.getInstance();
		fa.setTime(fMenor);

		Calendar fb = Calendar.getInstance();
		fb.setTime(fMayor);

		return fa.equals(fb);
	}

	public static int diferenciaDias(Date fInicial, Date fFinal) {
		Calendar ci = Calendar.getInstance();
		ci.setTime(fInicial);

		Calendar cf = Calendar.getInstance();
		cf.setTime(fFinal);

		long diferencia = cf.getTimeInMillis() - ci.getTimeInMillis();

		return (int) Math.ceil((double) diferencia / (1000 * 60 * 60 * 24));
	}

	public static int getMaximunDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getMinimumDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.getActualMinimum(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static boolean isMaximunDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH);
	}

	public static Date getFirstDateOfMes() {
		Calendar cal = Calendar.getInstance();

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.getMinimum(Calendar.HOUR_OF_DAY),
				cal.getMinimum(Calendar.MINUTE), cal.getMinimum(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date dateToMaximunDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date dateToMinimunDayOfMonth(Date date) {
		Calendar cal = dateToCalendar(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
		return cal.getTime();
	}

	public static Date getLastDateOfMes() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getActualMaximum(Calendar.DAY_OF_MONTH), cal.getMaximum(Calendar.HOUR_OF_DAY),
				cal.getMaximum(Calendar.MINUTE), cal.getMaximum(Calendar.SECOND));
		return cal.getTime();
	}

	// SUMA Y RESTA DE MES

	public static Date sumarMeses(Date fecha, int meses) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(fecha);
		c.add(Calendar.MONTH, meses);
		return c.getTime();
	}

	public static Date restarMeses(Date date, int meses) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(Calendar.MONTH, -meses);
		return c.getTime();
	}

	public static Date sumarMesesCabales(Date date, int meses) {
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTimeZone(timezone);
		c1.setTime(date);
		c1.add(Calendar.MONTH, meses);
		Calendar c2 = dateToCalendar(date);
		return (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) ? c1.getTime() : null;
	}

	// SUMA Y RESTA DE DIA

	public static Date sumarDias(Date date, int dias) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(Calendar.DATE, dias);
		return c.getTime();
	}

	public static Date restarDias(Date date, int dias) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(Calendar.DATE, -dias);
		return c.getTime();
	}

	public static Date restarDias(Timestamp timestamp, int dias) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(timestamp);
		c.add(Calendar.DATE, -dias);
		return c.getTime();
	}

	// SUMA Y RESTA DE HORA

	public static Date sumarHoras(Date date, int horas) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.HOUR_OF_DAY, horas);
		return c.getTime();
	}

	public static Date restarHoras(Date date, int horas) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.HOUR_OF_DAY, -horas);
		return c.getTime();
	}

	// SUMA Y RESTA DE MINUTO

	public static Date sumarMinutos(Date date, int minutos) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.MINUTE, minutos);
		return c.getTime();
	}

	public static Date restarMinutos(Date date, int minutos) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.MINUTE, -minutos);
		return c.getTime();
	}

	// SUMA Y RESTA DE SEGUNDO

	public static Date sumarSegundos(Date date, int segundos) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.SECOND, segundos);
		return c.getTime();
	}

	public static Date restarSegundos(Date date, int segundos) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(timezone);
		c.setTime(date);
		c.add(GregorianCalendar.SECOND, -segundos);
		return c.getTime();
	}

	public static String getDateActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat(FORMAT_DATE_GUION_YMD);
		return formateador.format(ahora);
	}

	public static String getHoraActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat(FORMAT_TIME_HMS_24);
		return formateador.format(ahora);
	}

	public static Date getDate() {
		return new Date();
	}

	public static Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static int diferenciaMeses(Date fInicial, Date fFinal) {
		DateTime dateTimeInicial = new DateTime(fInicial.getTime());
		DateTime dateTimeFinal = new DateTime(fFinal.getTime());
		Months d = Months.monthsBetween(dateTimeInicial, dateTimeFinal);
		return d.getMonths();
	}

	public static String XMLGregorianCalendarToString(XMLGregorianCalendar xgc, String format) {
		String response = "";

		Date date = xgc.toGregorianCalendar().getTime();
		if (date != null) {
			response = dateToString(date, format);
		}

		return response;
	}

	// yyyy-MM-dd HH:mm:ss

	public static XMLGregorianCalendar stringToXMLGregorianCalendar(String strDate) {
		return stringToXMLGregorianCalendar(strDate, FORMAT_DATETIME_GUION_YMDHMS_24);
	}

	public static XMLGregorianCalendar stringToXMLGregorianCalendar(String strDate, String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
		DatatypeFactory dtf;
		XMLGregorianCalendar xgcal = null;
		try {
			dtf = DatatypeFactory.newInstance();
			Date date = DATE_FORMAT.parse(strDate);
			cal.setTime(date);
			xgcal = dtf.newXMLGregorianCalendar();
			xgcal.setYear(cal.get(Calendar.YEAR));
			xgcal.setDay(cal.get(Calendar.DAY_OF_MONTH));
			xgcal.setMonth(cal.get(Calendar.MONTH) + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xgcal;
	}

	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		DatatypeFactory dtf;
		XMLGregorianCalendar xgcal = null;
		try {
			dtf = DatatypeFactory.newInstance();
			cal.setTime(date);
			xgcal = dtf.newXMLGregorianCalendar();
			xgcal.setYear(cal.get(Calendar.YEAR));
			xgcal.setDay(cal.get(Calendar.DAY_OF_MONTH));
			xgcal.setMonth(cal.get(Calendar.MONTH) + 1);
			xgcal.setHour(cal.get(Calendar.HOUR_OF_DAY));
			xgcal.setMinute(cal.get(Calendar.MINUTE));
			xgcal.setSecond(cal.get(Calendar.SECOND));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xgcal;
	}

	public static Long segundoToSemana(long segundo) {
		return segundo / (7 * 24 * 60 * 60);
	}

	public static Long segundoToDia(long segundo) {
		return segundo / (24 * 60 * 60);
	}

	public static Long segundoToHora(long segundo) {
		return segundo / (60 * 60);
	}

	public static Long segundoToMinuto(long segundo) {
		return segundo / (60);
	}

	public static Long milisegundoToSemana(long milesegundo) {
		return milesegundo / (7 * 24 * 60 * 60 * 1000);
	}

	public static Long milisegundoToDia(long milesegundo) {
		return milesegundo / (24 * 60 * 60 * 1000);
	}

	public static Long milisegundoToHora(long milesegundo) {
		return milesegundo / (60 * 60 * 1000);
	}

	public static Long milisegundoToMinuto(long milesegundo) {
		return milesegundo / (60 * 1000);
	}

	public static Long milisegundoToSegundo(long milesegundo) {
		return milesegundo / (1000);
	}

	public static Long semanaToMilisegundo(long semana) {
		return semana * 7 * 24 * 60 * 60 * 1000;
	}

	public static Long diaToMilisegundo(long dia) {
		return dia * 24 * 60 * 60 * 1000;
	}

	public static Long horaToMilisegundo(long hora) {
		return hora * 60 * 60 * 1000;
	}

	public static Long minutoToMilisegundo(long minuto) {
		return minuto * 60 * 1000;
	}

	public static Long segundoToMilisegundo(long segundo) {
		return segundo * 1000;
	}

	public static Long semanaToHora(long semana) {
		return semana * 7 * 24;
	}

	public static Long diaToHora(long dia) {
		return dia * 24;
	}

	public static Long semanaToSegundo(long semana) {
		return semana * 7 * 24 * 60 * 60;
	}

	public static Long siaToSegundo(long dia) {
		return dia * 24 * 60 * 60;
	}

	public static Long horaToSegundo(long hora) {
		return hora * 60 * 60;
	}

	public static Long minutoToSegundo(long minuto) {
		return minuto * 60;
	}

	/*public static void main(String[] args) {
		//System.out.println(timezone);
		//Date s= new Date( 2014,8,11);
		java.util.Date s= new Date();
		s.setYear(2014);
		s.setMonth(8);
		s.setDate(11);
		XMLGregorianCalendar m= dateToXMLGregorianCalendar(s);
		System.out.println(m.getYear());
		System.out.println(m.getMonth());
		System.out.println(m.getDay());
		
	}*/

}
