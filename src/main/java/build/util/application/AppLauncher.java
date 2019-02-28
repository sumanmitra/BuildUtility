package build.util.application;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.SortedSet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import build.util.application.client.BrowserService;
import build.util.application.config.AppConfig;
import build.util.application.persistence.entity.History;
import build.util.application.persistence.entity.Job;
import build.util.application.persistence.service.AuditTrailService;
import build.util.application.persistence.service.JobService;
import build.util.application.service.CopyService;


public class AppLauncher {


	public static void main(String... args) {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		BrowserService browserService = context.getBean(BrowserService.class);
		AuditTrailService auditTrailService = context.getBean(AuditTrailService.class);
			
		JobService jobService = context.getBean(JobService.class);
		CopyService copyService = context.getBean(CopyService.class);
		Job job = jobService.createJob();
		
		SortedSet<History> histories = copyService.process();
		
	//	job.setStopDateTime(new Timestamp(System.currentTimeMillis()));
		job.setStopDateTime(new Timestamp(Instant.now().toEpochMilli()));
		
		job.setFileCount(copyService.getTotalNumberOfFilesCopied());
		
		
		auditTrailService.save(job, histories);
		
		
		auditTrailService.getHistory();
		browserService.invokeURL("http://www.google.com");
		
	}

}
