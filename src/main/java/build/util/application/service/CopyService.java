package build.util.application.service;

import java.io.File;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import build.util.application.persistence.entity.History;
import build.util.application.service.executor.CopyTaskExecutor;
import build.util.application.service.executor.SearchTaskExecutor;
import build.util.application.service.executor.task.FileCopyTask;
import build.util.application.service.executor.task.FileSearchTask;

@Service
public class CopyService {

	private static final String PROPERTY_NAME_SOURCE_DIR = "sourceDir";
	private static final String PROPERTY_NAME_TARGET_DIR = "targetDir";

	private LinkedBlockingQueue<String> filesToBeCopied = new LinkedBlockingQueue<String>();
	private SortedSet<History> historySet = new TreeSet<History>();
	private ConcurrentHashMap<String, Integer> fileCountPerTaskMap = new ConcurrentHashMap<String, Integer>();
	private volatile Boolean searchCompleted;

	@Resource
	private Environment env;

	@Autowired
	private SearchTaskExecutor searchTaskExecutor;
	@Autowired
	private CopyTaskExecutor copyTaskExecutor;

	@Autowired
	AnnotationConfigApplicationContext context;

	public LinkedBlockingQueue<String> getFilesToBeCopied() {
		return filesToBeCopied;
	}

	public void setFilesToBeCopied(LinkedBlockingQueue<String> filesToBeCopied) {
		this.filesToBeCopied = filesToBeCopied;
	}

	public SortedSet<History> process() {

		String sourceDir = env.getRequiredProperty(PROPERTY_NAME_SOURCE_DIR);

		String targetDir = env.getRequiredProperty(PROPERTY_NAME_TARGET_DIR);

		if (!validInput(sourceDir, targetDir)) {
			System.out.println("Invalid input! (Source or Target)");
			return null;
		}

		searchAndCopy(sourceDir, targetDir);

		return getHistorySet();
	}

	private boolean validInput(String sourceDir, String destDir) {
		return (sourceDir != null && sourceDir.length() > 0 && destDir != null && destDir.length() > 0);
	}

	private void searchAndCopy(String sourceDir, String destDir) {

		CountDownLatch searchDoneSignal = searchDirectory(new File(sourceDir));

		CountDownLatch copyTaskDoneSignal = copyFiles(destDir);

		try {
			searchDoneSignal.await();
			setSearchCompleted(true);
			copyTaskDoneSignal.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (getTotalNumberOfFilesCopied() == 0) {
			System.out.println("\nNo jars found!");
		} else {

			System.out.println("Total no. of jars copied : " + getTotalNumberOfFilesCopied());
		}

	}

	private CountDownLatch copyFiles(String destDir) {

		CountDownLatch doneSignal = new CountDownLatch(4);

		IntStream.range(0, 4).forEach(i -> {
			FileCopyTask task = prepareCopyTask(destDir, i,doneSignal);
			copyTaskExecutor.execute(task);
			//startCopyTask(destDir, i,doneSignal)
		});

		copyTaskExecutor.shutdown();
		return doneSignal;

	}

	private void startCopyTask(String destDir, int i, CountDownLatch doneSignal) {
		FileCopyTask task = prepareCopyTask(destDir, i,doneSignal);

		copyTaskExecutor.execute(()->{
			task.run();
			doneSignal.countDown();
		});

	}

	private FileCopyTask prepareCopyTask(String destDir, int i, CountDownLatch doneSignal) {
		FileCopyTask task = context.getBean(FileCopyTask.class);
		task.setIdentity(i);
		task.setDestination(destDir);
		task.setDoneSignal(doneSignal);
		return task;
	}

	private CountDownLatch searchDirectory(File file) {

		final Predicate<File> directory = f -> f.isDirectory();

		int numberOfDirectories = (int) Stream.of(file.listFiles()).filter(directory).count();

		CountDownLatch doneSignalLatch = new CountDownLatch(numberOfDirectories);

		setSearchCompleted(false);

		Arrays.asList(file.listFiles()).parallelStream().filter(directory).forEach(dir -> 
			startSearch(dir,doneSignalLatch));

		searchTaskExecutor.shutdown();
		return doneSignalLatch;
	}

	private void startSearch(File dir, CountDownLatch doneSignalLatch) {
		FileSearchTask searchTask = prepareSearchTask(dir,doneSignalLatch);
		searchTaskExecutor.execute(searchTask);
		
/*		searchTaskExecutor.execute(()-> {
			searchTask.run();
			doneSignalLatch.countDown();
		});*/
		
	}

	private FileSearchTask prepareSearchTask(File dir, CountDownLatch doneSignal) {
		FileSearchTask searchTask = context.getBean(FileSearchTask.class);
		searchTask.setDirToSearch(dir);
		searchTask.setFileAbsolutePathContainer(filesToBeCopied);
		searchTask.setDoneSignal(doneSignal);
		return searchTask;
	}

	public boolean isSearchCompleted() {
		return searchCompleted;
	}

	 public  void setSearchCompleted(boolean searchCompleted) {
		 synchronized(this){
			 this.searchCompleted = searchCompleted;
		 }
	}

	public SortedSet<History> getHistorySet() {
		return historySet;
	}

	public void setHistorySet(SortedSet<History> historyList) {
		this.historySet = historyList;
	}

	public ConcurrentHashMap<String, Integer> getFileCountPerTaskMap() {
		return fileCountPerTaskMap;
	}

	public void setFileCountPerTaskMap(ConcurrentHashMap<String, Integer> fileCountPerTaskMap) {
		this.fileCountPerTaskMap = fileCountPerTaskMap;
	}

	public int getTotalNumberOfFilesCopied() {

		return getFileCountPerTaskMap().values().stream().mapToInt(i -> i.intValue()).sum();

	}

}