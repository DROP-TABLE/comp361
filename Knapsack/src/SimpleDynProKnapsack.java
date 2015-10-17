import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;


	/**
	 * A dynamic programming implementation of the 0-1 knapsack problem
	 * @author littledani1
	 *
	 */

public class SimpleDynProKnapsack {

	private final int MAX_SIZE = 1000;
	private final int INCREMENT = 100;

	private int barometer = 0;

	public SimpleDynProKnapsack(){
		plotGraph();
		plotGraphQuadratic();
	}

	public void plotGraph(){

		Chart chart = new Chart(800, 600);
	    chart.setChartTitle("Cost of Algorithm");
	    chart.setXAxisTitle("Size of Input");
	    chart.setYAxisTitle("Operations Required");

	    runTestsToPlot("Max Weight = 100", "SIMPLE", 100, chart);
	    runTestsToPlot("Max Weight = 1000", "SIMPLE", 1000, chart);
	    saveGraph("Constant_Weight", chart);
	}
	
	public void plotGraphQuadratic(){

		Chart chart = new Chart(800, 600);
	    chart.setChartTitle("Cost of Algorithm");
	    chart.setXAxisTitle("Size of Input");
	    chart.setYAxisTitle("Operations Required");

	    runTestsToPlot("Max Weight = 100", "SIMPLE", 100, chart);
	    runTestsToPlot("Max Weight = 1000", "SIMPLE", 1000, chart);
	    runTestsToPlotQuad("Max Weight = Size of Input", "SIMPLE", chart);
	    saveGraph("Scaling_Weight", chart);
	}

	private void runSingleTest(List<Item> items, int maxweight){
		Table table = buildTable(items, maxweight);
		List<Item> solution = findSolution(table, items);
		printSolution(solution);
	}
	
	private void printSolution(List<Item> solution){
		for(Item it : solution){
			System.out.println("Used:" + it.name + ", wgt: " + it.weight + ", val: " + it.value);
		}
	}

	private Table buildTable(List<Item> items, int maxweight){
		Table table = new Table(items.size(), maxweight);
		barometer += table.getBarometer();
		for(int i=0;i<items.size();i++){
			for(int w=0;w<maxweight;w++){
				barometer++;
				if(items.get(i).weight < w){
					table.setCell(Math.max(table.getCell(i-1, w), table.getCell(i-1, w - items.get(i).weight) + items.get(i).value), i, w);
				}
			}
		}
		return table;
	}
	
	private List<Item> findSolution(Table table, List<Item> items){
		List<Item> solution = new ArrayList<>();
		int w = table.getWidth()-1;
		for(int i=table.getHeight()-1;i>0;i--){
			barometer++;
			if(w >= items.get(i).weight && table.getCell(i-1, w-items.get(i).weight) + items.get(i).value > table.getCell(i-1, w)){
				solution.add(items.get(i));
				w = w - items.get(i).weight;
			}
		}
		return solution;
	}

	private void runTestsToPlot(String name, String testname, int maxweight, Chart chart){
		List<Number> xData = new ArrayList<>();
		List<Number> yData = new ArrayList<>();
		for(int size=INCREMENT;size<=MAX_SIZE;size+=INCREMENT){

			System.out.println("Running: " + testname + size);
			File file = new File("tests/" + testname + size + ".txt");
			barometer = 0;
			List<Item> items = loadFromFile(file);
			if(items != null){
				runSingleTest(items, maxweight);
				yData.add(new Double(barometer));
	    		xData.add(new Double(size));
			}
			else{
				System.out.println("failed to load file: " + testname + size + ".txt");
			}

		}
		addSeries(name, xData, yData, chart);
	}
	
	private void runTestsToPlotQuad(String name, String testname, Chart chart){
		List<Number> xData = new ArrayList<>();
		List<Number> yData = new ArrayList<>();
		for(int size=INCREMENT;size<=MAX_SIZE;size+=INCREMENT){
			System.out.println("Running: " + testname + size);
			File file = new File("tests/" + testname + size + ".txt");
			barometer = 0;
			List<Item> items = loadFromFile(file);
			if(items != null){
				runSingleTest(items, size);
				yData.add(new Double(barometer));
	    		xData.add(new Double(size));
			}
			else{
				System.out.println("failed to load file: " + testname + size + ".txt");
			}
		}
		addSeries(name, xData, yData, chart);
	}
	
	private void addSeries(String name, List<Number> xData, List<Number> yData, Chart chart){
		Series series = chart.addSeries(name, xData, yData);
	    series.setMarker(SeriesMarker.CIRCLE);
	}
	
	private void saveGraph(String name, Chart chart){
		try{
			BitmapEncoder.saveBitmap(chart, "./" + name, BitmapFormat.PNG);
		}catch(IOException e){System.out.println("failed to save chart: " + e);}
	}

	public List<Item> loadFromFile(File file){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] tokens;
			List<Item> items = new ArrayList<>();

			while((line = br.readLine()) != null){
				tokens = line.split(" ");
				//for(int i=0;i<Integer.parseInt(tokens[3]);i++){
					items.add(new Item(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
				//}
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
		new SimpleDynProKnapsack();

	}

}
