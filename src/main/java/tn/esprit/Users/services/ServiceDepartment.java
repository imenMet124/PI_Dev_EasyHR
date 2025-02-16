package tn.esprit.Users.services;

import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.utils.Base;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDepartment implements IService<Department> {
    private final Connection connection;
    private final ServiceUsers serviceUsers;  // To fetch users

    public ServiceDepartment() {
        connection = Base.getInstance().getConnection();
        serviceUsers = new ServiceUsers();  // Initialize user service
    }

    @Override
    public void ajouter(Department department) throws SQLException {
        String sql = "INSERT INTO `department` (`iyedNomDep`, `iyedDescriptionDep`, `iyedLocationDep`, `iyedManagerId`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, department.getIyedNomDep());
            preparedStatement.setString(2, department.getIyedDescriptionDep());
            preparedStatement.setString(3, department.getIyedLocationDep());
            preparedStatement.setObject(4, (department.getIyedManager() != null) ? department.getIyedManager().getIyedIdUser() : null);

            preparedStatement.executeUpdate();

            // Get the generated department ID
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    department.setIyedIdDep(rs.getInt(1));  // Set the new department ID
                }
            }
        }
    }


    @Override
    public void modifier(Department department) throws SQLException {
        String sql = "UPDATE `department` SET `iyedNomDep` = ?, `iyedDescriptionDep` = ?, `iyedLocationDep` = ?, `iyedManagerId` = ? WHERE `iyedIdDep` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, department.getIyedNomDep());
            preparedStatement.setString(2, department.getIyedDescriptionDep());
            preparedStatement.setString(3, department.getIyedLocationDep());
            preparedStatement.setObject(4, (department.getIyedManager() != null) ? department.getIyedManager().getIyedIdUser() : null);
            preparedStatement.setInt(5, department.getIyedIdDep());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM `department` WHERE `iyedIdDep` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Department> afficher() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM `department`";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(buildDepartmentFromResultSet(rs));
            }
        }

        return departments;
    }

    public Department getById(int id) throws SQLException {
        String sql = "SELECT * FROM `department` WHERE `iyedIdDep` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return buildDepartmentFromResultSet(rs);
                }
            }
        }

        return null;
    }

    private Department buildDepartmentFromResultSet(ResultSet rs) throws SQLException {
        int departmentId = rs.getInt("iyedIdDep");

        // Fetch manager if exists
        User manager = null;
        int managerId = rs.getInt("iyedManagerId");
        if (!rs.wasNull()) {
            manager = serviceUsers.getById(managerId);
        }

        // Fetch employees in this department
        List<User> employees = serviceUsers.getUsersByDepartment(departmentId);

        return new Department(
                departmentId,
                rs.getString("iyedNomDep"),
                rs.getString("iyedDescriptionDep"),
                rs.getString("iyedLocationDep"),
                employees,
                manager
        );
    }
    public Department getDepartmentWithManager(int departmentId) throws SQLException {
        String sql = "SELECT d.*, u.iyedNomUser AS managerName " +
                "FROM department d " +
                "LEFT JOIN user u ON d.iyedManagerId = u.iyedIdUser " +
                "WHERE d.iyedIdDep = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, departmentId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Department department = new Department(
                        rs.getInt("iyedIdDep"),
                        rs.getString("iyedNomDep"),
                        rs.getString("iyedDescriptionDep"),
                        rs.getString("iyedLocationDep"),
                        null,  // List of employees (can be fetched separately)
                        null   // Manager (to be set below)
                );

                // Set the department manager
                int managerId = rs.getInt("iyedManagerId");
                if (managerId > 0) {
                    User manager = serviceUsers.getById(managerId);  // Fetch the manager user object
                    department.setIyedManager(manager);
                }

                return department;
            }
        }
        return null;  // Return null if no department is found
    }
    public int getLastInsertedId() throws SQLException {
        String sql = "SELECT LAST_INSERT_ID() AS id";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return 0;  // Return 0 if no ID is found
    }

}

