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
		resetOperatorNames();
		resetCurrent();
		target_classesFiles = new LinkedList<File>();
		target_testClassesFiles = new LinkedList<File>();
				
		// Create javac output folders.
		
		File target_classes = new File(path + "target\\classes\\");
		System.out.println(target_classes.mkdirs());
		
		File target_testClasses = new File(path + "target\\test-classes\\");
		System.out.println(target_testClasses.mkdirs());
		
		// Compilation
		try {
			Runtime runtime = Runtime.getRuntime();
			StringBuilder sb = null;
			
			// Compilation of /target/classes/
			sb = new StringBuilder();
			sb.append("javac ").append(path).append("src\\main\\java\\*.java -d ").append(path).append("target\\classes\\");
			System.out.println(sb.toString());
			
			Process process = null;
			process = runtime.exec(sb.toString());
			try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
			
			// Compilation of /target/test-classes
			sb = new StringBuilder();
			sb.append("javac ").append(path).append("src\\test\\java\\*.java -d ").append(path).append("target\\test-classes\\");
			System.out.println(sb.toString());
			
			process = runtime.exec(sb.toString());
			try { process.waitFor(); } catch (InterruptedException ie) { ie.printStackTrace(); }
			
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		findJavaFiles(new File(path + "target/classes/"),target_classesFiles);
		findJavaFiles(new File(path + "target/test-classes/"),target_testClassesFiles);
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
		new Repairer("~\\workspace\\TITI\\");
	}
}
