package tn.esprit.tache.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import tn.esprit.tache.entities.Projet;
import tn.esprit.tache.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Collections;

public class ProjetService implements IProjetService {
    private Connection conn = DBConnection.getInstance().getConnection();
    private static final String APPLICATION_NAME = "Projet Manager";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_FILE = "C:/Users/bnsih/Downloads/youcan-new-349411-7acddf9f90d2.json";
    private static final String CALENDAR_ID = "bnsiheb666@gmail.com"; // Use your Google Calendar ID

    private static Calendar getCalendarService() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_FILE))
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public void ajouterProjet(Projet projet) {
        String sql = "INSERT INTO projet (nom_projet, desc_projet, statut_projet, date_debut_projet, date_fin_projet) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, projet.getNomProjet());
            ps.setString(2, projet.getDescProjet());
            ps.setString(3, projet.getStatutProjet());
            ps.setDate(4, new Date(projet.getDateDebutProjet().getTime()));
            ps.setDate(5, new Date(projet.getDateFinProjet().getTime()));
            ps.executeUpdate();
            System.out.println("✅ Projet ajouté avec succès.");

            // Add project to Google Calendar
            ajouterProjetToGoogleCalendar(projet);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void ajouterProjetToGoogleCalendar(Projet projet) throws IOException {
        Calendar service = getCalendarService();

        Event event = new Event()
                .setSummary(projet.getNomProjet())
                .setDescription(projet.getDescProjet());

        // Set start and end time
        DateTime startDateTime = new DateTime(projet.getDateDebutProjet());
        DateTime endDateTime = new DateTime(projet.getDateFinProjet());

        event.setStart(new EventDateTime().setDateTime(startDateTime));
        event.setEnd(new EventDateTime().setDateTime(endDateTime));

        // Insert the event into Google Calendar
        event = service.events().insert(CALENDAR_ID, event).execute();
        System.out.println("📅 Projet ajouté à Google Calendar: " + event.getHtmlLink());
    }



    public List<String> getProjetNames() {
        List<String> projetNames = new ArrayList<>();
        String sql = "SELECT nom_projet FROM projet";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projetNames.add(rs.getString("nom_projet"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des noms des projets : " + e.getMessage());
        }

        return projetNames;
    }


    @Override
    public List<Projet> getAllProjets() {
        List<Projet> projets = new ArrayList<>();
        String sql = "SELECT * FROM projet";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                projets.add(new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projets;
    }

    @Override
    public void modifierProjet(Projet projet) {
        String sql = "UPDATE projet SET nom_projet=?, desc_projet=?, statut_projet=?, date_debut_projet=?, date_fin_projet=? WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, projet.getNomProjet());
            ps.setString(2, projet.getDescProjet());
            ps.setString(3, projet.getStatutProjet());
            ps.setDate(4, new Date(projet.getDateDebutProjet().getTime()));
            ps.setDate(5, new Date(projet.getDateFinProjet().getTime()));
            ps.setInt(6, projet.getIdProjet());
            ps.executeUpdate();
            System.out.println("✅ Projet modifié avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimerProjet(int idProjet) {
        String sql = "DELETE FROM projet WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjet);
            ps.executeUpdate();
            System.out.println("✅ Projet supprimé.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Projet getProjetById(int idProjet) {
        String sql = "SELECT * FROM projet WHERE id_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProjet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Projet getProjetByName(String nomProjet) {
        String sql = "SELECT * FROM projet WHERE nom_projet=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomProjet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Projet(rs.getInt("id_projet"), rs.getString("nom_projet"), rs.getString("desc_projet"),
                        rs.getString("statut_projet"), rs.getDate("date_debut_projet"), rs.getDate("date_fin_projet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
