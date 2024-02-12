package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.services.PositionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PositionService_IntTest {
    @Mock
    private PositionDao mockDao;
    private PositionService positionService;

    private Connection c;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        positionService = new PositionService(mockDao);
    }

    @Test
    public void testBuy() {
        String ticker = "MSFT";
        int numOfShares = 5;
        double price = 150.0;

        Position mockSavedPosition = new Position();
        when(mockDao.save(any(Position.class))).thenReturn(mockSavedPosition);

        Position result = positionService.buy(ticker, numOfShares, price);

        assertEquals(mockSavedPosition, result);
        verify(mockDao).save(any(Position.class));
    }

    @Test
    public void testSell() {
        String ticker = "MSFT";

        Position mockPosition = new Position();
        when(mockDao.findById(ticker)).thenReturn(Optional.of(mockPosition));

        positionService.sell(ticker);

        verify(mockDao).findById(ticker);
        verify(mockDao).deleteById(ticker);
    }
}

 