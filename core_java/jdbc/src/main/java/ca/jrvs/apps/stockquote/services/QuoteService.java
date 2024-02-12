package ca.jrvs.apps.stockquote.services;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;

import java.util.Optional;

public class QuoteService {

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao qRepo , QuoteHttpHelper pRepo){
        dao = qRepo;
        httpHelper = pRepo;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        return Optional.ofNullable(httpHelper.fetchQuoteInfo(ticker));
    }

    public void save(Quote quote) {
        dao.save(quote);
    }

    public Optional<Quote> find(String symbol) {
        return dao.findById(symbol);
    }

    public void delete(String symbol) {
        dao.deleteById(symbol);
    }

    public Iterable<Quote> findAll() {
        return dao.findAll();
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    /*public boolean verify(String symbol){
        return dao.verify(symbol);
    }*/
}