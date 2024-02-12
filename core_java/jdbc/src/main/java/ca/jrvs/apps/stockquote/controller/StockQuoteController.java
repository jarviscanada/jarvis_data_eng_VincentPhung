package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.services.PositionService;
import ca.jrvs.apps.stockquote.services.QuoteService;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockQuoteController {
    static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
    private QuoteService sQuote;
    private PositionService sPos;

    public StockQuoteController(QuoteService sQuote, PositionService sPos){
        this.sQuote = sQuote;
        this.sPos = sPos;

    }

    public static void client(String[] args) {
        Map<String, String> properties = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (FileNotFoundException e) {
            logger.error("Could not find properties file, FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error with properities file, IOException " + e.getMessage());
        }

        try {
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e) {
            logger.error("Database not found");
        }
        OkHttpClient client = new OkHttpClient();
        String url = "jdbc:postgresql://"+properties.get("server")+":"+properties.get("port")+"/"+properties.get("database");
        try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            QuoteDao qRepo = new QuoteDao(c);
            PositionDao pRepo = new PositionDao(c);
            QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
            QuoteService sQuote = new QuoteService(qRepo, rcon);
            PositionService sPos = new PositionService(pRepo);
            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            con.initClient();
        } catch (SQLException e) {
            logger.error("Failed to establish connection to database, SQLException: " + e.getMessage());
        }
    }

    //User-Interface
    private void initClient() throws SQLException {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Possible actions: info, delete, buy, sell, view, viewall.");
        System.out.println("Notes: info is to see a ticker's info, view is for viewing user transactions");
        System.out.println("Notes: delete is for deleting quote data, stored from calling info");
        System.out.println("Notes: Buy format is TICKER BUY NumberOfShares Price, sell will sell the entire position");

        while(true){
            System.out.println("Enter a ticker followed by an action (e.g., APPL Sell), type 'exit' if you want to exit ");
            String input = userInput.nextLine().trim();

            if (input.equalsIgnoreCase("exit")){
                break;
            }

            // Split input into ticker and action
            String[] parts = input.split("\\s+", 5); // Split at whitespace
            if (parts.length < 2 || parts.length >= 5) {
                System.out.println("Invalid input format. Please enter ticker and action separated by space.");
                continue;
            }

            String ticker = parts[0].toUpperCase();
            String action = parts[1];

            // Perform action based on user input
            switch (action.toUpperCase()) {
                case "BUY":
                    if (parts.length != 4){
                        System.out.println("Insufficient arguments provided for BUY");
                        break;
                    }

                    System.out.println("Buying stock " + ticker);
                    int numOfShares = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    sPos.buy(ticker,numOfShares,price);
                    break;
                case "SELL":
                    System.out.println("Selling stock " + ticker);
                    sPos.sell(ticker);
                    break;
                case "INFO":
                    Optional<Quote> quoteInfo = sQuote.fetchQuoteDataFromAPI(ticker);

                    if (quoteInfo.isPresent()){
                        System.out.println(ticker + " stock data: \n");
                        Quote quoteData = quoteInfo.get();
                        sQuote.save(quoteData);
                        System.out.println(quoteData);
                    } else {
                        System.out.print("Ticker could not be found");
                    }
                    break;
                case "VIEW":
                    Optional<Quote> quoteView = sQuote.find(ticker);
                    if(quoteView.isPresent()){
                        System.out.println(" " + quoteView.get());
                    } else{
                        System.out.println("You have not called INFO on this quote before, hence it is not saved.");
                    }
                    break;
                case "VIEWALL":
                    for (Quote quote : sQuote.findAll()){
                        System.out.println(quote);
                        }
                    break;
                case "DELETE":
                    sQuote.delete(ticker);
                    break;
                default:
                    System.out.println("Unknown action: " + action + ". Please try again.");
            }
                System.out.println("");

        }
        userInput.close();

    }

}
