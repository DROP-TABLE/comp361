
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.BitmapEncoder.BitmapFormat;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.VectorGraphicsEncoder;
import com.xeiam.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;


public class ManhattenSkyline {

	private List<Building> city;
	private Chart chart;
	
	private final String[] NORMALTESTS = {"NORMAL10", "NORMAL100", "NORMAL1000", "NORMAL10000", "NORMAL100000", "NORMAL1000000", "NORMAL10000000"};
	private final String[] DISTINCTTESTS = {"DISTINCT10", "DISTINCT100", "DISTINCT1000", "DISTINCT10000", "DISTINCT100000", "DISTINCT1000000", "DISTINCT10000000"};
	private final String[] NESTEDTESTS = {"NESTED10", "NESTED100", "NESTED1000", "NESTED10000", "NESTED100000", "NESTED1000000", "NESTED10000000"};
	private final Double[] TESTPATTERN = {10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 10000000.0};
	
	private int barometer = 0;
	
	private final boolean MAKE_GRAPHS = true;

	public ManhattenSkyline(){
		if(MAKE_GRAPHS){
			plotGraph();
		}
		else{
			city = load(null);
		}
		if(city != null){
			List<SkylineTuple> skyline = buildSkyline(city, 0, city.size()-1);
			//display(skyline);
			save(skyline);
			System.out.println("Barometer: " + barometer);
		}
	}



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

	private List<SkylineTuple> merge(List<SkylineTuple> sky1, List<SkylineTuple> sky2){
		int i = 0;
		int j = 0;
		double h1 = 0;
		double h2 = 0;
		double ho = 0;
		double x = 0;
		List<SkylineTuple> skyline = new ArrayList<>();
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
			}
		}
		while(j < sky2.size()){
			barometer++;
			SkylineTuple elem = sky2.get(j);
			j++;
			if(ho != elem.getHeight()){
				skyline.add(elem);
			}
		}

		return skyline;
	}

	private void display(List<SkylineTuple> skyline){
		for(SkylineTuple tuple : skyline){
			System.out.println("(" + tuple.getX() + ", " + tuple.getHeight() + ")");
		}
	}

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



	public List<Building> load(File file){
		
		if(file == null){
			final JFrame frame = new JFrame();
			final JFileChooser fileChooser = new JFileChooser();
			file = null;
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setDialogTitle("Select input file");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
			else {return null; }
		}

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
	
	public void plotGraph(){
		
		chart = new Chart(800, 600);
	    chart.setChartTitle("Cost of Algorithm");
	    chart.setXAxisTitle("Size of Input");
	    chart.setYAxisTitle("Operations Required");
	    //chart.getStyleManager().setAxisTickMarkLength(1000);
	    
	    
	    runTestsToPlot("Average Case", "NORMAL");
	    runTestsToPlot("Nested (Best) Case", "NESTED");
	    runTestsToPlot("Distinct (Worst) Case", "DISTINCT");
	    
	    saveGraph();
	    
		
	}
	
	private void runTestsToPlot(String name, String test){
		List<Number> yData = new ArrayList<>();
		//List<Number> xData = new ArrayList<>(Arrays.asList(TESTPATTERN));
		List<Number> xData = new ArrayList<>();
		
		for(int i=1;i<11;i++){
			barometer = 0;
			city = load(new File("tests/" + test + i*1000000 + ".txt"));
	    	if(city != null){
	    		buildSkyline(city, 0, city.size()-1);
	    		yData.add(new Double(barometer));
	    		xData.add(new Double(i*1000000));
	    	}
	    	else{
	    		System.out.println("Load not successful on " + test + i*1000000);
	    		//return;
	    	}
	    }
		addSeries(name, xData, yData);
	}
	
	private void addSeries(String name, List<Number> xData, List<Number> yData){
		Series series = chart.addSeries(name, xData, yData);
	    series.setMarker(SeriesMarker.CIRCLE);
	}
	
	private void saveGraph(){
		try{
			BitmapEncoder.saveBitmap(chart, "./Cost_Plot", BitmapFormat.PNG);
			chart.getStyleManager().setXAxisLogarithmic(true);
			BitmapEncoder.saveBitmap(chart, "./Cost_Plot_log", BitmapFormat.PNG);
		}catch(IOException e){System.out.println("failed to save chart: " + e);}
	}

	public static void main(String[] args){
		new ManhattenSkyline();
		System.exit(0);
	}

}
