import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestCityGen {

	/**
	 * Mode Key:
	 * NORMAL: Standard random generation using Math.random()
	 * DISTINCT: Prevents overlapping buildings
	 * NESTED: Buildings edges do not cross. Each is inside others.
	 */

	private enum Mode {NORMAL, DISTINCT, NESTED}

	private final Mode MODE = Mode.NESTED;
	private final int SIZE = 10000000;
	private final String FILENAME = MODE.toString() + SIZE;
	
	private final boolean GENERATE_ALL = true;


	public TestCityGen(){
		if(GENERATE_ALL){
			generateTestSet(10000000);
		}
		else{
			List<Building> city = generate(SIZE, MODE);
			writeToFile(city, FILENAME);
		}
	}


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


		}catch(IOException e){System.out.println("Error in writing to file " + e);}
	}

	private List<Building> generate(int size, Mode mode){
		List<Building> city = new ArrayList<>();

		double left = 0;
		double right = 0;
		double height = 0;

		switch(mode){
		case NORMAL:
			for(int i=0;i<size;i++){
				left = Math.random()*1000;
				right = left + (Math.random()*50);
				height = Math.random()*200;
				city.add(new Building(left, right, height));
			}
			break;

		case DISTINCT:
			for(int i=0;i<size;i++){
				left = right + Math.random()*20;
				right = left + (Math.random()*50);
				height = Math.random()*200;
				city.add(new Building(left, right, height));
			}
			break;

		case NESTED:
			for(int i=0;i<size;i++){
				left = left - Math.random()*10;
				right = right + (Math.random()*10);
				height = height + Math.random()*20;
				city.add(new Building(left, right, height));
			}
			break;
		}
		System.out.println("File Created");
		return city;
	}


	private void generateTestSet(int max){
		int increment = 1000000;
		int num = 1000000;
		while(num <= max){
			build(num, Mode.NORMAL, "NORMAL" + num);
			build(num, Mode.DISTINCT, "DISTINCT" + num);
			build(num, Mode.NESTED, "NESTED" + num);
			num = num + increment;
		}

	}

	private void build(int num, Mode m, String name){
		List<Building> city = generate(num, m);
		writeToFile(city, name);
	}








	public static void main(String[] args) {
		new TestCityGen();
		System.exit(0);
	}

}
