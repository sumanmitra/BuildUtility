package build.util.application.persistence.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class AuditTrailDTO implements Serializable{
	
	private String jarName;
	private String fileSourcePath;
	private long size;
	private Timestamp lastModified;
	private Timestamp startDateTime;
	
	
	
	
	public AuditTrailDTO(Date startDateTime,String jarName, String fileSourcePath, Date lastModified,long size ) {
		super();
		this.jarName = jarName;
		this.fileSourcePath = fileSourcePath;
		this.size = size;
		this.lastModified = new Timestamp(lastModified.getTime());
		this.startDateTime = new Timestamp(startDateTime.getTime());
	}
	
	public String getJarName() {
		return jarName;
	}
	public void setJarName(String jarName) {
		this.jarName = jarName;
	}
	public String getFileSourcePath() {
		return fileSourcePath;
	}

	public void setFileSourcePath(String fileSourcePath) {
		this.fileSourcePath = fileSourcePath;
	}

	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Timestamp getLastModified() {
		return lastModified;
	}
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	public Timestamp getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Override
	public String toString() {
		return "AuditTrailDTO [jarName=" + jarName + ", fileSourcePath=" + fileSourcePath + ", size=" + size
				+ ", lastModified=" + lastModified + ", startDateTime=" + startDateTime + "]";
	}

	


}
