package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {
    final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);
    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(String apiKey, OkHttpClient client){
        this.apiKey = apiKey;
        this.client = client;
    }

    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException{
        Quote quote = new Quote();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("Successful response from Alpha Vantage API");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            JsonNode globalQuoteNode = jsonNode.get("Global Quote");

            if (globalQuoteNode != null) {
                quote = objectMapper.treeToValue(globalQuoteNode, Quote.class);
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                quote.setTimestamp(currentTimestamp);
            }

        } catch (InterruptedException e) {
            logger.error("Failed to receive response, InterruptedException: " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("Failed to parse JSON data, JsonMappingException: " + e.getMessage());
        } catch (JsonProcessingException e) {
            logger.error("Failed to process JSON data, JsonProcessingException: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Input/Output error, IOException: " + e.getMessage());
        }

        if(quote.getSymbol() == null || quote.getSymbol().isEmpty()){
            System.out.println("Symbol could not be found");
            return null;
        }
        return quote;
    }

    }



