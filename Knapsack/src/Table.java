
public class Table {

	private double[][] table;

	private int barometer;

	public Table(int m, int n){
		table = new double[m+1][n+1];
		for(int j=0;j<n+1;j++){
			table[0][j] = -j*2;
			barometer++;
		}
		for(int i=0;i<m+1;i++){
			table[i][0] = -i*2;
			barometer++;
		}
	}

	/**
	 * Used to track initialization costs of table.
	 * @return number of loops in initialization.
	 */

	public int getBarometer(){
		return barometer;
	}

	public boolean setCell(double value, int height, int width){
		if(height < 0 || width < 0){
			System.out.println("Cannot set at negative indices");
			return false;
		}
		table[height+1][width+1] = value;  //the indices are increased by 1 to leave a base row to the table.
		return true;
	}

	public double getCell(int height, int width){
		return table[height+1][width+1];
	}
	
	public int getHeight(){
		return table.length-1;
	}
	
	public int getWidth(){
		return table[0].length-1;
	}

	public void printTable(){
		for(int i=0;i<table.length;i++){
			for(int j=0;j<table[0].length;j++){
				if(table[i][j] > 9){
					System.out.print(table[i][j] + "  ");
				}
				else if(table[i][j] <= 9 && table[i][j] > -1){
					System.out.print(table[i][j] + "   ");
				}
				else if(table[i][j] <= -1 && table[i][j] > -10){
					System.out.print(table[i][j] + "  ");
				}
				else{
					System.out.print(table[i][j] + " ");
				}
			}
			System.out.println();
		}
	}
}
