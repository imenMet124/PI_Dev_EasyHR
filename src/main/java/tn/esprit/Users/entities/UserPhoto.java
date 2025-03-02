package tn.esprit.Users.entities;

public class UserPhoto {
    private int id;
    private int userId;
    private String photoPath;

    public UserPhoto(int id, int userId, String photoPath) {
        this.id = id;
        this.userId = userId;
        this.photoPath = photoPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
