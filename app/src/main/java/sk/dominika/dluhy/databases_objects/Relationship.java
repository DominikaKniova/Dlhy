package sk.dominika.dluhy.databases_objects;

public class Relationship {
    private String fromUserId;
    private String toUserId;
    private String toUserName;

    public Relationship(){}

    public Relationship(String fromUserId, String toUserId, String toUserName) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
    }

    public String getFromUserId() {return this.fromUserId;}

    public String getToUserId() {return this.toUserId;}

    public String getToUserName() {return this.toUserName;}
}
