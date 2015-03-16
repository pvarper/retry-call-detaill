package micrium.calldetail.mail;

import java.util.List;

import micrium.calldetail.bussines.SysParameter;
import micrium.calldetail.utils.BooleanUtil;
import micrium.calldetail.utils.NumberUtil;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailConfig extends HtmlEmail {

	public EmailConfig() throws EmailException {
		String mailSmtlHost = SysParameter.getProperty(SysParameter.MAIL_SMTP_HOST);
		String mailSmtlUserName = SysParameter.getProperty(SysParameter.MAIL_SMTP_USER_NAME);
		int mailSmtlPort = NumberUtil.toInt(SysParameter.getProperty(SysParameter.MAIL_SMTP_PORT));
		boolean mailSmtlAuth = BooleanUtil.toBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_AUTH));
		boolean mailSmtlStarttlsEnable = BooleanUtil.toBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_STARTTLS_ENABLE));
		boolean mailSmtlSslEnable = BooleanUtil.toBoolean(SysParameter.getProperty(SysParameter.MAIL_SMTP_SSL_ENABLE));
		String mailSmtlUser = SysParameter.getProperty(SysParameter.MAIL_SMTP_USER);
		String mailSmtlPassword = SysParameter.getProperty(SysParameter.MAIL_SMTP_PASSWORD);
		String mailSmtlFrom = SysParameter.getProperty(SysParameter.MAIL_SMTP_FROM);

		setCharset("UTF-8");
		setHostName(mailSmtlHost);
		setSmtpPort(mailSmtlPort);

		if (mailSmtlAuth) {
			// setAuthentication(mailSmtlUser, mailSmtlPassword);
			setAuthenticator(new DefaultAuthenticator(mailSmtlUser, mailSmtlPassword));
		}

		setSSLOnConnect(mailSmtlSslEnable);
		setStartTLSEnabled(mailSmtlStarttlsEnable);

		addFrom(mailSmtlFrom, mailSmtlUserName);
	}

	public void addFrom(String mailSmtlUser, String mailSmtlUserName) throws EmailException {
		setFrom(mailSmtlUser, mailSmtlUserName);
	}

	public void agregarDestinatarios(List<String> destinos, List<String> copias, List<String> ocultos) throws EmailException {
		if (destinos != null) {
			for (String emailDst : destinos) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addTo(emailDst);
				// }
			}
		}

		if (copias != null) {
			for (String emailDst : copias) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addCc(emailDst);
				// }
			}
		}

		if (ocultos != null) {
			for (String emailDst : ocultos) {
				// if (EmailUtils.isEmailValid(emailDst)) {
				addBcc(emailDst);
				// }
			}
		}
	}

}
