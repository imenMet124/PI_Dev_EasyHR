package tn.esprit.Users.utils;

public class SharedData {
    private static String phoneNumber;

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        SharedData.phoneNumber = phoneNumber;
    }
}

