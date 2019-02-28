package build.util.application.service.executor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component("copyTaskExecutor")
public class CopyTaskExecutor extends ThreadPoolTaskExecutor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CopyTaskExecutor(){
		super();


		this.setCorePoolSize(5);
		this.setMaxPoolSize(15);
		this.setWaitForTasksToCompleteOnShutdown(true);

	
	}

}
