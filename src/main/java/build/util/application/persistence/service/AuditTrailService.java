package build.util.application.persistence.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.persistence.ConstructorResult;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import build.util.application.persistence.entity.AuditTrailDTO;
import build.util.application.persistence.entity.History;
import build.util.application.persistence.entity.Job;
import build.util.application.persistence.repository.HistoryRepository;
import build.util.application.persistence.repository.JobRepository;

@Service
public class AuditTrailService {

	private static final String PROPERTY_NAME_SOURCE_DIR = "sourceDir";

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private String hostName;
	@Resource
	private Environment env;

	public History createHistory(String sourcePath, File file) {
		String sourceDir = env.getRequiredProperty(PROPERTY_NAME_SOURCE_DIR);
		History history = new History();
		history.setJarName(file.getName());
		history.setLastModified(new Timestamp(file.lastModified()));
		history.setSize(file.length());
		history.setFileSourcePath(sourcePath.substring(sourceDir.length()));
		return history;

	}

	public void save(Job job, SortedSet<History> histories) {

		jobRepository.save(job);
		
		if (histories.size() == 0) {
			return;
		}

		histories.parallelStream().forEachOrdered(history -> {

			history.setJobDetails(job);
			historyRepository.save(history);
		});

	}
	
	public void getHistory() {
		/*
		 * //JPQL
		 * 
		 * List<AuditTrailDTO> list = jobRepository.findHistory("ttn.jar");
		 * list.parallelStream().forEach(System.out::println);
		 */
		
	 

		/*  @NativeQuery
		 *
 		 *	List<History> histories =
		 * historyRepository.queryByHostNameAndUserNameForLatestResult(hostName,
		 * System.getProperty("user.name")); 
		 * 
		 */
		
		
		// @NamedNativeQuery with non-entity class using @SqlResultSetMapping & @ConstructorResult
		
		List<AuditTrailDTO> histories = historyRepository.getLatestResult(hostName, System.getProperty("user.name")); 
		
		histories.forEach(history -> {
			System.out.println(history.getJarName() + " - " + history.getFileSourcePath());
		});

	}
	
	public void getHistories(){
		// * QueryDSL

		  Job job = jobRepository.queryByLatestStartDateTime(); 
		  List<History> histories =  historyRepository.findAllByJobDetailsIdOrderByJarName(job.getId());
			histories.forEach(history -> {
				System.out.println(history.getJarName() + " - " + history.getFileSourcePath());
			});
	}

}
