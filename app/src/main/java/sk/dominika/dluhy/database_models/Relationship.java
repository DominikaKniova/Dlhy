package sk.dominika.dluhy.database_models;

/**
 * Model for relationship.
 */
public class Relationship {
    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;

    public Relationship(){}

    public Relationship(String fromUserId, String fromUserName, String toUserId, String toUserName) {
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
    }

    public String getFromUserId() {return this.fromUserId;}

    public String getFromUserName() {
        return fromUserName;
    }

    public String getToUserId() {return this.toUserId;}

    public String getToUserName() {return this.toUserName;}
}
