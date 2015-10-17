import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestCityGen {

	private final int SIZE = 500000;
	private final int INCREMENT = 10000;

	/**
	 * This class is used to generate test cases for the algorithm.
	 * To use, set a maximum size and increment size then run.
	 * Will generate test cases up to the maximum size in random (normal) distinct and nested flavours.
	 * 
	 * Requires a "/tests/" sub folder to store tests in.
	 */

	public TestCityGen(){
		generateTestSet(SIZE);
	}

	/**
	 * Saves input city as a space separated text file with an appropriate name.  Will not overwrite files.
	 * @param city: A List of buildings to save.
	 * @param filename: Name to save the file as.
	 */

	private void writeToFile(List<Building> city, String filename){
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
			for(Building b : city){
				bw.write(b.getLeft() + " " + b.getRight() + " " + b.getHeight());
				bw.newLine();
			}
			bw.close();
			System.out.println(filename + " Created.");


		}catch(IOException e){System.out.println("Error in writing to file " + e);}
	}

	/**
	 * Generates test cases for each type based on the increment and maximum size.
	 * @param max: Maximum size of test cases.
	 */

	private void generateTestSet(int max){
		int num = INCREMENT;
		List<Building> city;
		while(num <= max){
			city = generateNormal(num);
			writeToFile(city, "NORMAL" + num);
			
			city = generateNormalAlt(num);
			writeToFile(city, "NORMALALT" + num);
			
			city = generateDistinct(num);
			writeToFile(city, "DISTINCT" + num);
			
			city = generateNested(num);
			writeToFile(city, "NESTED" + num);
			
			num = num + INCREMENT;
		}

	}
	
	/**
	 * Generates a random city.  Buildings are placed randomly from 0 to 1000, with 0 to 50 width and 0 to 200 height.
	 * @param size: size of test case to generate.
	 * @return List of buildings for test case.
	 */
	
	private List<Building> generateNormal(int size){
		List<Building> city = new ArrayList<>();
		double left = 0;
		double right = 0;
		double height = 0;
		for(int i=0;i<size;i++){
			left = Math.random()*1000;
			right = left + (Math.random()*50);
			height = Math.random()*200 + 10;
			city.add(new Building(left, right, height));
		}
		return city;
	}
	
	/**
	 * A second random generation method to test if the linear behaviour persists.
	 * 
	 * @param size: size of test case to generate.
	 * @return List of buildings for test case.
	 */
	
	private List<Building> generateNormalAlt(int size){
		List<Building> city = new ArrayList<>();
		double left = 0;
		double right = 0;
		double height = 0;
		for(int i=0;i<size;i++){
			left = Math.random()*1000;
			right = Math.random()*1000;
			if(left > right){  //ensure left is the least-most value.
				double temp = left;
				left = right;
				right = temp;
			}
			height = Math.random()*200 + 10;
			city.add(new Building(left, right, height));
		}
		return city;
	}
	
	/**
	 * Generates a random city with no overlapping buildings.  
	 * @param size: size of test case to generate.
	 * @return List of buildings for test case.
	 */
	
	private List<Building> generateDistinct(int size){
		List<Building> city = new ArrayList<>();
		double left = 0;
		double right = 0;
		double height = 0;
		for(int i=0;i<size;i++){
			left = right + Math.random()*20;
			right = left + (Math.random()*50);
			height = Math.random()*200 + 10;
			city.add(new Building(left, right, height));
		}
		return city;
	}
	
	/**
	 * Generates a random city of entirely nested buildings..  
	 * @param size: size of test case to generate.
	 * @return List of buildings for test case.
	 */
	
	private List<Building> generateNested(int size){
		List<Building> city = new ArrayList<>();
		double left = 0;
		double right = 0;
		double height = 0;
		for(int i=0;i<size;i++){
			left = left - Math.random()*10;
			right = right + (Math.random()*10);
			height = height + Math.random()*20 + 1;
			city.add(new Building(left, right, height));
		}
		return city;
	}

	public static void main(String[] args) {
		new TestCityGen();
		System.exit(0);
	}

}
