package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.services.QuoteService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class QuoteService_IntTest {

    private QuoteDao mockDao;
    private QuoteHttpHelper mockHttpHelper;
    private QuoteService quoteService;

    private Connection c;
    @Before
    public void setup() {
        mockDao = mock(QuoteDao.class);
        mockHttpHelper = mock(QuoteHttpHelper.class);
        quoteService = new QuoteService(mockDao, mockHttpHelper);
    }

    @Test
    public void testFetchQuoteDataFromAPI() {
        String symbol = "MFST";
        Quote mockQuote = new Quote();
        when(mockHttpHelper.fetchQuoteInfo(symbol)).thenReturn(mockQuote);

        Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(symbol);
        Quote testQuote = result.get();
        assertEquals(mockQuote.getSymbol(), testQuote.getSymbol());
        verify(mockHttpHelper).fetchQuoteInfo(symbol);
    }
}