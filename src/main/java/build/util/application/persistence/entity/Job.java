package build.util.application.persistence.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_details")
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Timestamp startDateTime;
	private Timestamp stopDateTime;
	private int fileCount;
	private String workspaceLocation;
	private String outputLocation;
	private String hostName;
	private String userName;

	public Job() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Timestamp getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(Timestamp stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public String getWorkspaceLocation() {
		return workspaceLocation;
	}

	public void setWorkspaceLocation(String artifactoryLocation) {
		this.workspaceLocation = artifactoryLocation;
	}

	public String getOutputLocation() {
		return outputLocation;
	}

	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", startDateTime=" + startDateTime + ", stopDateTime=" + stopDateTime + ", fileCount="
				+ fileCount + ", hostName=" + hostName + ", userName=" + userName + "]";
	}

}
