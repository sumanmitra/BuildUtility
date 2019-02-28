package build.util.application.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



public class CopyServiceTest {
	
	CopyService service = Mockito.mock(CopyService.class);
	
	@Before
	public void setup(){
		Mockito.when(service.getTotalNumberOfFilesCopied()).thenReturn(10);
	}
	
	@Test
	public void test_TotalNumberOfFilesCopied(){

	//	service.getTotalNumberOfFilesCopied();
		Assert.assertEquals(10, service.getTotalNumberOfFilesCopied());
		Mockito.verify(service).getTotalNumberOfFilesCopied();
	//	Mockito.verify(service, Mockito.times(2) ).getTotalNumberOfFilesCopied();
	}
}
