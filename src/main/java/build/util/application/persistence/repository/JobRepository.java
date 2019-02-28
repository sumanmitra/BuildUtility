package build.util.application.persistence.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.NamedNativeQuery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import build.util.application.persistence.entity.AuditTrailDTO;
import build.util.application.persistence.entity.Job;

public interface JobRepository extends CrudRepository<Job,Serializable>{
	
/*	Variant 1.
 * @Query ("select "
			+"new build.util.application.persistence.entity.AuditTrailDTO"
			+"(J.startDateTime, H.jarName, H.lastModified,  H.size) "
			+"from History H join Job J "
			+"on J.id = H.jobDetails.id "
			+"where H.jarName = ?1")
	List<AuditTrailDTO> findHistory(String jarName);*/
	
/*	Variant 2. JPQL*/
	@Query ("select "
			+"new build.util.application.persistence.entity.AuditTrailDTO"
			+"(J.startDateTime, H.jarName, H.fileSourcePath, H.lastModified,  H.size) "
			+"from History H join Job J "
			+"on J.id = H.jobDetails.id "
			+"where H.jarName =:jarName")
	List<AuditTrailDTO> findHistory(@Param("jarName") String jarName);
	
/*	@SqlResultSetMapping(
	        name = "HistoryDetailsMapping",
	        classes = @ConstructorResult(
	                targetClass = build.util.application.persistence.entity.AuditTrailDTO.class,
	                columns = {
	                    @ColumnResult(name = "startDateTime", type = java.util.Date.class),
	                    @ColumnResult(name = "jarName"),
	                    @ColumnResult(name = "lastModified", type = java.util.Date.class),
	                    @ColumnResult(name = "size", type = Long.class)}))
	
	@NamedNativeQuery(name = "HistoryDetailsMapping", query = "select startDateTime, jarname, lastmodified, size "
															  + "from history join job_details "
															  + "on job_details.id = history.jobdetails_id "
															  + "where jarName = :jarName "
															  + "order by lastmodified desc, size; " )*/

	@Query(value="SELECT  * FROM Job_details ORDER BY startDateTime DESC LIMIT 1", nativeQuery = true)
	Job queryByLatestStartDateTime();
}
