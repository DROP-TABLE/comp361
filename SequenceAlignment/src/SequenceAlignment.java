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
	
	private List<String> input1;
	private List<String> input2;
	
	public SequenceAlignment(){
		input1 = new ArrayList<>();
		input2 = new ArrayList<>();
		runTest();
		System.exit(0);
	}
	
	private void runTest(){
		if(loadFromDialog()){
			Table table = buildTable();
			List<Pair> matching = findMatching(table);
			printMatching(matching);
			saveMatching(matching);
		}
		else{
			System.out.println("Failed to load");
		}
	}
	
	
	
	private Table buildTable(){
		Table table = new Table(input2.size(), input1.size());
		System.out.println("m=" + input2.size() + " n=" + input1.size());
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
	
	private List<Pair> findMatching(Table table){
		List<Pair> matching = new ArrayList<>();
		int j = input1.size()-1;
		int i = input2.size()-1;
		while(i > 0 && j > 0){
			if(table.getCell(i-1, j-1) > Math.max(table.getCell(i-1, j), table.getCell(i, j-1))){
				matching.add(new Pair(input1.get(j-1), input2.get(i-1)));
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
		Collections.reverse(matching); //needed as matching is otherwise backwards.
		return matching;
	}
	
	private void saveMatching(List<Pair> matching){
		
		List<String> first = new ArrayList<>();
		List<String> second = new ArrayList<>();
		List<String> comparison = new ArrayList<>();
		int count = 0;
		int j = 0;
		int i = 0;
		System.out.println("length of matching: " + matching.size());
		System.out.println("inp2 ele1: " + input2.get(i) + ", matchign 1/2: " + matching.get(count).getSecondElem());
		while(j < input1.size() && i < input2.size() && count < matching.size()){
			
			if(input1.get(j).equals(matching.get(count).getFirstElem()) && input2.get(i).equals(matching.get(count).getSecondElem())){
				first.add(input1.get(j));
				second.add(input2.get(i));
				j++;
				i++;
				count++;
			}
			else if(input1.get(j).equals(matching.get(count).getFirstElem())){
				//while(!input2.get(i).equals(matching.get(count).getSecondElem()) && j < input1.size()){
					first.add(" ");
					second.add(input2.get(i));
					i++;
				//}
			}
			else if(input2.get(i).equals(matching.get(count).getSecondElem())){
				//while(!input1.get(j).equals(matching.get(count).getFirstElem()) && i < input2.size()){
					first.add(input1.get(j));
					second.add(" ");
					j++;
				//}
			}
			else{
				System.out.println("invalid matching");
				break;
			}
		}
		
		while(j < input1.size()){  //add rest of input1 to array
			first.add(input1.get(j));
			second.add(" ");
			j++;
		}
		
		while(i< input2.size()){ //add rest of input2 to array
			first.add(" ");
			second.add(input2.get(i));
			i++;
		}
		
		int total = 0;
		for(int k=0;k<first.size();k++){
			if(first.get(k).equals(second.get(k))){
				comparison.add("+");
				total = total + 1;
			}
			else if(first.get(k).equals(" ") || second.get(k).equals(" ")){
				comparison.add("*");
				total = total - 2;
			}
			else{
				comparison.add("-");
				total = total - 1;
			}
		}
		
		
		try{
			File file = new File("output.txt");
			if(file.exists()){ //removes file if exists to clear.
				file.delete();
			}
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			for(String s : first){
				bw.write(s + " ");
			}
			bw.newLine();
			for(String s : second){
				bw.write(s + " ");
			}
			bw.newLine();
			for(String s : comparison){
				bw.write(s + " ");
			}
			bw.write("(" + total + ")");
			
			bw.close();
			System.out.println("File Created");

		}catch(IOException e){System.out.println("Failed to write to file " + e);}
	}
	
	private void printMatching(List<Pair> matching){
		for(Pair p : matching){
			System.out.println("1: " + p.getFirstElem() + ", 2: " + p.getSecondElem());
		}
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
