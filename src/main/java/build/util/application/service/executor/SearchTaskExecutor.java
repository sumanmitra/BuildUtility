package build.util.application.service.executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component("searchTaskExecutor")
public class SearchTaskExecutor extends ThreadPoolTaskExecutor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchTaskExecutor() {
		super();

		this.setCorePoolSize(5);
		this.setMaxPoolSize(15);
		this.setWaitForTasksToCompleteOnShutdown(true);

	
	
	}
	
	

}
