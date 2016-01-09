package repairer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Repairer {
	private List<String> operatorNames;
	private Iterator<String> currentOperator;
	private String path;
	private LinkedList<File> javaFiles;
	
	
	/**
	 * @param path The root path of the project to repare which ends with "/"
	 * The project to repare must have src/main/java.
	 */
	public Repairer(String path) {
		this.path = path;
		resetOperatorNames();
		resetCurrent();
		javaFiles = new LinkedList<File>();
		fillJavaFiles(new File(path + "/src/main/java/"));
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
	
	private void fillJavaFiles(File file) {
		if (file.isFile()) {
			if (file.toString().contains(".java")) {
				javaFiles.add(file);
			}
		}
		if (file.isDirectory()) {
			File files[] = file.listFiles();
			for (File f : files) {
				fillJavaFiles(f);
			}
		}
	}
	
	public void repair() {
			boolean continueToRepare;
			boolean hasNextOperator;			
	}
	
	public static void main(String args[]) {
		new Repairer("E:/Utilisateurs/Étienne/Documents/workspace/Titi/");
	}
}
