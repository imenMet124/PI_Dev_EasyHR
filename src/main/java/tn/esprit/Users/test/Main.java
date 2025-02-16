package tn.esprit.Users.test;

import tn.esprit.Users.entities.Department;
import tn.esprit.Users.entities.User;
import tn.esprit.Users.entities.UserRole;
import tn.esprit.Users.entities.UserStatus;
import tn.esprit.Users.services.ServiceUsers;
import tn.esprit.Users.services.ServiceDepartment;
import tn.esprit.Users.utils.Base;

import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Initialize the database connection
        Base db = Base.getInstance();

        // Create instances of ServiceUsers and ServiceDepartment
        ServiceUsers su = new ServiceUsers();
        ServiceDepartment sd = new ServiceDepartment();

        try {
            // Step 1: Create a user first (without a department)
            User newUser = new User(
                    0,                             // Auto-generated ID
                    "Iyed Blick",                  // iyedNomUser
                    "iyed.blick@example.com",      // iyedEmailUser
                    "667788990",                   // iyedPhoneUser
                    UserRole.EMPLOYE,              // iyedRoleUser
                    "Software Engineer",           // iyedPositionUser
                    9000.0,                        // iyedSalaireUser
                    Date.valueOf("2023-07-01"),    // iyedDateEmbaucheUser
                    UserStatus.ACTIVE,             // iyedStatutUser
                    null                           // No department assigned yet
            );
            su.ajouter(newUser);  // Add the user to the database
            int userId = su.getLastInsertedId(); // Get the user's ID
            newUser.setIyedIdUser(userId); // Update user object with its ID
            System.out.println("User added successfully with ID: " + userId);

            // Step 2: Create a department and assign the user as manager
            Department department = new Department(0, "IT", "Information Technology Department", "Building B", null, newUser);
            sd.ajouter(department);  // Add department to the database
            int departmentId = sd.getLastInsertedId(); // Get department ID
            department.setIyedIdDep(departmentId); // Update department object
            System.out.println("Department added successfully with ID: " + departmentId);

            // Step 3: Update user to belong to the department
            newUser.setIyedDepartment(department);
            su.modifier(newUser);
            System.out.println("User assigned to department successfully!");

            // Fetch and display the department with its manager
            Department fetchedDepartment = sd.getDepartmentWithManager(departmentId);
            System.out.println("Department: " + fetchedDepartment.getIyedNomDep());
            System.out.println("Manager: " + (fetchedDepartment.getIyedManager() != null ?
                    fetchedDepartment.getIyedManager().getIyedNomUser() : "No Manager"));

            // Fetch and display user details along with their department
            User fetchedUser = su.getById(userId);
            System.out.println("User Name: " + fetchedUser.getIyedNomUser());
            System.out.println("User Department: " + fetchedUser.getIyedDepartment().getIyedNomDep());
            System.out.println("User Position: " + fetchedUser.getIyedPositionUser());

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}

