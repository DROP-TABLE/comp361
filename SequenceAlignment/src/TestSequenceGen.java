import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TestSequenceGen {
	
	private final int SIZE = 100000;
	private final int INCREMENT = 1000;
	
	private List<String> seq1;
	private List<String> seq2;
	
	public TestSequenceGen(){
		generateTestSet(SIZE);
	}

	
	private void writeToFile(String filename){
		try{
			File file = new File("tests/" + filename + ".txt");
			if(file.exists()){
				System.out.println("File already exists");
				return;
			}
			else{
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			for(String s : seq1){
				bw.write(s);
			}
			bw.newLine();
			for(String s : seq2){
				bw.write(s);
			}
			bw.close();
			System.out.println(filename + " Created.");


		}catch(IOException e){System.out.println("Error in writing to file " + e);}
	}
	
	private void generateTestSet(int max){
		int num = INCREMENT;
		while(num <= max){
			generateNormal(num);
			writeToFile("NORMAL" + num);
			
			generateUnbalanced(num);
			writeToFile("UNBALANCED" + num);
			
			num = num + INCREMENT;
		}

	}
	
	private void generateNormal(int size){
		seq1 = new ArrayList<>();
		seq2 = new ArrayList<>();
		for(int i=0;i<size;i++){
			seq1.add(getBase());
			seq2.add(getBase());
		}
	}
	
	private void generateUnbalanced(int size){
		seq1 = new ArrayList<>();
		seq2 = new ArrayList<>();
		for(int i=0;i<size;i++){
			seq1.add(getBase());
		}
		for(int i=0;i<size/2;i++){
			seq2.add(getBase());
		}
	}
	
	private String getBase(){
		Random rand = new Random();
		switch(rand.nextInt(4)){
		case 0:
			return "A";
			
		case 1:
			return "T";
		
		case 2:
			return "C";
			
		default: //case 3
			return "G";
			
		}
	}
	
	public static void main(String[] args) {
		new TestSequenceGen();
		System.exit(0);
	}
	
}
