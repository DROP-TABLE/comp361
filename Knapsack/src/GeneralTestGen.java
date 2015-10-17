import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Generates test case for 0-1 and 0-N knapsack.
 * @author littledani1
 *
 */

public class GeneralTestGen {

	private int itemnumber = 0;

	private final int SIZE = 10;
	private final int INCREMENT = 1;

	public GeneralTestGen(){
		runAllTests();
	}

	private void runAllTests(){
		for(int i=INCREMENT;i<=SIZE;i+=INCREMENT){
			generateTests(i);
			itemnumber = 0;
			generateSimpleTests(i);
			itemnumber = 0;
		}
		
		for(int i=100;i<=1000;i+=100){
			generateTests(i);
			itemnumber = 0;
			generateSimpleTests(i);
			itemnumber = 0;
		}
	}

	private void generateTests(int size){
		List<String> items = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		List<Integer> weights = new ArrayList<>();
		List<Integer> amount = new ArrayList<>();
		int itemcount = 0;
		Random rand = new Random();

		while(itemcount<size){
			items.add(getItemName());
			values.add(rand.nextInt(100) + 1);
			weights.add(rand.nextInt(10) + 1);
			int temp = rand.nextInt(10) + 1;
			amount.add(temp);
			itemcount += temp;
		}

		writeToFile("SIMPLE" + size, items, values, weights, amount);
	}
	
	private void generateSimpleTests(int size){
		List<String> items = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		List<Integer> weights = new ArrayList<>();
		List<Integer> amount = new ArrayList<>();
		Random rand = new Random();

		for(int i=0;i<size;i++){
			items.add(getItemName());
			values.add(rand.nextInt(100) + 1);
			weights.add(rand.nextInt(10) + 1);
			amount.add(1);
		}
		writeToFile("FULL" + size, items, values, weights, amount);
	}

	private String getItemName(){
		itemnumber++;
		return "Item" + itemnumber;
	}

	private void writeToFile(String filename, List<String> items, List<Integer> values, List<Integer> weights, List<Integer> amount){
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

			for(int i=0;i<items.size();i++){
				bw.write(items.get(i));
				bw.write(" " + values.get(i));
				bw.write(" " + weights.get(i));
				bw.write(" " + amount.get(i));
				bw.newLine();
			}

			bw.close();

			System.out.println(filename + " Created.");

		}catch(IOException e){System.out.println("Error in writing to file " + e);}
	}

	public static void main(String[] args) {
		new GeneralTestGen();

	}

}
