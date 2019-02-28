package build.util.application.persistence.repository;

import java.io.Serializable;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import build.util.application.persistence.entity.AuditTrailDTO;
import build.util.application.persistence.entity.History;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;
import javax.persistence.QueryHint; 
public interface HistoryRepository extends CrudRepository<History, Serializable>{
	//Query creation from method names
	List <History> findByJarName(String jarName);
	//Query method using property expression.
	List<History> findByJobDetailsFileCount(int count);
	//Limiting query results
	List<History> findTop1ByJarName(String jarName);
	
	//Stream the result of a query with Java 8 Stream<T>
	//Streaming results is not implement for this PersistenceProvider: GENERIC_JPA
	List<History> findAllByJobDetailsIdOrderByJarName(int id);  //QueryDSL
	
	
	@Query(value = "select * from history join job_details on history.jobdetails_id = job_details.id "
			+ "where job_details.id = "
			+            "(select id from job_details order by startdatetime desc limit 1)"
			+ "  and hostname = :hostName"
			+ "  and userName = :userName"
			+ " order by jarname", nativeQuery = true)

	List<History> queryByHostNameAndUserNameForLatestResult(@Param ("hostName") String hostName, 
															@Param ("userName")String userName);
	@Query   // This annotation is added to avoid 'Invalid derived query!' error.
	List<AuditTrailDTO> getLatestResult(@Param ("hostName") String hostName, 
										@Param ("userName")String userName);
	
}
