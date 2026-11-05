import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("tiny.txt"));
        String symbol = in.next();
        int n = in.nextInt();
    
        int[] years = new int [n];
        int[] months = new int [n];
        double[] prices = new double [n];
        
		loadData (in, years, months, prices);
		System.out.println( symbol + " lowest: $" + prices[indexOfLowest(prices)] + " " + monthName(indexOfLowest(prices)+1) + " " + years[indexOfLowest(prices)]);
		System.out.println( symbol + " highest: $" + prices[indexOfHighest(prices)] + " " + monthName(indexOfHighest(prices)+1) + " " + years[indexOfHighest(prices)]);
       
		}
		public static void loadData(Scanner in, int[] years, int[] months, double[] prices){
			for (int i = 0; i < years.length; i++){
				years[i] = in.nextInt();
				months[i] = in.nextInt();
				prices[i] = in.nextDouble();
				
			}
		printData(years,months,prices);
			
		}
		public static String monthName(int month){
			String[] monthNames = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			return monthNames[month];
		}
	
		public static void printData(int[] years, int[] months, double[] prices){
			for (int i = 0; i < months.length; i++){
				System.out.print(monthName(i+1) + " " + years[i]+ ": $");
				System.out.printf("%.2f%n", prices[i]);
		}
	}
		
		public static int indexOfLowest(double[] prices){
			int lowest = 0;
			for (int i = 0; i < prices.length; i++){
				if (prices[i] < prices[lowest]){
					lowest = i;
					
					
				}
			}
			return lowest;
		}
			
		public static int indexOfHighest(double[] prices){
			int highest = 0;
			for (int i = 0; i < prices.length; i++){
				if (prices[i] > prices[highest]){
					highest = i;
					
				}
			}
			return highest;
				
}
}
