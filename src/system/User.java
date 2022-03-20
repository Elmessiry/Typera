package system;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private long score = 0;
    private long time =0;
    private boolean loggedIn = false;
    private boolean selected = false;
    private boolean teamd = false;
    private boolean ready = false;
    private boolean q = true;
    private int teamID=77;


    public boolean isQ() {
        return q;
    }

    public void setQ(boolean q) {
        this.q = q;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User() {
    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }

    public long getScore() {

        return score;
    }

    public void setScore(long score) {

        this.score = score;
    }

    public boolean isLoggedIn() {

        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {

        this.loggedIn = loggedIn;
    }

    public boolean isTeamd() {

        return teamd;
    }

    public void setTeamd(boolean teamd) {
        this.teamd = teamd; }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getTeamID() {

        return teamID;
    }

    public void setTeamID(int teamID) {

        this.teamID = teamID;
    }
}

