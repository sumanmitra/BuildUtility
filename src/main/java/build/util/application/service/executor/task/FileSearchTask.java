package build.util.application.service.executor.task;
import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileSearchTask implements Runnable {

		
	private BlockingQueue<String> fileAbsolutePathContainer ;
	private	File dirToSearch;
	private CountDownLatch doneSignal;
	private String dirNameToSearch;
		

		
		
		
		
		
		@Override
		public void run() {

			searchDir(getDirToSearch(), "gradleoutput"); 

		}
		
		private void searchDir(File directory, String dirNameToSearch) {

			setDirNameToSearch(dirNameToSearch);

			if (directory.isDirectory()) {
				search(directory);
			}
			getDoneSignal().countDown();
		}
		
		private void search(File file) {

			if (file.isDirectory()) {
				if (file.canRead()) {
					for (File temp : file.listFiles()) {
						if (temp.isDirectory()
								&& getDirNameToSearch().equalsIgnoreCase(
										temp.getName())) {

							try {
								addPathForFilesToBeCopied(temp);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						} else {
							if (temp.isDirectory()) {
								search(temp);
							}

						}
					}

				}
			}
			

		}
		private void addPathForFilesToBeCopied(File temp) throws InterruptedException {
			
			String fileName = temp.getAbsolutePath() + "\\libs\\";
			File file = new File(fileName);
			FileFilter filter = (File tmpFile)-> tmpFile.getName().toLowerCase().endsWith(".jar");
			
			if(file.isDirectory() && file.exists()){
				for(File tempFile : file.listFiles(filter)){
					getFileAbsolutePathContainer().put(tempFile.getAbsolutePath());
				}
				
			}
			
//			getFileAbsolutePathContainer().put(determineFileName(temp));
		}
		private String determineFileName(File temp) {
			String fileName = temp.getAbsolutePath() + "\\libs\\"
					+ temp.getParentFile().getName() + ".jar";
			return fileName;
		}
		
		public String getDirNameToSearch() {
			return dirNameToSearch;
		}
		
		public void setDirNameToSearch(String dirNameToSearch) {
			this.dirNameToSearch = dirNameToSearch;
		}
		
		public File getDirToSearch() {
			return dirToSearch;
		}
		public void setDirToSearch(File dirToSearch) {
			this.dirToSearch = dirToSearch;
		}
		public BlockingQueue<String> getFileAbsolutePathContainer() {
			return fileAbsolutePathContainer;
		}
		public void setFileAbsolutePathContainer(BlockingQueue<String> filesToBeCopied) {
			this.fileAbsolutePathContainer = filesToBeCopied;
		}

		public CountDownLatch getDoneSignal() {
			return doneSignal;
		}

		public void setDoneSignal(CountDownLatch doneSignal) {
			this.doneSignal = doneSignal;
		}
		
	}