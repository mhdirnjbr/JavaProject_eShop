package dao;

import model.Factor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static dao.DAOManager.delete;
import static dao.DAOManager.getConnection;

public class FactorDAO {

    public Factor createFactor(Factor newFactor) {
        Factor result = null;
        String sql = "INSERT INTO shop.factor (basket_id, user_id, price, date, delivery) VALUES(?,?,?,?,?)";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, newFactor.getBasketId());
            ps.setLong(2, newFactor.getUserId());
            ps.setLong(3, newFactor.getPrice());
            ps.setDate(4, newFactor.getDate());
            ps.setLong(5, newFactor.getDelivery());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    long key = rs.getLong(1);
                    newFactor.setId(key);
                    result = newFactor;
                }

            }

        } catch (SQLException e) {
            
                System.err.println(e.getMessage());
        }

        return result;
    }

    public List<Factor> getFactors() {
        List<Factor> listFactor = new ArrayList<>();

        String sql = "SELECT * FROM factor";

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long basketId = resultSet.getLong("basket_id");
                long userId = resultSet.getLong("user_id");
                long price = resultSet.getLong("price");
                Date date = resultSet.getDate("date");
                int delivery = resultSet.getInt("delivery");

                Factor factor = new Factor(id, basketId, userId, price, date, delivery);
                listFactor.add(factor);
            }
            return listFactor;

        } catch (SQLException e) {
            
                System.err.println(e.getMessage());
            return null;
        }

    }

    public void deleteFactor(long id) {
        String sql = "DELETE FROM factor where id = ?";

        delete(id, sql);

    }

    public Factor updateFactor(Factor updatedFactor) {
        Factor result = null;
        String sql = "UPDATE factor SET basket_id = ?, user_id = ?, price = ?, date = ?, delivery = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, updatedFactor.getBasketId());
            ps.setLong(2, updatedFactor.getUserId());
            ps.setLong(3, updatedFactor.getPrice());
            ps.setDate(4, updatedFactor.getDate());
            ps.setInt(5, updatedFactor.getDelivery());
            ps.setLong(6, updatedFactor.getId());

            if (ps.executeUpdate() > 0)
                result = updatedFactor;

        } catch (SQLException e) {
            
                System.err.println(e.getMessage());
        }

        return result;
    }

    public Factor getFactor(long id) {
        Factor factor = null;
        String sql = "SELECT * FROM factor WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    long basketId = rs.getLong("basket_id");
                    long userId = rs.getLong("user_id");
                    long price = rs.getLong("price");
                    Date date = rs.getDate("date");
                    int delivery = rs.getInt("delivery");
                    factor = new Factor(id, basketId, userId, price, date, delivery);
                }

            }

        } catch (SQLException e) {
            
                System.err.println(e.getMessage());
        }

        return factor;
    }

    public List<Factor> getFactorsByUserId(long userId) {

        Factor factor = null;
        String sql = "SELECT * FROM factor WHERE user_id = ?";

        List<Factor> list = new ArrayList<>();

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    long id = rs.getLong("id");
                    long basketId = rs.getLong("basket_id");
                    long price = rs.getLong("price");
                    Date date = rs.getDate("date");
                    int delivery = rs.getInt("delivery");
                    factor = new Factor(id, basketId, userId, price, date, delivery);
                    list.add(factor);
                }

            }

        } catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        return list;

    }

    public List<Factor> findByDate(LocalDate start, LocalDate end) {

        Factor factor = null;
        String sql = "SELECT * FROM factor WHERE date between '" + start + "' AND '" + end + "' ";

        List<Factor> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            /*ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));*/

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    long id = rs.getLong("id");
                    long basketId = rs.getLong("basket_id");
                    long userId = rs.getLong("user_id");
                    long price = rs.getLong("price");
                    Date date = rs.getDate("date");
                    int delivery = rs.getInt("delivery");
                    factor = new Factor(id, basketId, userId, price, date, delivery);
                    list.add(factor);
                }

            }

        } catch (SQLException e) {

            System.err.println(e.getMessage());
        }

        return list;


    }
}
