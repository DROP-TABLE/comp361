
import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;


public class ManhattanSkyline {

	private Chart chart;
	
	private int barometer = 0;
	
	private final int INCREMENT = 10000;
	private final int MAX_TESTS = 50;
	
	/**
	 * This class is used to run the algorithm on previously generated test data.
	 * Will read test case from folder "tests/" and output a graph of all test cases run at "Cost_Plot.png"
	 * 
	 * To work correctly, INCREMENT and MAX_TESTS must be set to match the test cases to be used.
	 * 
	 * To view to output, a utility method to save to file is included at the bottom of the file.
	 */

	public ManhattanSkyline(){
		plotGraph();
	}

	/**
	 * Main body of algorithm.
	 * @param input: The list of buildings to be processed.
	 * @param start: The start of the sublist to work on.
	 * @param end: The end of the sublist to be worked on.
	 * @return The merged skyline from the two returned in the combine stage.
	 */

	public List<SkylineTuple> buildSkyline(List<Building> input, int start, int end){
		if(start == end){
			Building elem = input.get(start);
			List<SkylineTuple> skyline = new ArrayList<>();
			skyline.add(new SkylineTuple(elem.getLeft(), elem.getHeight()));
			skyline.add(new SkylineTuple(elem.getRight(), 0));
			return skyline;
		}
		else{
			List<SkylineTuple> sky1 = buildSkyline(input, start, (start+end)/2);
			List<SkylineTuple> sky2 = buildSkyline(input, (start+end)/2 + 1, end);

			return merge(sky1, sky2);
		}
	}
	
	/**
	 * Merges two skylines in linear time.  Does the main work for the combine stage of the algorithm.
	 * @param sky1: The first skyline to be merged.
	 * @param sky2: The second skyline to be merged.
	 * @return The merged skylines.
	 */

	private List<SkylineTuple> merge(List<SkylineTuple> sky1, List<SkylineTuple> sky2){
		int i = 0;
		int j = 0;
		double h1 = 0;
		double h2 = 0;
		double ho = 0;
		double x = 0;
		List<SkylineTuple> skyline = new ArrayList<>(sky1.size()+sky2.size());
		while(i < sky1.size() && j < sky2.size()){
			barometer++;
			if(sky1.get(i).getX() < sky2.get(j).getX()){
				SkylineTuple elem = sky1.get(i);
				h1 = elem.getHeight();
				x = elem.getX();
				i++;
			}
			else{
				SkylineTuple elem = sky2.get(j);
				h2 = elem.getHeight();
				x = elem.getX();
				j++;
			}

			if(ho != Math.max(h1, h2)){
				skyline.add(new SkylineTuple(x, Math.max(h1, h2)));
				ho = Math.max(h1, h2);
			}
		}
		while(i < sky1.size()){
			barometer++;
			SkylineTuple elem = sky1.get(i);
			i++;
			if(ho != elem.getHeight()){
				skyline.add(elem);
				ho = elem.getHeight();
			}
		}
		while(j < sky2.size()){
			barometer++;
			SkylineTuple elem = sky2.get(j);
			j++;
			if(ho != elem.getHeight()){
				skyline.add(elem);
				ho = elem.getHeight();
			}
		}

		return skyline;
	}

	/**
	 * Loads a city from file.  Assumes space separated values, arranged (Left Right Height) with one building per line.
	 * @param file: File to load from.
	 * @return ArrayList of Building elements.
	 */

	public List<Building> load(File file){
		try{
			List<Building> city = new ArrayList<>();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] tokens;
			while((line = br.readLine()) != null){
				tokens = line.split(" ");
				city.add(new Building(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2])));
			}
			br.close();
			return city;
		}
		catch(IOException e){
			System.out.println("IO exception on file load: " + e);
			return null;
		}
	}
	
	/**
	 * Sets up the graph.
	 */
	
	public void plotGraph(){
		chart = new Chart(800, 600);
	    chart.setChartTitle("Cost of Algorithm");
	    chart.setXAxisTitle("Size of Input");
	    chart.setYAxisTitle("Operations Required");
	    
	    runTestsToPlot("Average Case", "NORMAL");
	    runTestsToPlot("Nested (Best) Case", "NESTED");
	    runTestsToPlot("Distinct (Worst) Case", "DISTINCT");
	    runTestsToPlot("Alternate Random Data", "NORMALALT");
	    
	    saveGraph();
	}
	
	/**
	 * Runs all the tests of specified type.  Test type must match name of file (typically NORMAL, DISCRETE or NESTED).
	 * @param name: Name of the series being plotted.
	 * @param test: Type of test being plotted.
	 */
	
	private void runTestsToPlot(String name, String test){
		List<Number> yData = new ArrayList<>();
		List<Number> xData = new ArrayList<>();
		
		for(int i=1;i<MAX_TESTS+1;i++){
			List<Building> city;
			barometer = 0;
			city = load(new File("tests/" + test + i*INCREMENT + ".txt"));
	    	if(city != null){
	    		buildSkyline(city, 0, city.size()-1);
	    		yData.add(new Double(barometer));
	    		xData.add(new Double(i*INCREMENT));
	    		System.out.println(name + " " + i*INCREMENT + ": " + barometer);
	    	}
	    	else{
	    		System.out.println("Load not successful on " + test + i*INCREMENT);
	    		return;
	    	}
	    }
		addSeries(name, xData, yData);
	}
	
	/**
	 * Adds series to graph.
	 * @param name: Name of series to add.
	 * @param xData: X axis points of series.
	 * @param yData: Y axis points of series.
	 */
	
	private void addSeries(String name, List<Number> xData, List<Number> yData){
		Series series = chart.addSeries(name, xData, yData);
	    series.setMarker(SeriesMarker.CIRCLE);
	}
	
	/**
	 * Saves the generated graph to file as "Cost_Plot.png"
	 */
	
	private void saveGraph(){
		try{
			BitmapEncoder.saveBitmap(chart, "./Cost_Plot", BitmapFormat.PNG);
		}catch(IOException e){System.out.println("failed to save chart: " + e);}
	}

	public static void main(String[] args){
		new ManhattanSkyline();
		System.exit(0);
	}
	
	/**
	 * Unused utility method to save output of algorithm to file.
	 * Generates a .txt file at "output.txt" with (X value, new height) for each tuple.
	 * @param skyline: The output from the algorithm to be saved.
	 */
	
	/*
	 private void save(List<SkylineTuple> skyline){
		try{
			File file = new File("output.txt");
			if(file.exists()){ //removes file if exists to clear.
				file.delete();
			}
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(SkylineTuple tuple : skyline){
				bw.write("(" + tuple.getX() + ", " + tuple.getHeight() + ")");
				bw.newLine();
			}
			bw.close();
			System.out.println("File Created");

		}catch(IOException e){System.out.println("Failed to write to file " + e);}
	}
	*/

}
