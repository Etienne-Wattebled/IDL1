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
		try {
			Runtime runtime = Runtime.getRuntime();
			StringBuilder sb = null;
			
			
			this.findJavaFiles(new File(path + Constants.TARGET_CLASSES_PATH),this.target_classesFiles);
			this.findJavaFiles(new File(path + Constants.TARGET_TESTCLASSES_PATH),this.target_testClassesFiles);
			Process process = null;
			
			// Compilation of /target/classes/
			sb = new StringBuilder();
			Iterator<File> it = null;			
			it = target_classesFiles.iterator();
			while (it.hasNext()) {
				sb.append("javac ").append(this.path).append(Constants.SRC_CLASSES_PATH).append(it.next().getPath()).append(" -d ").append(this.path).append(Constants.TARGET_CLASSES_PATH);
				process = runtime.exec(sb.toString());
				try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
			}			
			
			// Compilation of /target/test-classes
			sb = new StringBuilder();
			it = target_testClassesFiles.iterator();
			while (it.hasNext()) {
				sb.append("javac ").append(this.path).append(Constants.SRC_TESTCLASSES_PATH).append(it.next().getPath()).append(" -d ").append(this.path).append(Constants.TARGET_TESTCLASSES_PATH);
				process = runtime.exec(sb.toString());
				try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
			}
			
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		
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
		new Repairer("\\home\\m2iagl\\wattebled\\workspace\\TITI\\");
	}
}
