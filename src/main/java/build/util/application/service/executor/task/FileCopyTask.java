package build.util.application.service.executor.task;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import build.util.application.persistence.entity.History;
import build.util.application.persistence.service.AuditTrailService;
import build.util.application.service.CopyService;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileCopyTask implements Runnable {

	private static int totalCount = 0;
	private String destination;
	
	@Autowired
	CopyService copyService;
	@Autowired
	private AuditTrailService auditTrailService;

	private int identity;
	
	private CountDownLatch doneSignal;




	@Override
	public void run() {
		String taskId = "CopyThread- " + Integer.toString(identity + 1);
		while (!copyService.isSearchCompleted() || !copyService.getFilesToBeCopied().isEmpty()) {
			
			int count = copyFile(getDestination(), copyService.getFilesToBeCopied());

			if (copyService.getFileCountPerTaskMap().containsKey(taskId)){
				int tempCount = copyService.getFileCountPerTaskMap().get(taskId);
				tempCount+=count;
				copyService.getFileCountPerTaskMap().put(taskId, tempCount);
			}else{
				
				copyService.getFileCountPerTaskMap().put(taskId,
						new Integer(count));
			}
		}
		System.out.println(taskId +" - "+copyService.getFileCountPerTaskMap().get(taskId));
		getDoneSignal().countDown();
	}

	int copyFile(String destDir, BlockingQueue<String> blockingQueue) {
		File sourceFile = null;
		int count = 0;
		/*
		 * new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return (name.toLowerCase().endsWith(".jar"));
					}
				}
		 * */
		FileFilter filter = (File file)-> file.getName().toLowerCase().endsWith(".jar");
		String fileName = blockingQueue.poll();
		if (fileName != null) {

			sourceFile = new File(fileName);
		}
		File destFile = null;
		if (sourceFile != null && !sourceFile.exists()) {

			File parentFile = sourceFile.getParentFile();
			if (parentFile != null && parentFile.isDirectory()) {
				for (File tmp : parentFile.listFiles(filter)) {
					destFile = new File(destDir + "\\" + tmp.getName());
					count+= copyFile(tmp, destFile);
				}
			}
			return count;
		} else if (sourceFile != null) {

			destFile = new File(destDir + "\\" + sourceFile.getName());
			count+= copyFile(sourceFile, destFile);
		}

		return count;
	}

	private int copyFile(File sourceFile, File destFile) {
		try {

			if (!destFile.exists() || (destFile.exists() && sourceFile.lastModified() > destFile.lastModified())) {

				Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//				System.out.println(destFile.getName());
				maintainHistory(sourceFile.getParentFile().getAbsolutePath(),destFile);
				return 1;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void maintainHistory(String sourcePath, File destFile) {
		getHistorySet().add(auditTrailService.createHistory(sourcePath,destFile));
	}


	public void setDestination(String destDir) {

		this.destination = destDir;

	}

	public String getDestination() {
		return destination;
	}

	public static int getTotalCount() {
		return totalCount;
	}

	public SortedSet<History> getHistorySet() {

		return copyService.getHistorySet();
	}

	public void setIdentity(int i) {
		this.identity= i;
	}

	public CountDownLatch getDoneSignal() {
		return doneSignal;
	}

	public void setDoneSignal(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}



}