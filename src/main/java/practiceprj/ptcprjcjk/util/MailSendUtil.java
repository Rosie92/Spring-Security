package practiceprj.ptcprjcjk.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class MailSendUtil {
	
	final String EMAIL_ID="toonivie0613@gmail.com";//sw@ideaconcert.com
	final String EMAIL_PW="toon123!@"; //password root4477
	final String EMAIL_SENDER_NAME="TOONFLIX";
	
	int size;
	
	@Value("classpath:templates/mail/mail_auth.html")
	private Resource authMail;
	
	@Value("classpath:templates/mail/mail_forgot_password.html")
	private Resource findPwMail;
	
	
	
	private HtmlEmail initEmailSender() {
		HtmlEmail initEmail = new HtmlEmail();
		try {
		
		initEmail.setCharset("UTF-8");
		initEmail.setHostName("smtp.gmail.com");//smtp.worksmobile.com
		
		initEmail.setAuthentication(EMAIL_ID, EMAIL_PW);
		initEmail.setSmtpPort(587);
		initEmail.setDebug(false);
		initEmail.setStartTLSEnabled(true);
		initEmail.setSSLOnConnect(true);
		initEmail.setSSLCheckServerIdentity(true);
		initEmail.getMailSession().getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
		initEmail.getMailSession().getProperties().put("mail.smtp.ssl.protocols", "TLSv1.2");
		initEmail.getMailSession().getProperties().put("mail.smtps.port", "587");
		initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.port", "587");
		initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
		initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
		initEmail.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");
		
		} catch (EmailException e) {
			e.printStackTrace();
		}
		
		return initEmail;
	}

	private MultiPartEmail initMultipartEmailSender() {
		MultiPartEmail initEmail = new MultiPartEmail();
		try {

			initEmail.setCharset("UTF-8");
			initEmail.setHostName("smtp.gmail.com");//smtp.worksmobile.com

			initEmail.setAuthentication(EMAIL_ID, EMAIL_PW);
			initEmail.setSmtpPort(587);
			initEmail.setDebug(false);
			initEmail.setStartTLSEnabled(true);
			initEmail.setSSLOnConnect(true);
			initEmail.setSSLCheckServerIdentity(true);
			initEmail.getMailSession().getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
			initEmail.getMailSession().getProperties().put("mail.smtp.ssl.protocols", "TLSv1.2");
			initEmail.getMailSession().getProperties().put("mail.smtps.port", "587");
			initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.port", "587");
			initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.class",   "javax.net.ssl.SSLSocketFactory");
			initEmail.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback", "false");
			initEmail.getMailSession().getProperties().put("mail.smtp.starttls.enable", "true");

		} catch (EmailException e) {
			e.printStackTrace();
		}

		return initEmail;
	}

	public String sendAuthEmail(String emailTo, String subject, String hashKey) {
		String message = createMail(authMail).replace("%change", hashKey);
		return sendEmail(emailTo, subject, message);
	}
	public String sendFindPwEmail(String emailTo, String subject, String hashKey) {
		String message = createMail(findPwMail).replace("%change", 
				"<a href='http://183.111.234.51:8078/member/changepassword?authKey="+hashKey+"' target='_blank'>비밀번호 변경</a>");
		return sendEmail(emailTo, subject, message);
	}
	public String sendFileEmail(String emailTo, String subject, String msg, MultipartFile multiPartFile) throws IOException {
		File convFile = new File(multiPartFile.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multiPartFile.getBytes());
		fos.close();


		String resultStr = sendMultiPartEmail(emailTo, subject, msg, convFile);
		convFile.delete();
		return resultStr;
	}
	
	private String createMail(Resource mail) {
		StringBuilder mailBuilder = new StringBuilder();
		try {
			InputStream in = mail.getInputStream();
			Reader reader = new InputStreamReader(in);
			BufferedReader buffReader = new BufferedReader(reader);
			
			//BufferedReader in = new BufferedReader(new FileReader(mail.getFile()));
//			String s;
//			while((s = in.readLine())!=null) {
//				System.out.println(s);
//				mailBuilder.append(s);
//			}
			int ch;
			while((ch = buffReader.read()) != -1) {
				mailBuilder.append((char)ch);
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return mailBuilder.toString();
	}
	
	private String sendEmail(String emailTo, String subject, String msg) {
		HtmlEmail sendemail = initEmailSender();
		try {
			sendemail.addTo(emailTo);
			sendemail.setSubject(subject);
			sendemail.setFrom(EMAIL_ID, EMAIL_SENDER_NAME);
			sendemail.setHtmlMsg(msg);
			System.out.println("===========================setHtmlMsg========================");
			sendemail.send();
		} catch (EmailException e1) {
			e1.printStackTrace();
			System.out.println("NOT OK");
			return "NOT OK";
		}
		return "OK";
	}

	private String sendMultiPartEmail(String emailTo, String subject, String msg, File file) {
		HtmlEmail sendemail = initEmailSender();
		try {
			sendemail.addTo(emailTo);
			sendemail.setSubject(subject);
			sendemail.setFrom(EMAIL_ID, EMAIL_SENDER_NAME);
			sendemail.setHtmlMsg(msg);
			sendemail.attach(file);
			System.out.println("===========================setHtmlMsg========================");
			sendemail.send();
		} catch (EmailException e1) {
			e1.printStackTrace();
			System.out.println("NOT OK");
			return "NOT OK";
		}
		return "OK";
	}
}
