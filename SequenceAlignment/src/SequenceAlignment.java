import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class SequenceAlignment {

	private final boolean MAKE_GRAPH = false;

	private final int MAX_SIZE = 10000;
	private final int INCREMENT = 100;


	private List<String> input1;
	private List<String> input2;



	public SequenceAlignment(){
		input1 = new ArrayList<>();
		input2 = new ArrayList<>();
		if(!MAKE_GRAPH){
			if(loadFromDialog()){
				runSingleTest();
			}
			else{
				System.out.println("Failed to load");
			}
		}
		else{
			runTestsForGraph();
		}
		System.exit(0);
	}

	private void runSingleTest(){
		Table table = buildTable();
		Matching matching = findMatching(table);
		printMatching(matching);
		saveMatching(matching);
	}

	private void runTestsForGraph(){
		int size = INCREMENT;
		while(size < MAX_SIZE){
			File file = new File("tests/NORMAL" + size + ".txt");
			if(loadFromFile(file)){
				runSingleTest();
			}
			else{
				System.out.println("failed to load file: NORMAL" + size + ".txt");
			}
			file = new File("tests/UNBALANCED" + size + ".txt");
			if(loadFromFile(file)){
				runSingleTest();
			}
			else{
				System.out.println("failed to load file: UNBALANCED" + size + ".txt");
			}
		}

	}



	private Table buildTable(){
		Table table = new Table(input2.size(), input1.size());
		for(int j=0;j<input1.size();j++){
			for(int i=0;i<input2.size();i++){
				if(input1.get(j).equals(input2.get(i))){
					table.setCell(Math.max(table.getCell(i-1, j-1) + 1 ,Math.max(table.getCell(i-1, j) - 2, table.getCell(i, j-1) - 2)), i, j);
				}
				else{
					table.setCell(Math.max(table.getCell(i-1, j-1) - 1 ,Math.max(table.getCell(i-1, j) - 2, table.getCell(i, j-1) - 2)), i, j);
				}
			}
		}
		return table;
	}

	private Matching findMatching(Table table){
		List<Pair> matchingList = new ArrayList<>();

		int j = input1.size()-1;
		int i = input2.size()-1;
		if(input1.get(j).equals(input2.get(i))){
			matchingList.add(new Pair(input1.get(j), input2.get(i)));
		}
		while(i > 0 && j > 0){
			if(table.getCell(i-1, j-1) >= Math.max(table.getCell(i-1, j), table.getCell(i, j-1))){
				matchingList.add(new Pair(input1.get(j-1), input2.get(i-1)));
				i--;
				j--;
			}
			else if(table.getCell(i-1, j) > table.getCell(i, j-1)){
				i--;
			}
			else{
				j--;
			}
		}

		Collections.reverse(matchingList); //needed as matching is otherwise backwards.
		return new Matching(matchingList, input1, input2);
	}



	private void saveMatching(Matching matching){
		try{
			File file = new File("output.txt");
			if(file.exists()){ //removes file if exists to clear.
				file.delete();
			}
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));



			for(String s : matching.getProcessedOutput1()){
				bw.write(s + " ");
			}
			bw.newLine();
			for(String s : matching.getProcessedOutput2()){
				bw.write(s + " ");
			}
			bw.newLine();
			for(String s : matching.getProcessedOutputComparison()){
				bw.write(s + " ");
			}
			bw.write("(" + matching.getTotalValue() + ")");

			bw.close();
			System.out.println("File Created");

		}catch(IOException e){System.out.println("Failed to write to file " + e);}
	}

	private void printMatching(Matching matching){

		for(String s : matching.getProcessedOutput1()){
			System.out.print(s + " ");
		}
		System.out.println();
		for(String s : matching.getProcessedOutput2()){
			System.out.print(s + " ");
		}
		System.out.println();;
		for(String s : matching.getProcessedOutputComparison()){
			System.out.print(s + " ");
		}
		System.out.println("(" + matching.getTotalValue() + ")");
	}


	public boolean loadFromDialog(){

		File file = new File(".");
		final JFrame frame = new JFrame();
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Select input file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);


		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		else {return false; }

		return loadFromFile(file);

	}

	public boolean loadFromFile(File file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] tokens;
			if((line = br.readLine()) != null){
				tokens = line.split("");
				input1 = Arrays.asList(tokens);
			}
			if((line = br.readLine()) != null){
				tokens = line.split("");
				input2 = Arrays.asList(tokens);
			}
			else{
				System.out.println("Failed to load from file - Requires 1 input per line");
				br.close();
				return false;
			}
			br.close();
			return true;
		}
		catch(IOException e){
			System.out.println("IO exception on file load: " + e);
			return false;
		}
	}



	public static void main(String[] args) {
		new SequenceAlignment();

	}

}
