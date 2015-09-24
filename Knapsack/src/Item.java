
public class Item{

	public final String name;
	public final double value;
	public final int weight;

	public Item(String name, double value, int weight) {
		super();
		this.name = name;
		this.value = value;
		this.weight = weight;
	}

	public boolean equals(Object o){
		Item it = (Item)o;
		if(it.name.equals(name) && it.value == value && it.weight == weight){
			return true;
		}
		return false;
	}

	public int compareTo(Object o){
		Item it = (Item)o;
		if(it.value/it.weight < value/weight){
			return 1;
		}
		else if(it.value/it.weight == value/weight){
			return 0;
		}
		else{
			return -1;
		}
	}
}
