package repairer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import spoon.processing.AbstractProcessor;

public class Repairer {
	private List<String> operatorNames;
	private Iterator<String> currentOperator;
	
	public Repairer() {
		resetOperatorNames();
		resetCurrent();
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
	
	public void repair() {
			boolean continueToRepare;
			boolean hasNextOperator;
	}
	
	public static void main(String args[]) {
		
	}
}
