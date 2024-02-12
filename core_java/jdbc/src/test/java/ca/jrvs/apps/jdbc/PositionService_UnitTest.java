package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.services.PositionService;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class PositionService_UnitTest {
    private PositionService positionService;
    private PositionDao dao;
    private Connection c;
    @Before
    public void setup() {
        PositionDao dao = new PositionDao(c);
        positionService = new PositionService(dao);
    }

    @Test
    public void testBuyValid() {
        String ticker = "MSFT";
        int numOfShares = 5;
        double price = 200.0;

        Position mockSavedPosition = new Position();


        Position result = positionService.buy(ticker, numOfShares, price);

        assertNotNull(result);

    }

    @Test
    public void testBuyNotValid() {
        String ticker = "BSCZ";
        int numOfShares = 10;
        double price = 60.0;

        Position mockSavedPosition = new Position();


        Position result = positionService.buy(ticker, numOfShares, price);

        assertNull(result);

    }

    @Test
    public void testSellValid() {
        String ticker = "MSFT";

        Position mockPosition = new Position();

        assertNotNull(dao.findById(ticker));

        positionService.sell(ticker);

        assertNull(dao.findById(ticker));

    }

    public void testSellNotValid() {
        String ticker = "SDFASZ";

        Position mockPosition = new Position();

        positionService.sell(ticker);

        assertNull(dao.findById(ticker));

    }
}