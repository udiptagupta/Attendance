package com.ntmo.attendance.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendEmailUtil {
	
	private JavaMailSender javaMailSender;
	private String toAddress;
	private String fromAddress;
	private String subject;
	private String message;
	private String attachmentName;
	
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	
	
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendEmail() throws IOException, MessagingException {

		
		MimeMessage msg = this.javaMailSender.createMimeMessage();
		
		String pattern = "yyyyMMdd";
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		String reportName = "Attendance_" + df.format(new Date()) + ".xlsx";

		// true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("udigupta@in.ibm.com");
        // helper.setTo(this.getToAddress());
        helper.setSubject(this.getSubject());
        helper.setText(this.getMessage());
	
        FileSystemResource file = new FileSystemResource(new File(this.getAttachmentName()));
        helper.addAttachment(file.getFilename(), file);

		log.debug("Sending report " + reportName + " to " + this.getToAddress());
        javaMailSender.send(msg);
	}
}
