package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Quote {
    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("02. open")
    private double open;

    @JsonProperty("03. high")
    private double high;

    @JsonProperty("04. low")
    private double low;

    @JsonProperty("05. price")
    private double price;

    @JsonProperty("06. volume")
    private int volume;

    @JsonProperty("07. latest trading day")
    private Date latestTradingDay;

    @JsonProperty("08. previous close")
    private double previousClose;

    @JsonProperty("09. change")
    private double change;

    @JsonProperty("10. change percent")
    private String changePercent;

    private Timestamp timestamp; //time when the info was pulled

    // Getters

    public String getSymbol() {
        return symbol;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public Date getLatestTradingDay() {
        return latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public double getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    // Setters

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    // toString method

    @Override
    public String toString() {
        return "Quote{" +
                "symbol='" + symbol + '\'' +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", price='" + price + '\'' +
                ", volume='" + volume + '\'' +
                ", latestTradingDay='" + latestTradingDay + '\'' +
                ", previousClose='" + previousClose + '\'' +
                ", change='" + change + '\'' +
                ", changePercent='" + changePercent + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}