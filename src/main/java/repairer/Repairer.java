package repairer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class Repairer {
	private List<String> operatorNames;
	private Iterator<String> currentOperator;
	private String path;
	private LinkedList<File> target_classesFiles;
	private LinkedList<File> target_testClassesFiles;
	private LinkedList<File> src_main_javaFiles;
	private LinkedList<File> src_test_javaFiles;
	private int totalStartFailures;
	private JUnitCore jUnitCore;
	/**
	 * @param path The root path of the project to repare which ends with "/"
	 * The project to repare must have src/main/java.
	 */
	public Repairer(String path) {
		this.path = path;
		this.resetOperatorNames();
		this.resetCurrent();
		this.target_classesFiles = new LinkedList<File>();
		this.target_testClassesFiles = new LinkedList<File>();
		this.src_main_javaFiles = new LinkedList<File>();
		this.src_test_javaFiles = new LinkedList<File>();
		this.totalStartFailures = 0;
		this.jUnitCore = new JUnitCore();
		
		this.findFiles(new File(path + Constants.SRC_CLASSES_PATH),this.src_main_javaFiles,".java");
		this.findFiles(new File(path + Constants.SRC_TESTCLASSES_PATH),this.src_test_javaFiles,".java");		
		
		// Create javac output folders.
		File target_classes = new File(this.path + Constants.TARGET_CLASSES_PATH);
		target_classes.mkdirs();
		
		File target_testClasses = new File(this.path + Constants.TARGET_TESTCLASSES_PATH);
		target_testClasses.mkdirs();
		
		// Compilation
		Runtime runtime = Runtime.getRuntime();
		StringBuilder sb = null;
		
		Process process = null;

		// Compilation of /target/classes/
		sb = new StringBuilder();
		
		for (File f : this.src_main_javaFiles) {
			sb.append("javac ").append(f.getAbsolutePath()).append(" -d ").append(this.path)
				.append(Constants.TARGET_CLASSES_PATH);
			
			try { process = runtime.exec(sb.toString()); } catch (IOException ie) { ie.printStackTrace(); };
			try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }		
		}
		
		// Compilation of /target/test-classes
		for (File f : this.src_test_javaFiles) {
			sb.append("javac ").append(f.getAbsolutePath()).append(" -d ").append(this.path)
				.append(Constants.TARGET_TESTCLASSES_PATH).append(" -cp ").append(Constants.SRC_MAIN_RESOURCES_PATH)
				.append(Constants.JUNIT_JAR_NAME);
			try { process = runtime.exec(sb.toString()); } catch (IOException ie) { ie.printStackTrace(); };
			try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
		}
		
		this.findFiles(new File(path + Constants.TARGET_CLASSES_PATH),this.target_classesFiles,".class");
		this.findFiles(new File(path + Constants.TARGET_TESTCLASSES_PATH),this.target_testClassesFiles,".class");

		Result result = runTests();
		this.totalStartFailures = result.getFailureCount();
		System.out.println(this.totalStartFailures);
	}
	
	private void resetCurrent() {
		this.currentOperator = this.operatorNames.iterator();
	}
	
	private Result runTests() {
		LinkedList<URL> urls = new LinkedList<URL>();
		URL url = null;
		File dir = null;
		for (File f : this.target_testClassesFiles) {
			try {
				dir = f.getParentFile();
				url = new URL("file://"+f.getAbsolutePath());
				urls.add(url);
			} catch (MalformedURLException me) {
				me.printStackTrace();
			}
		}
		URLClassLoader ucl = new URLClassLoader((URL[]) urls.toArray());
		LinkedList<Class<?>> list = new LinkedList<Class<?>>();
		for (File f : this.target_testClassesFiles) {
			try {
				list.add(ucl.loadClass(f.getName()));
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
		
		return JUnitCore.runClasses((Class<?>[]) list.toArray());
	}
	private void resetOperatorNames() {
		this.operatorNames = new LinkedList<String>();
		try {
			FileReader fr = new FileReader(Constants.SRC_MAIN_RESOURCES_PATH + Constants.OPERATORS_NAME);
			BufferedReader br = new BufferedReader(fr);
			String name = null;
			while ((name = br.readLine()) != null) {
				this.operatorNames.add(name);
			}
			br.close();
			fr.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	private void findFiles(File file, LinkedList<File> list, String part) {
		if (file.isFile()) {
			if (file.toString().contains(part)) {
				list.add(file);
			}
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				findFiles(f,list,part);
			}
		}
	}
	
	public void repair() {
		boolean continueToRepare;
		boolean hasNextOperator;			
	}
	
	public static void main(String args[]) {
		new Repairer("C:\\IDLtmp\\IntroClassJava\\dataset\\checksum\\2c1556672751734adf9a561fbf88767c32224fca14a81e9d9c719f18d0b21765038acc16ecd8377f74d4f43e8c844538161d869605e3516cf797d0a6a59f1f8e\\003\\");
	}
}
