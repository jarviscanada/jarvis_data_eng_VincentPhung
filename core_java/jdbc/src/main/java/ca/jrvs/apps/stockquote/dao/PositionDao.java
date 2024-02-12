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

public class PositionDao implements CrudDao<Position, String>{
    final Logger logger = LoggerFactory.getLogger(CrudDao.class);
    private Connection c;

    public PositionDao(Connection c){
        this.c = c;
    }

    /**
     * Saves a given entity. Used for create and update
     * @param entity - must not be null
     * @return The saved entity. Will never be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null){
            logger.error("Position does not exist");
        }
            //Create New Position
        else if(findById(entity.getTicker()).isEmpty()){
            String sqlQuery = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
            try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
                statement.setString(1, entity.getTicker());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());

                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                logger.error("Failed to save position, SQLException: " + e.getMessage());
                //throw new RuntimeException(e);
            }

        }
        //Update Existing Position
        else{
            String sqlQuery = "UPDATE position SET symbol = ?, number_of_shares = ?, value_paid = ? WHERE symbol = ?";
            try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
                statement.setString(1, entity.getTicker());
                statement.setInt(2, entity.getNumOfShares());
                statement.setDouble(3, entity.getValuePaid());
                statement.setString(4, entity.getTicker());

                statement.executeUpdate();

                return entity;
            } catch (SQLException e) {
                logger.error("Failed to save position, SQLException: " + e.getMessage());
                //throw new RuntimeException(e);
            }
        }
        return entity;
    }

    /**
     * Retrieves an entity by its id
     * @param s - must not be null
     * @return Entity with the given id or empty optional if none found
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public Optional<Position> findById(String s) throws IllegalArgumentException {
        if (s == null || s.isEmpty()){
            logger.error("Ticker does not exist");
        }

        String sqlQuery = "SELECT * FROM position WHERE symbol = ?";
        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1,s);

            ResultSet rs = statement.executeQuery();

            if (rs.next()){
                System.out.println("Position of " + s + " found");
                return Optional.ofNullable(positionMapper(rs));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Position could not be found, SQLException: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Retrieves all entities
     * @return All entities
     */
    @Override
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();

        String sqlQuery = "SELECT * FROM position";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                positions.add(positionMapper(rs));
            }

        } catch (SQLException e) {
            logger.error("Could not find all positions, SQLException: " + e.getMessage());
        }
        return positions;
    }


    /**
     * Deletes the entity with the given id. If the entity is not found, it is silently ignored
     * @param s - must not be null
     * @throws IllegalArgumentException - if id is null
     */
    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        if (s == null || s.isEmpty()){
            logger.error("Ticker does not exist");
        }

        String sqlQuery = "DELETE FROM position WHERE symbol = ?";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1,s);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete position, SQLException: " + e.getMessage());
        }
    }

    /**
     * Deletes all entities managed by the repository
     */
    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM position";
        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete all  positions, SQLException: " + e.getMessage());
        }

    }

    private Position positionMapper(ResultSet rs) throws SQLException {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));

        return position;
    }

    /*public boolean verify(String ticker) {
        String sqlQuery = "SELECT * FROM position WHERE symbol = ?";

        try (PreparedStatement statement = c.prepareStatement(sqlQuery)) {
            statement.setString(1, ticker);

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
