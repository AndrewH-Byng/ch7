import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("sp500_1990_2025.txt"));
        String symbol = in.next();
        int n = in.nextInt();
    
        int[] years = new int [n];
        int[] months = new int [n];
        double[] prices = new double [n];

        
		loadData (in, years, months, prices);
		System.out.println( symbol + " lowest: $" + prices[indexOfLowest(prices)] + " " + monthName(months[indexOfLowest(prices)]) + " " + years[indexOfLowest(prices)]);
		System.out.println( symbol + " highest: $" + prices[indexOfHighest(prices)] + " " + monthName(months[indexOfHighest(prices)]) + " " + years[indexOfHighest(prices)]);
		double result = simulate(years, months, prices);
		System.out.printf("Final value: $%,.2f%n", result);
		
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
				System.out.print(monthName(months[i]) + " " + years[i]+ ": $");
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
		public static final double START_CASH = 10000.0;
		public static final double FEE = 10.0;
		
		public static double simulate(int[] years, int[] months, double[] prices) {
			double cash = START_CASH;
			int shares = 0;
			double boughtAt = 0.0;

			for (int i = 0; i < prices.length; i++) {
				double[] history = Arrays.copyOf(prices, i + 1);
				String action = decide(history, shares > 0, boughtAt);

				if (action.equals("BUY") && shares == 0) {
					int canBuy = (int) ((cash - FEE) / prices[i]);
					if (canBuy > 0) {
						shares = canBuy;
						cash = cash - shares * prices[i] - FEE;
						boughtAt = prices[i];
						System.out.printf("%s %d: BUY %d shares at $%.2f%n",
								monthName(months[i]), years[i], shares, prices[i]);
					}
				} else if (action.equals("SELL") && shares > 0) {
					cash = cash + shares * prices[i] - FEE;
					System.out.printf("%s %d: SELL %d shares at $%.2f%n",
							monthName(months[i]), years[i], shares, prices[i]);
					shares = 0;
					boughtAt = 0.0;
				}
				
			}
				return cash + shares * prices[prices.length - 1];
			}
		
		public static String decide(double[] history, boolean holding, double boughtAt) {
		return buyAndHold(history, holding, boughtAt); 
}
}
