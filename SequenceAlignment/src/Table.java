
public class Table {

	private int[][] table;
	
	public Table(int m, int n){
		table = new int[m+1][n+1];
		for(int j=0;j<n;j++){
			table[0][j] = -j*2;
		}
		for(int i=0;i<m;i++){
			table[i][0] = -i*2;
		}
	}
	
	public boolean setCell(int value, int height, int width){
		if(height < 0 || width < 0){
			System.out.println("Cannot set at negative indices");
			return false;
		}
		table[height+1][width+1] = value;  //the indices are increased by 1 to leave a base row to the table.
		return true;
	}
	
	public int getCell(int height, int width){
		return table[height+1][width+1];
	}
	
}