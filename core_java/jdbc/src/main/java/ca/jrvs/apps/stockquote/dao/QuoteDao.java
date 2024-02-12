package ca.jrvs.apps.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteDao implements CrudDao<Quote, String> {
    final Logger logger = LoggerFactory.getLogger(CrudDao.class);
    private Connection c;

    public QuoteDao(Connection c) {
        this.c = c;
    }

    /**
     * Saves a given entity. Used for create and update
     *
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity == null){
            logger.error("Quote does not exist");
        }
            //Create New Quote
        else if (findById(entity.getSymbol()).isEmpty()) {
            String sqlQuery = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
                statement.setString(1, entity.getSymbol());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, entity.getLatestTradingDay());
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getTimestamp());

                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                logger.error("Failed to save quote, SQLException: " + e.getMessage());
            }

        }
        //Update Existing Quote
        else {
            String sqlQuery = "UPDATE quote SET symbol = ?, open = ?, high = ?, low = ?, price = ?, volume = ?, " +
                    "latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
            try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
                statement.setString(1, entity.getSymbol());
                statement.setDouble(2, entity.getOpen());
                statement.setDouble(3, entity.getHigh());
                statement.setDouble(4, entity.getLow());
                statement.setDouble(5, entity.getPrice());
                statement.setInt(6, entity.getVolume());
                statement.setDate(7, entity.getLatestTradingDay());
                statement.setDouble(8, entity.getPreviousClose());
                statement.setDouble(9, entity.getChange());
                statement.setString(10, entity.getChangePercent());
                statement.setTimestamp(11, entity.getTimestamp());
                statement.setString(12,entity.getSymbol());

                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                logger.error("Failed to save quote, SQLException: " + e.getMessage());
            }
        }
        return entity;
    }

    /**
     * Retrieves an entity by its id
     *
     * @param s - must not be null
     * @return Entity with the given id or empty optional if none found
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        if (s == null || s.isEmpty()) {
            System.out.println("Ticker does not exist");
        }

        String sqlQuery = "SELECT * FROM quote WHERE symbol = ?";
        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1, s);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                System.out.println("Quote of " + s + " found");
                return Optional.ofNullable(quoteMapper(rs));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Failed to find quote, SQLException: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves all entities
     *
     * @return All entities
     */
    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM quote";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                quotes.add(quoteMapper(rs));
            }

        } catch (SQLException e) {
            logger.error("Failed to find all quotes, SQLException: " + e.getMessage());
        }
        return quotes;
    }


    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     *
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (s == null || s.isEmpty()) {
            logger.error("Ticker does not exist");
        }

        String sqlQuery = "DELETE FROM quote WHERE symbol = ?";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1, s);
            statement.executeUpdate();
            System.out.println("Ticker " + s + " has been deleted from quotes.");
        } catch (SQLException e) {
            logger.error("Failed to delete quote, SQLException: " + e.getMessage());
        }
    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM quote";
        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete all quotes, SQLException: " + e.getMessage());
        }

    }

    private Quote quoteMapper(ResultSet rs) throws SQLException {
        Quote quote = new Quote();
        quote.setSymbol(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));

        return quote;
    }

    /*public boolean verify(String symbol) {
        String sqlQuery = "SELECT * FROM quote WHERE symbol = ?";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1, symbol);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if symbol exists, false otherwise
            }
        }
         catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return false;
    }*/
}