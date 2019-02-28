package build.util.application.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import build.util.application.persistence.jpa.JpaConfig;

@Configuration
@Import(JpaConfig.class)
@PropertySource("classpath:application.properties")
@ComponentScan("build.util.application.persistence.service, "
			 + "build.util.application.service, "
			 + "build.util.application.client")

@EnableJpaRepositories(basePackages = "build.util.application.persistence.repository")
@EnableTransactionManagement
public class AppConfig {

	@Bean
	@Scope("prototype")
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(8);
		pool.setMaxPoolSize(16);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean
	public String hostName() throws UnknownHostException{
		return InetAddress.getLocalHost().getHostName();
	}

}
