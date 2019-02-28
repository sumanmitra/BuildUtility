package build.util.application.persistence.repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import build.util.application.config.AppConfig;
import build.util.application.persistence.entity.AuditTrailDTO;
import build.util.application.persistence.entity.History;

public class HistoryRepositoryClient {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		HistoryRepository repository = context.getBean(HistoryRepository.class);

		
		//Query creation from method names
//		findByJarName(repository);
		
		//Query method using property expression.
//		findByJobDetailsCount(repository);
		
		//Limiting query results
//		findTop1ByJarName(repository);
		
		//Stream the result of a query with Java 8 Stream<T>
		try {
			getLatestResult(repository);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	private static void findAllByJobDetailsId(HistoryRepository repository) {
		
		List <History> histories = repository.findAllByJobDetailsIdOrderByJarName(3340);
		histories.parallelStream().forEach(System.out::println);
		
	}

	private static void findTop1ByJarName(HistoryRepository repository) {
		List<History> histories = repository.findTop1ByJarName("ttn.jar");
		histories.stream().forEach(System.out::println);
		
	}

	private static void findByJobDetailsCount(HistoryRepository repository) {
		List<History> histories = repository.findByJobDetailsFileCount(10);
		histories.parallelStream().forEach(System.out::println);
		
	}

	private static void findByJarName(HistoryRepository repository) {
		
		List<History> histories = repository.findByJarName("ttn.jar");
		histories.parallelStream().forEach(System.out::println);
	}
	
	private static void getLatestResult(HistoryRepository repository) throws UnknownHostException{
		List<AuditTrailDTO> histories = repository.getLatestResult(InetAddress.getLocalHost().getHostName(), System.getProperty("user.name")); 
		
		histories.forEach(history -> {
			System.out.println(history.getJarName() + " - " + history.getFileSourcePath());
		});
	}
	


}
