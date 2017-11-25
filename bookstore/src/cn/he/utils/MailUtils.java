package cn.he.utils;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 163和qq不太一样，配置稍有不同
 * @author He
 *
 */
public class MailUtils {

	/**
	 * 发送邮件
	 * @param host	主机名称，如：smtp.163.com
	 * @param username	登录用户，不用带艾特后面的内容
	 * @param password	登录密码
	 * @param from	发件人
	 * @param to	收件人列表，可以在邮箱地址后加?to或者?bcc或者?cc或者to或者不加，使用默认发送
	 * @param subject	主题
	 * @param content	正文
	 * @param files		附件列表
	 */
	public static void sendMails(String host, final String username, final String password,
			String from, String to, String subject, String content, List<File> files) {
		Properties props = new Properties();
		props.setProperty("mail.host", host);
		props.setProperty("mail.smtp.auth", "true");
		
		if (host.contains("qq")) {
			props.setProperty("mail.smtp.port", "465");
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
			
		Authenticator author = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getInstance(props, author);

		try {
			Transport.send(createMimeMessage(session, from, to, subject, content, files));
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 创建MimeMessage对象
	 * @param session
	 * @param from
	 * @param to
	 * @param subject
	 * @param content
	 * @param files
	 * @return
	 */
	private static MimeMessage createMimeMessage(Session session, String from, String to,
			String subject, String content, List<File> files) {
		
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			
			if (to.contains(",")) {
				String[] toArr = to.split(",");
				for (int i = 0; i < toArr.length; i++ ) {
					if (toArr[i].contains("\\?")) {
						String[] innerArr = toArr[i].split("[?]");
						if (innerArr[1].trim().equalsIgnoreCase("to")) {
							msg.setRecipients(RecipientType.TO, innerArr[0]);
						} else if (innerArr[1].trim().equalsIgnoreCase("bcc")) {
							msg.setRecipients(RecipientType.BCC, innerArr[0]);
						} else if (innerArr[1].trim().equalsIgnoreCase("cc")) {
							msg.setRecipients(RecipientType.CC, innerArr[0]);
						}
					} else {
						msg.setRecipients(RecipientType.TO, toArr[i]);
					}
				}
			} else {
				if (to.contains("\\?")) {
					String[] innerArr = to.split("[?]");
					if (innerArr[1].trim().equalsIgnoreCase("to")) {
						msg.setRecipients(RecipientType.TO, innerArr[0]);
					} else if (innerArr[1].trim().equalsIgnoreCase("bcc")) {
						msg.setRecipients(RecipientType.BCC, innerArr[0]);
					} else if (innerArr[1].trim().equalsIgnoreCase("cc")) {
						msg.setRecipients(RecipientType.CC, innerArr[0]);
					}
				} else {
					msg.setRecipients(RecipientType.TO, to);
				}
			}
			
			msg.setSubject(subject);
			MimeMultipart list = new MimeMultipart();
			MimeBodyPart part1 = new MimeBodyPart();
			part1.setContent(content, "text/html;charset=utf-8");
			list.addBodyPart(part1);
			if (files != null) {
				for (int i = 0 ; i < files.size(); i++ ) {
					File file = files.get(i);
					MimeBodyPart part = new MimeBodyPart();
					part.attachFile(file);
					part.setFileName(MimeUtility.encodeText(file.getName()));
					list.addBodyPart(part);
				}
			}
			msg.setContent(list);
			
			return msg;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
