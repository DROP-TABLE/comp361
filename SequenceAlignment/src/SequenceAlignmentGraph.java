import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;

public class SequenceAlignmentGraph {

	private final int MAX_SIZE = 1000;
	private final int INCREMENT = 10;

	private int barometer;

	private List<String> input1;
	private List<String> input2;



	public SequenceAlignmentGraph(){
		plotGraph();
	}

	private void runSingleTest(){
		Table table = buildTable();
		findMatching(table);
	}

	private void runTestsToPlot(String name, String testname, Chart chart){
		List<Number> xData = new ArrayList<>();
		List<Number> yData = new ArrayList<>();
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
	    
	    
	    runTestsToPlot("Average Case", "NORMAL", chart);
	    runTestsToPlot("Worst Case", "WORST", chart);
	    runTestsToPlot("Best Case", "BEST", chart);
	    runTestsToPlot("Fixed size of 1 list", "FIXED1D", chart);
	    
	    saveGraph(chart);
	    
		
	}
	
	private void addSeries(String name, List<Number> xData, List<Number> yData, Chart chart){
		Series series = chart.addSeries(name, xData, yData);
	    series.setMarker(SeriesMarker.CIRCLE);
	}
	
	private void saveGraph(Chart chart){
		try{
			BitmapEncoder.saveBitmap(chart, "./Cost_Plot", BitmapFormat.PNG);
		}catch(IOException e){System.out.println("failed to save chart: " + e);}
	}



	private Table buildTable(){
		Table table = new Table(input2.size(), input1.size());
		barometer += table.getBarometer();
		for(int j=0;j<input1.size();j++){
			for(int i=0;i<input2.size();i++){
				barometer++;
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

	private void findMatching(Table table){
		List<Pair> matchingList = new ArrayList<>();

		int j = input1.size()-1;
		int i = input2.size()-1;
		if(input1.get(j).equals(input2.get(i))){
			matchingList.add(new Pair(input1.get(j), input2.get(i)));
		}
		while(i > 0 && j > 0){
			barometer++;
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
		//return new Matching(matchingList, input1, input2);
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
		new SequenceAlignmentGraph();
		System.exit(0);
	}

}

