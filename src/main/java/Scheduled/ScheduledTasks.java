package Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
	
	@Scheduled(fixedRate = 1000)
	public void reportCurrentTime() {
		log.info("The time now is " + df.format(new Date()));
		System.out.println("The time now is " + df.format(new Date()));
	}
}
