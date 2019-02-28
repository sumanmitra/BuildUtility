package build.util.application.persistence.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import build.util.application.persistence.entity.Job;

@Service
public class JobService {
	
	@Autowired
	private String hostName;
	@Resource
	private Environment env;	
	
	private static final String PROPERTY_NAME_SOURCE_DIR = "sourceDir";
	private static final String PROPERTY_NAME_TARGET_DIR = "targetDir";
	public Job createJob(){
		
		Job job = new Job();
		long calendar = Calendar.getInstance().getTimeInMillis();
		long intant = Instant.now().toEpochMilli();
	//	job.setStartDateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		job.setStartDateTime(new Timestamp(Instant.now().toEpochMilli()));
		job.setHostName(hostName);
		job.setUserName(System.getProperty("user.name"));
		job.setWorkspaceLocation(env.getRequiredProperty(PROPERTY_NAME_SOURCE_DIR));
		job.setOutputLocation(env.getRequiredProperty(PROPERTY_NAME_TARGET_DIR));
		return job;
	}

}
