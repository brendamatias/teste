package br.com.bsdev.evibbra.services;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;

import br.com.bsdev.evibbra.dtos.EmailRequestDto;
import br.com.bsdev.evibbra.exceptions.ResourceNotFoundException;

@Service
public class EmailService {

	@Value("${mailersend.from.name}")
	private String fromName;

	@Value("${mailersend.from.mail}")
	private String fromMail;

	@Value("${mailersend.api.token}")
	private String apiToken;

	public String send(EmailRequestDto dto) {
		Email email = new Email();

		email.setSubject(dto.getSubject());

		email.setFrom(fromName, fromMail);
		email.addRecipient(dto.getToName(), dto.getToEmail());
		
		String htmlTemplate = loadEmailTemplate(dto.getTemplate());
		String replacedHtml = htmlTemplate
                .replace("[USER_NAME]", dto.getToName())
                .replace("[REMAINDER_AMOUNT]", dto.getRemainderAmount())
                .replace("[LIMIT_MEI]", dto.getLimitMei());
		
		if (dto.getTotalAmount() != null) {
			replacedHtml = replacedHtml.replace("[TOTAL_AMOUNT]", dto.getTotalAmount());
		}
		
		email.setHtml(replacedHtml);

		MailerSend ms = new MailerSend();
		ms.setToken(apiToken);

		try {

			var response = ms.emails().send(email);

			if (response.messageId == null) {
				throw new ResourceNotFoundException("Error sending email, statusCode: " + response.responseStatusCode);
			}
			return response.messageId;
		} catch (MailerSendException e) {
			throw new ResourceNotFoundException("Error sending email: " + e.getMessage());
		}
	}
	
	public String loadEmailTemplate(String template) throws ResourceNotFoundException {
        Resource resource = new ClassPathResource("templates/"+template+".html");
        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
        	throw new ResourceNotFoundException("Error loading html template: " + e.getMessage());
        }
    }
}