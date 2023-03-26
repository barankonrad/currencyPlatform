package sample.gui.data;

public class ContactItem{
    private final int userId;
    private final int friendId;
    private final String friendLogin;
    private final String friendFirstName;
    private final String friendLastName;

    public ContactItem(int userId, int friendId, String friendLogin, String friendFirstName, String friendLastName){
        this.userId = userId;
        this.friendId = friendId;
        this.friendLogin = friendLogin;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
    }

    public int getUserId(){
        return userId;
    }

    public int getFriendId(){
        return friendId;
    }

    public String getFriendLogin(){
        return friendLogin;
    }

    public String getFriendFullName(){
        return String.format("%s %s", friendFirstName, friendLastName);
    }

    @Override
    public String toString(){
        return String.format("%s %s  -  %s", friendFirstName, friendLastName, friendLogin);
    }
}
