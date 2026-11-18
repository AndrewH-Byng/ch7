import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


//Buy and Hold won on the most files, mostly because of fees: it only pays a $\$10$ fee once at the very start. Active strategies trade over and over again, and those $\$10$ fees add up and quietly drain all the profits.
//On a stock that constantly goes down like falling.txt, the dip buyer gets confused. The stock drops two months in a row, so the strategy thinks there's a dip so they buy more. But the stock just keeps crashing forever.
//On a stock like amzn_2000_2010.txt that had a huge crash followed by a long recovery, Buy and Hold forced you to ride the stock all the way down to the bottom. The Moving Average strategy, however, was smart enough to sell early, and buy back in only when things started going back up
//My strategy tried to play it safe, but tweaking the rules to make it win on one specific file usually made it worse on the other files because every stock behaves completely differently



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
    public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
        for (int i = 0; i < years.length; i++) {
            years[i] = in.nextInt();
            months[i] = in.nextInt();
            prices[i] = in.nextDouble();
        }
        printData(years, months, prices);
    }

	public static String monthName(int month){
			String[] monthNames = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			return monthNames[month];
		}
    public static void printData(int[] years, int[] months, double[] prices) {
        for (int i = 0; i < months.length; i++) {
            System.out.print(monthName(months[i]) + " " + years[i] + ": $");
            System.out.printf("%.2f%n", prices[i]);
        }
    }
    public static int indexOfLowest(double[] prices) {
        int lowestIndex = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[lowestIndex]) {
                lowestIndex = i;
            }
        }
        return lowestIndex;
    }

    public static int indexOfHighest(double[] prices) {
        int highestIndex = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[highestIndex]) {
                highestIndex = i;
            }
        }
        return highestIndex;
    }

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
                    System.out.printf("%s %d: BUY %d shares at $%.2f%n", monthName(months[i]),
                        years[i], shares, prices[i]);
                }
            } else if (action.equals("SELL") && shares > 0) {
                cash = cash + shares * prices[i] - FEE;
                System.out.printf("%s %d: SELL %d shares at $%.2f%n", monthName(months[i]),
                    years[i], shares, prices[i]);
                shares = 0;
                boughtAt = 0.0;
            }
        }
        return cash + shares * prices[prices.length - 1];
    }

    public static final double START_CASH = 10000.0;
    public static final double FEE = 10.0;
    public static String decide(double[] history, boolean holding, double boughtAt) {
        return movingAverage(history, holding, boughtAt);
    }


    public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
        if (!holding) {
            if (history.length >= 3) {
                double today = history[history.length - 1];
                double lastMonth = history[history.length - 2];
                double twoMonthsAgo = history[history.length - 3];
                if (today < lastMonth && lastMonth < twoMonthsAgo) {
                    return "BUY";
                }
            }
        } else {
            double today = history[history.length - 1];
            if (today >= boughtAt * 1.10) {
                return "SELL";
            }
        }
        return "HOLD";
    }

    
    public static String movingAverage(double[] history, boolean holding, double boughtAt) {
        if (history.length < 12) {
            return "HOLD";
        }
        double sum = 0.0;
        for (int i = history.length - 12; i < history.length; i++) {
            sum += history[i];
        }
        double avg = sum / 12.0;
        double today = history[history.length - 1];
        if (!holding && today > avg) {
            return "BUY";
        } else if (holding && today < avg) {
            return "SELL";
        }
        return "HOLD";
    }

    // Step D: Custom Strategy (Momentum & Risk-Management / Stop-Loss)
    // Rules:
    // 1. If not holding, buy if price is above its 5-month moving average and has gone up for 2 consecutive months.
    // 2. If holding, sell if price drops 5% below purchase price (stop loss) or gains 15% (take profit).
    public static String myStrategy(double[] history, boolean holding, double boughtAt) {
        double today = history[history.length - 1];
        if (!holding) {
            if (history.length >= 5) {
                double sum = 0;
                for (int i = history.length - 5; i < history.length; i++) {
                    sum += history[i];
                }
                double ma5 = sum / 5.0;
                if (today > ma5 && today > history[history.length - 2] && history[history.length - 2] > history[history.length - 3]) {
                    return "BUY";
                }
            }
        } else {
            if (today <= boughtAt * 0.95 || today >= boughtAt * 1.15) {
                return "SELL";
            }
        }
        return "HOLD";
    }

    
	
		}
	
