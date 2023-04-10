package br.com.bsdev.evibbra.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.bsdev.evibbra.services.NotificationService;

@Component
public class NotificationScheduler {

	private final NotificationService notificationService;

	public NotificationScheduler(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	// Scheduled method to send monthly limit notification on the 1st day of each month
	@Scheduled(cron = "0 0 10 1 * *")
	public void sendMonthlyLimitNotification() {
		notificationService.sendMonthlyLimitNotification();
	}
}
