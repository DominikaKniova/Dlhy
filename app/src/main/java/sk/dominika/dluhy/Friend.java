package sk.dominika.dluhy;

import java.util.ArrayList;

public class Friend {
    private String friendName;
    private int friendNumber;

    public Friend(String name, int number){
        friendName = name;
        friendNumber = number;
    }

    public String getName() {
        return friendName;
    }

    public static ArrayList<Friend> createFriendList(int numFriends) {
        ArrayList<Friend> friends = new ArrayList<Friend>();
        for (int i = 1; i <= numFriends; i++) {
            friends.add(new Friend("Person ", i));
        }
        return friends;
    }
}
