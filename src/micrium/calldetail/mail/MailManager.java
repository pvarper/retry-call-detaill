package micrium.calldetail.mail;

import java.util.List;

import micrium.calldetail.result.Code;
import micrium.calldetail.result.Result;
import micrium.calldetail.utils.FileUtil;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class MailManager {

	private static Logger log = Logger.getLogger(MailManager.class);

	public static Result sendEmailDetalle(Mail mail) {
		log.info("Vamos a enviar el mail " + mail);
		Result result= new Result();
		try {
			String pathname = mail.getAttachment().getPath();
			Mailing mailing = new Mailing();
			result=mailing.sendMail(mail.getSubject(), mail.getContent(), mail.getAttachment(), mail.getLstEmailsTo(), mail.getLstEmailsCc(),
					mail.getLstEmailsCco());
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el mail " + mail);
				result.error("No se envio el mail " + mail);
				return result;
			}	
			FileUtil.delete(pathname);
			log.info("Se envio satisfactoriamente el mail " + mail);
			result.ok("Se envio satisfactoriamente el mail " + mail);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("No se envio el mail " + mail);
			result.error("No se envio el mail " + mail);
			return result;
		}
		
	}

	public static Result sendEmailDetalle2(Mail mail) {
		log.info("Vamos a enviar el mail " + mail);
		Result result= new Result();
		try {
			//String pathname = mail.getAttachment().getPath();
			Mailing mailing = new Mailing();
			result=mailing.sendMail2(mail.getSubject(), mail.getContent(), mail.getLstEmailsTo(), mail.getLstEmailsCc(),
					mail.getLstEmailsCco());
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el mail " + mail);
				result.error("No se envio el mail " + mail);
				return result;
			}	
			//FileUtil.delete(pathname);
			log.info("Se envio satisfactoriamente el mail " + mail);
			result.ok("Se envio satisfactoriamente el mail " + mail);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("No se envio el mail " + mail);
			result.error("No se envio el mail " + mail);
			return result;
		}
		
	}
	public static Mail getMail(String path, String subject, String content, List<String> lstEmailsTo) {

		Mail mail = new Mail();
		Attachment attachment = new Attachment();
		attachment.setPath(path);
		String name = FilenameUtils.getName(path);
		attachment.setName(name);
		mail.setAttachment(attachment);

		mail.setLstEmailsTo(lstEmailsTo);
		mail.setSubject(subject);
		mail.setContent(content);

		return mail;
	}
	public static Mail getMail2(String subject, String content, List<String> lstEmailsTo) {

		Mail mail = new Mail();
		mail.setLstEmailsTo(lstEmailsTo);
		mail.setSubject(subject);
		mail.setContent(content);

		return mail;
	}
	public static Result sendEmailIntentos(Mail mail) {
		log.info("Vamos a enviar el mail " + mail);

		Result result= new Result();
		try {
			String pathname = mail.getAttachment().getPath();
			Mailing mailing = new Mailing();
			result=mailing.sendMail(mail.getSubject(), mail.getContent(), mail.getAttachment(), mail.getLstEmailsTo(), mail.getLstEmailsCc(),
					mail.getLstEmailsCco());
			if (!result.getCode().equalsIgnoreCase(Code.OK)) {
				log.info("No se envio el mail " + mail);
				result.error("No se envio el mail " + mail);
				log.error(result.getDescription());
				return result;
			}

			FileUtil.delete(pathname);
			log.info("Se envio satisfactoriamente el mail " + mail);
			result.ok("Se envio satisfactoriamente el mail " + mail);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.info("No se envio el mail " + mail);
			result.error("No se envio el mail " + mail);
			log.error(result.getDescription());
			return result;
		}
	}
	public static Mail getMailIntentos(String subject, String content, List<String> lstEmailsTo) {

		Mail mail = new Mail();
		mail.setLstEmailsTo(lstEmailsTo);
		mail.setSubject(subject);
		mail.setContent(content);

		return mail;
	}

}
