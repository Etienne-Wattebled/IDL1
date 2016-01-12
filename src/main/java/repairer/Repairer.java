package repairer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Repairer {
	private List<String> operatorNames;
	private Iterator<String> currentOperator;
	private String path;
	private LinkedList<File> target_classesFiles;
	private LinkedList<File> target_testClassesFiles;
	

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
		
		sb.append("javac ").append(this.path).append(Constants.SRC_CLASSES_PATH).append("*.java").append(" -d ").append(this.path).append(Constants.TARGET_CLASSES_PATH);
		
		try { process = runtime.exec(sb.toString()); } catch (IOException ie) { ie.printStackTrace(); };
		try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }		
		
		// Compilation of /target/test-classes
		sb.append("javac ").append(this.path).append(Constants.SRC_TESTCLASSES_PATH).append("*.java").append(" -d ").append(this.path).append(Constants.TARGET_TESTCLASSES_PATH);
		try { process = runtime.exec(sb.toString()); } catch (IOException ie) { ie.printStackTrace(); };
		try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
		
		this.findJavaFiles(new File(path + Constants.TARGET_CLASSES_PATH),this.target_classesFiles);
		this.findJavaFiles(new File(path + Constants.TARGET_TESTCLASSES_PATH),this.target_testClassesFiles);
	}
	
	private void resetCurrent() {
		this.currentOperator = this.operatorNames.iterator();
	}
	
	private void resetOperatorNames() {
		this.operatorNames = new LinkedList<String>();
		try {
			FileReader fr = new FileReader("./src/main/java/resources/operators.txt");
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
	
	private void findJavaFiles(File file, LinkedList<File> list) {
		if (file.isFile()) {
			if (file.toString().contains(".class")) {
				list.add(file);
			}
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				findJavaFiles(f,list);
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
