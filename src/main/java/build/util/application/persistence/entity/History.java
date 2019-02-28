package build.util.application.persistence.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

@Entity
@SqlResultSetMapping(
        name = "getLatestResult",
        classes = @ConstructorResult(
                targetClass = AuditTrailDTO.class,
                columns = {
                    @ColumnResult(name = "startDateTime", type = Date.class),
                    @ColumnResult(name = "jarName"),
                    @ColumnResult(name = "fileSourcePath"),
                    @ColumnResult(name = "lastModified", type = Date.class),
                	@ColumnResult(name = "size", type = Long.class)}))
@NamedNativeQuery(name = "History.getLatestResult", query = "select startDateTime, jarName,fileSourcePath,lastModified,  size from history join job_details on history.jobdetails_id = job_details.id "
		+ "where job_details.id = "
		+            "(select id from job_details order by startdatetime desc limit 1)"
		+ "  and hostname = :hostName"
		+ "  and userName = :userName"
		+ " order by jarname,fileSourcePath", resultSetMapping = "getLatestResult" )//,resultClass = AuditTrailDTO.class

@Table (name = "History")

public class History implements Serializable,Comparable<History> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
/*	@GeneratedValue(strategy = GenerationType.AUTO, generator = "bee_gen")
	@SequenceGenerator(name = "bee_gen", sequenceName = "bee_id_seq")*/
	@GeneratedValue( strategy = GenerationType.AUTO ) 
	private Integer id;
	private String jarName;
	private long size;
	private Timestamp lastModified;
	private String fileSourcePath;
	
	@ManyToOne
	private Job jobDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}



	public void setLastModified(Timestamp now) {
		lastModified = now;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Job getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(Job jobDetails) {
		this.jobDetails = jobDetails;
	}

	public String getFileSourcePath() {
		return fileSourcePath;
	}

	public void setFileSourcePath(String fileSourcePath) {
		this.fileSourcePath = fileSourcePath;
	}



	@Override
	public String toString() {
		return "History [id=" + id + ", jarName=" + jarName + ", size=" + size + ", lastModified=" + lastModified
				+ ", fileSourcePath=" + fileSourcePath + ", jobDetails=" + jobDetails + "]";
	}

	@Override
	public int compareTo(History obj) {

		if (obj == null) {
			return 0;
		}
		if (this.getLastModified().before(((History) obj).getLastModified())) {
			return -1;
		} else if (this.getLastModified().after(((History) obj).getLastModified())) {
			return 1;
		}

		return this.getJarName().compareTo((((History) obj).getJarName()));

	}

}
