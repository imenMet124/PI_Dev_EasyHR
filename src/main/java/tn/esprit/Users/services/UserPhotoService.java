package tn.esprit.Users.services;

import tn.esprit.Users.entities.UserPhoto;
import tn.esprit.Users.utils.Base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class UserPhotoService {

    private final Connection connection = Base.getInstance().getConnection();

    public void addUserPhoto(int userId, String photoPath) throws SQLException {
        String query = "INSERT INTO UserPhoto (user_id, photo_path) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, photoPath);
            ps.executeUpdate();
        }
    }

    public String getUserPhotoPath(int userId) throws SQLException {
        String query = "SELECT photo_path FROM UserPhoto WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("photo_path");
            }
        }
        return null; // No photo found
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }
}
