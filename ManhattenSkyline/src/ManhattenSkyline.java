
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class ManhattenSkyline {

	private List<Building> city;

	private int barometer = 0;

	public ManhattenSkyline(){
		city = load();
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



	public List<Building> load(){

		final JFrame frame = new JFrame();
		final JFileChooser fileChooser = new JFileChooser();
		File file = null;
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Select input file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		else {return null; }

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

	public static void main(String[] args){
		new ManhattenSkyline();
		System.exit(0);
	}

}
