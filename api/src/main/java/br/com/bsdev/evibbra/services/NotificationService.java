package br.com.bsdev.evibbra.services;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.bsdev.evibbra.controllers.response.AvailableAmountResponse;
import br.com.bsdev.evibbra.dtos.EmailRequestDto;
import br.com.bsdev.evibbra.entities.User;
import br.com.bsdev.evibbra.repositories.UserRepository;

@Service
public class NotificationService {

	private static final double LIMIT_THRESHOLD_PERCENT = 80.0;

	private final UserRepository userRepository;
	private final DashboardService dashboardService;
	private final PreferenceService preferenceService;
	private final EmailService emailService;
	private final SmsService smsService;

	// Constructor injection for the required services
	public NotificationService(DashboardService dashboardService, EmailService emailService,
			PreferenceService preferenceService, UserRepository userRepository, SmsService smsService) {
		this.dashboardService = dashboardService;
		this.emailService = emailService;
		this.preferenceService = preferenceService;
		this.userRepository = userRepository;
		this.smsService = smsService;
	}

	/**
	 * Sends a monthly notification to users about their MEI invoice limit.
	 */
	public void sendMonthlyLimitNotification() {
		var users = userRepository.findAll();
		var preference = preferenceService.getPreference();

		// Check if there are users and if notifications are enabled (email or SMS)
		if (users.isEmpty() || (!preference.getEmailNotificationsEnabled() && !preference.getSmsNotificationsEnabled())) {
			return;
		}

		int currentYear = LocalDate.now().getYear();
		AvailableAmountResponse availableAmount = dashboardService.getAvailableInvoiceAmountByCompetenceYear(currentYear);

		String remainingLimit = formatCurrency(availableAmount.getAnnualInvoiceLimit() - availableAmount.getTotalInvoiceAmount());
		String meiLimit = formatCurrency(availableAmount.getAnnualInvoiceLimit());

		for (User user : users) {
			if (preference.getEmailNotificationsEnabled()) {
				sendEmail(user, remainingLimit, meiLimit, "email_balance", "Aviso de Limite de Faturamento MEI");
			}

			if (preference.getSmsNotificationsEnabled()) {
				sendSms(user.getPhoneNumber(), "Seu limite de faturamento como MEI está próximo. Ainda pode emitir " + remainingLimit + " em Notas Fiscais este ano.");
			}
		}
	}

	/**
	 * Sends an alert to the user when the MEI invoice limit reaches 80%.
	 */
	public void sendNotificationNearLimit() {
		var userSession = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(userSession instanceof User)) {
			return;
		}

		User user = (User) userSession;
		var preference = preferenceService.getPreference();

		int currentYear = LocalDate.now().getYear();
		AvailableAmountResponse availableAmount = dashboardService.getAvailableInvoiceAmountByCompetenceYear(currentYear);

		double totalInvoiceAmount = availableAmount.getTotalInvoiceAmount();
		double annualInvoiceLimit = availableAmount.getAnnualInvoiceLimit();

		if (isNearLimit(totalInvoiceAmount, annualInvoiceLimit)) {
			String remainingLimit = formatCurrency(annualInvoiceLimit - totalInvoiceAmount);
			String meiLimit = formatCurrency(annualInvoiceLimit);
			String totalInvoices = formatCurrency(totalInvoiceAmount);

			if (preference.getEmailNotificationsEnabled()) {
				sendEmail(user, remainingLimit, meiLimit, totalInvoices, "email_limite", "Atenção: Você está próximo do limite de faturamento MEI");
			}

			if (preference.getSmsNotificationsEnabled()) {
				sendSms(user.getPhoneNumber(),
						"Atenção! Você atingiu 80% do limite de faturamento anual do MEI. Confira as ações para evitar desenquadramento.");
			}
		}
	}

	// Helper Methods

	/**
	 * Checks if the user has reached 80% of the MEI annual invoice limit.
	 */
	private boolean isNearLimit(double totalInvoiceAmount, double annualInvoiceLimit) {
		return (totalInvoiceAmount / annualInvoiceLimit) * 100 >= LIMIT_THRESHOLD_PERCENT;
	}

	/**
	 * Formats a double value into Brazilian Real currency format.
	 */
	private String formatCurrency(double value) {
		Locale brazilLocale = new Locale("pt", "BR");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brazilLocale);
		return currencyFormatter.format(value);
	}

	/**
	 * Sends an email to the user with the given details.
	 */
	private void sendEmail(User user, String remainingLimit, String meiLimit, String template, String subject) {
		EmailRequestDto emailRequest = new EmailRequestDto();
		emailRequest.setSubject(subject);
		emailRequest.setToEmail(user.getEmail());
		emailRequest.setToName(user.getName());
		emailRequest.setTemplate(template);
		emailRequest.setRemainderAmount(remainingLimit);
		emailRequest.setLimitMei(meiLimit);
		emailService.send(emailRequest);
	}

	/**
	 * Sends an email with the given additional total amount.
	 */
	private void sendEmail(User user, String remainingLimit, String meiLimit, String totalInvoices, String template, String subject) {
		EmailRequestDto emailRequest = new EmailRequestDto();
		emailRequest.setSubject(subject);
		emailRequest.setToEmail(user.getEmail());
		emailRequest.setToName(user.getName());
		emailRequest.setTemplate(template);
		emailRequest.setRemainderAmount(remainingLimit);
		emailRequest.setLimitMei(meiLimit);
		emailRequest.setTotalAmount(totalInvoices);
		emailService.send(emailRequest);
	}

	/**
	 * Sends an SMS message to the given phone number.
	 */
	private void sendSms(String phoneNumber, String message) {
		smsService.send(phoneNumber, message);
	}
}
