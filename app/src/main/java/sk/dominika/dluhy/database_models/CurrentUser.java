package sk.dominika.dluhy.database_models;

/**
 * Static class for storing information about the current user, like id, name and email.
 * Accessible throughout the whole app.
 */
public class CurrentUser {
    public static class UserCurrent{
        public static String firstName;
        public static String lastName;
        public static String email;
        public static String id;
    }

    public static void setData(String first, String last, String mail) {
        UserCurrent.firstName = first;
        UserCurrent.lastName = last;
        UserCurrent.email = mail;
    }

    public static void setId(String id) {
        UserCurrent.id = id;
    }

    public static void cleanUser(){
        UserCurrent.firstName = "";
        UserCurrent.lastName = "";
        UserCurrent.email = "";
        UserCurrent.id = "";
    }
}
