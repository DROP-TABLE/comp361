import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


	/**
	 * A dynamic programming implementation of the 0-1 knapsack problem
	 * @author littledani1
	 *
	 */

public class SimpleDynProKnapsack {

	private final int MAX_SIZE = 1000;
	private final int INCREMENT = 10;

	private final int MAX_WEIGHT = 100;

	private int barometer = 0;

	public SimpleDynProKnapsack(){

	}

	private void simpleKnapsack(){
		plotGraph();
	}

	private void runSingleTest(List<Item> items){
		Table table = buildTable(items);
		findMatching(table);
	}

	private Table buildTable(List<Item> items){
		Table table = new Table(items.size(), MAX_WEIGHT);
		barometer += table.getBarometer();
		for(int i=0;i<items.size();i++){
			for(int w=0;w<MAX_WEIGHT;w++){
				barometer++;
				if(items.get(i).weight < w){
					table.setCell(Math.max(table.getCell(i-1, w), table.getCell(i-1, w - items.get(i).weight) + items.get(i).value), i, w);
				}
			}
		}


		return table;
	}

	private void runTestToPlot(String name, String testname, Chart chart){
		List<Number> xData = new ArrayList<>();
		List<Number> yData = new ArrayList<>();
		for(int size=INCREMENT;size<MAX_SIZE;size+=INCREMENT){

			System.out.println("Running: " + testname + size);
			File file = new File("tests/" + testname + size + ".txt");
			barometer = 0;
			List<Item> items = loadFromFile(file);
			if(items != null){
				runSingleTest();
				yData.add(new Double(barometer));
	    		xData.add(new Double(size));
			}
			else{
				System.out.println("failed to load file: " + testname + size + ".txt");
			}

			List<Item> items = loadFromFile()
		}
		int size = INCREMENT;
		while(size < MAX_SIZE){
			System.out.println("Running: " + testname + size);
			File file = new File("tests/" + testname + size + ".txt");
			barometer = 0;
			if(loadFromFile(file)){
				runSingleTest();
				yData.add(new Double(barometer));
	    		xData.add(new Double(size));
			}
			else{
				System.out.println("failed to load file: " + testname + size + ".txt");
			}
			size = size + INCREMENT;
		}
		addSeries(name, xData, yData, chart);
	}

	public void plotGraph(){

		Chart chart = new Chart(800, 600);
	    chart.setChartTitle("Cost of Algorithm");
	    chart.setXAxisTitle("Size of Input");
	    chart.setYAxisTitle("Operations Required");


	    runTestsToPlot("0-1 Dynamic Programming Knapsack", "Single", chart);

	    saveGraph(chart);


	}

	public List<Item> loadFromFile(File file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] tokens;
			List<Item> items = new ArrayList<>();

			while((line = br.readLine()) != null){
				tokens = line.split(" ");
				for(int i=0;i<Integer.parseInt(tokens[3]);i++){
					items.add(new Item(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
				}
			}
			br.close();
			return items;
		}
		catch(IOException e){
			System.out.println("IO exception on file load: " + e);
			return null;
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
