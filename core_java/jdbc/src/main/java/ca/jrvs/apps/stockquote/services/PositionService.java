package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;

import java.util.Optional;

public class PositionService {

    private PositionDao dao;

    public PositionService(PositionDao dao){
        this.dao = dao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        Position pos = new Position();

        pos.setTicker(ticker);
        pos.setNumOfShares(numberOfShares);
        pos.setValuePaid(price);

        return dao.save(pos);
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        Optional<Position> daoPos = dao.findById(ticker);
        if(daoPos.isEmpty()){
            System.out.println("You do not own this stock");
            return;
        }
        //Check if the position exist
        Position pos = daoPos.get();

        dao.deleteById(pos.getTicker());

    }

    /*public boolean verify(String symbol){
        return dao.verify(symbol);
    }*/
}

