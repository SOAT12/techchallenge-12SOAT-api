package com.fiap.soat12.tc_group_7.cleanarch.repository;

import com.fiap.soat12.tc_group_7.cleanarch.entity.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleDataSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcVehicleServiceRepository implements VehicleDataSource {

    private final DataSource dataSource;

    public JdbcVehicleServiceRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<VehicleService> findAll() {
        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT * FROM vehicle_service";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<VehicleService> services = new ArrayList<>();
            while (rs.next()) {
                VehicleService service = VehicleService.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .value(new BigDecimal(rs.getString("value")))
                        .active(rs.getBoolean("active"))
                        .build();
                services.add(service);
            }

            return services;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching vehicle service", e);
        }
    }

    @Override
    public Optional<VehicleService> findById(Long id) {
        String query = "SELECT * FROM vehicle_service WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                VehicleService service = VehicleService.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .value(rs.getBigDecimal("value"))
                        .active(rs.getBoolean("active"))
                        .build();

                return Optional.of(service);
            }

            return Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching vehicle service by id", e);
        }
    }

    @Override
    public Long save(VehicleService vehicleService) {
        String query = "INSERT INTO vehicle_service (name, value, active) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehicleService.getName());
            stmt.setBigDecimal(2, vehicleService.getValue());
            stmt.setBoolean(3, vehicleService.getActive());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            } else {
                throw new RuntimeException("Failed to retrieve generated ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving vehicle service", e);
        }
    }

    @Override
    public void update(VehicleService service) {
        String query = "UPDATE vehicle_service SET name = ?, value = ?, active = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, service.getName());
            stmt.setBigDecimal(2, service.getValue());
            stmt.setBoolean(3, service.getActive());
            stmt.setLong(4, service.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("No record found with id: " + service.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating vehicle service", e);
        }
    }

}

