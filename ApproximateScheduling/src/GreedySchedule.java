import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GreedySchedule {

	private final int MAX_SIZE = 1000;
	private final int INCREMENT = 100;


	public enum Job {
		SIMPLE, COMPLEX
	}

	public GreedySchedule(){
		runTests();
	}

	private void runTests(){
		for(int i=INCREMENT;i<MAX_SIZE;i+=INCREMENT){
			List<Job> jobs = loadFromFile(new File("tests/Schd" + i + ".txt"));
			int temp = findSolution(5, jobs);
			System.out.println(temp);
		}
	}

	private int findSolution(int m, List<Job> jobs){
		List<List<Job>> schd = new ArrayList<>();
		int[] runtime = new int[m];
		for(int i=0;i<m;i++){
			schd.add(new ArrayList<>());
		}
		for(int i=0;i<jobs.size();i++){
			int min = Integer.MAX_VALUE;
			int minloc = 0;
			for(int j=0;j<runtime.length;j++){
				if(runtime[j] < min){
					min = runtime[j];
					minloc = j;
				}
			}
			schd.get(minloc).add(jobs.get(i));
			if(jobs.get(i).equals(Job.SIMPLE)){
				runtime[minloc] = runtime[minloc] + 5;
			}
			else{
				runtime[minloc] = runtime[minloc] + 10;
			}
		}

		int max = 0;
		for(int i=0;i<runtime.length;i++){
			if(max < runtime[i]){
				max = runtime[i];
			}
		}
		return max;
	}


	public List<Job> loadFromFile(File file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] tokens;
			List<Job> input = new ArrayList<>();
			if((line = br.readLine()) != null){
				tokens = line.split("");
				for(int i=0;i<tokens.length;i++){
					if("S".equals(tokens[i])){
						input.add(Job.SIMPLE);
					}
					else if("C".equals(tokens[i])){
						input.add(Job.COMPLEX);
					}
					else{
						System.out.println("Unexpected token: " + tokens[i]);
						br.close();
						return null;
					}
				}
			}
			else{
				System.out.println("Failed to load from file - no text found");
				br.close();
				return null;
			}
			br.close();
			return input;
		}
		catch(IOException e){
			System.out.println("IO exception on file load: " + e);
			return null;
		}
	}








	public static void main(String[] args) {
		new GreedySchedule();
		System.exit(0);

	}

}
