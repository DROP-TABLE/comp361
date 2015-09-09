import java.util.ArrayList;
import java.util.List;


public class Matching {

	private List<String> processedOutput1;
	private List<String> processedOutput2;
	private List<String> processedOutputComparison;
	private int totalValue;

	public Matching(List<Pair> matchingList, List<String> input1, List<String> input2){
		processMatching(matchingList, input1, input2);
	}

	public List<String> getProcessedOutput1(){
		return processedOutput1;
	}

	public List<String> getProcessedOutput2(){
		return processedOutput2;
	}

	public List<String> getProcessedOutputComparison(){
		return processedOutputComparison;
	}

	public int getTotalValue(){
		return totalValue;
	}

	private void processMatching(List<Pair> matchingList, List<String> input1, List<String> input2){
		processedOutput1 = new ArrayList<>();
		processedOutput2 = new ArrayList<>();
		processedOutputComparison = new ArrayList<>();
		int count = 0;
		int j = 0;
		int i = 0;
		while(j < input1.size() && i < input2.size() && count < matchingList.size()){

			if(input1.get(j).equals(matchingList.get(count).getFirstElem()) && input2.get(i).equals(matchingList.get(count).getSecondElem())){
				processedOutput1.add(input1.get(j));
				processedOutput2.add(input2.get(i));
				j++;
				i++;
				count++;
			}
			else if(input1.get(j).equals(matchingList.get(count).getFirstElem())){
				//while(!input2.get(i).equals(matching.get(count).getSecondElem()) && j < input1.size()){
				processedOutput1.add(" ");
				processedOutput2.add(input2.get(i));
					i++;
				//}
			}
			else if(input2.get(i).equals(matchingList.get(count).getSecondElem())){
				//while(!input1.get(j).equals(matching.get(count).getFirstElem()) && i < input2.size()){
				processedOutput1.add(input1.get(j));
				processedOutput2.add(" ");
					j++;
				//}
			}
			else{
				System.out.println("invalid matching");
				break;
			}
		}

		while(j < input1.size()){  //add rest of input1 to array
			processedOutput1.add(input1.get(j));
			processedOutput2.add(" ");
			j++;
		}

		while(i< input2.size()){ //add rest of input2 to array
			processedOutput1.add(" ");
			processedOutput2.add(input2.get(i));
			i++;
		}

		for(int k=0;k<processedOutput1.size();k++){
			if(processedOutput1.get(k).equals(processedOutput2.get(k))){
				processedOutputComparison.add("+");
				totalValue = totalValue + 1;
			}
			else if(processedOutput1.get(k).equals(" ") || processedOutput2.get(k).equals(" ")){
				processedOutputComparison.add("*");
				totalValue = totalValue - 2;
			}
			else{
				processedOutputComparison.add("-");
				totalValue = totalValue - 1;
			}
		}
	}

}
