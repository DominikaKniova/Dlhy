package sk.dominika.dluhy.database_models;

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
//    private static String firstName;
//    private static String lastName;
//    private static String email;
//    private static String id;
//
//    public CurrentUser(String first, String last, String mail, String id_user){
//        firstName = first;
//        lastName = last;
//        email = mail;
//        id = id_user;
//    }
//
//    public static String getFirstName() {
//        return firstName;
//    }
//
//    public static String getLastName() {
//        return lastName;
//    }
//
//    public static String getEmail() {
//        return email;
//    }
//
//    public static String getId() {
//        return id;
//    }
//
//    public static void setId(String id) {
//        CurrentUser.id = id;
//    }
}
