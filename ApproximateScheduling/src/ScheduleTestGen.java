import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Generates test cases for scheduling problem.
 * @author littledani1
 *
 */

public class ScheduleTestGen {

	private final int SIZE = 1000;
	private final int INCREMENT = 100;

	public ScheduleTestGen(){
		runAllTests();
	}

	private void runAllTests(){
		for(int i=INCREMENT;i<SIZE;i+=INCREMENT){
			generateTest(i);
		}
	}

	private void generateTest(int size){
		List<String> jobs = new ArrayList<>();
		Random rand = new Random();

		for(int i=0;i<size;i++){
			if(rand.nextBoolean()){
				jobs.add("S");
			}
			else{
				jobs.add("C");
			}

		}

		writeToFile("Schd" + size, jobs);
	}


	private void writeToFile(String filename, List<String> jobs){
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

			for(int i=0;i<jobs.size();i++){
				bw.write(jobs.get(i));
			}

			bw.close();

			System.out.println(filename + " Created.");

		}catch(IOException e){System.out.println("Error in writing to file " + e);}
	}

	public static void main(String[] args) {
		new ScheduleTestGen();
		System.exit(0);
	}

}
