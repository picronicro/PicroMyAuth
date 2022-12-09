package me.picro.myauth.enums;

public class PlayerData {

    private String id;
    private String nickname;
    private String password;

    public PlayerData(String id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
