import java.util.ArrayList;
import java.util.List;


public class ManhattenSkyline {


	public ManhattenSkyline(){

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
			SkylineTuple elem = sky1.get(i);
			i++;
			if(ho != elem.getHeight()){
				skyline.add(elem);
			}
		}
		while(j < sky2.size()){
			SkylineTuple elem = sky2.get(j);
			j++;
			if(ho != elem.getHeight()){
				skyline.add(elem);
			}
		}

		return skyline;
	}


	/*public List<SkylineTuple> buildSkyline(List<Building> input){
		stack.push(new StackTuple(0, input.size()));
		while(!stack.isEmpty()){
			StackTuple next = stack.pop();
			int start = next.getStart();
			int end = next.getEnd();

			if(start == end){
				Building elem = input.get(start);
				List<SkylineTuple> skyline = new ArrayList<>();
				skyline.add(new SkylineTuple(elem.getLeft(), elem.getHeight()));
				skyline.add(new SkylineTuple(elem.getRight(), 0));
				return skyline;
			}
			else{
				stack.push(new StackTuple(start, (start+end)/2));
				stack.push(new StackTuple((start+end)/2 + 1, end));
			}


		}
		return null;
	}*/


}
