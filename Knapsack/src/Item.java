
public class Item{

	public final String name;
	public final int value;
	public final int weight;
	public final int ID;
	
	private static int IDcount = 0;

	public Item(String name, int value, int weight) {
		super();
		this.name = name;
		this.value = value;
		this.weight = weight;
		this.ID = IDcount;
		IDcount++;
	}

	public boolean equals(Object o){
		Item it = (Item)o;
		if(it.name.equals(name) && it.value == value && it.weight == weight && it.ID == ID){
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
