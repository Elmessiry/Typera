package system;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;
import java.lang.*;


public class ServerSocketTask implements Runnable {

    private Socket connection;  // Create Socket
    private ArrayList<User> usersList;
    public List<ScoreObject> scoreList;
    public int bestScore;
    public ArrayList<Boolean> stat;
    ArrayList<ArrayList<User>> teamList;

    public ServerSocketTask(int server, Socket s, ArrayList<User> usersList, ArrayList<ArrayList<User>> teamList, List<ScoreObject> scoreList, int bestScore, ArrayList<Boolean> stat) {
        this.connection = s;
        this.usersList = usersList;
        this.teamList = teamList;
        this.scoreList = scoreList;
        this.bestScore = bestScore;
        this.stat = stat;
    }


    @Override
    public void run() {
        Random rand = new Random();
        int z = rand.nextInt(2);


        try {


            System.out.println("connected");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            while (true) {

                User receivedUser = (User) ois.readObject();


                String request = br.readLine();

                if (request.equals("register")) {
                    register(receivedUser, usersList);
                    bw.write(receivedUser.getUsername() + " is Registered Successfully");
                    bw.write("\n");
                    bw.flush();
                }

                if (request.equals("login")) {

                    boolean verified = verify(receivedUser, usersList);
                    String verification = Boolean.toString(verified);
                    for (int i = 0; i < usersList.size(); i++) {
                        if (receivedUser.getUsername().equals(usersList.get(i).getUsername())) {
                            usersList.get(i).setLoggedIn(true);
                        }
                    }

                    if (verified)
                        receivedUser.setLoggedIn(true);
                    bw.write(verification);
                    bw.write("\n");
                    bw.flush();

                }

                if (request.equals("make a team")) {
                    int x;
                    if (teamList.size() == 0) {
                        x = 1;
                    } else if (teamList.get(teamList.size() - 1).size() == 2) {
                        x = teamList.size() + 1;
                    } else {
                        x = teamList.size();
                    }
                    boolean unchanged = true;
                    if (teamList.size() > 0)
                        Thread.sleep(500);
                    for (int i = 0; i < teamList.size(); i++) {
                        if (teamList.get(i).size() < 2) {
                            teamList.get(i).add(receivedUser);
                            receivedUser.setTeamID(x);
                            unchanged = false;
                            while (true) {
                                Thread.sleep(300);
                                if (teamList.get(i).size() == 2) {

                                    receivedUser.setTeamd(true);
                                    break;
                                }
                            }
                            bw.write(String.valueOf(x));
                            bw.write("\n");
                            bw.write("done");
                            bw.write("\n");
                            bw.flush();
                        }
                    }

                    if (unchanged) {
                        ArrayList<User> team = new ArrayList<>(2);
                        receivedUser.setTeamID(x);
                        team.add(receivedUser);

                        teamList.add(team);

                        while (true) {
                            Thread.sleep(200);
                            if (team.size() == 2) {

                                receivedUser.setTeamd(true);
                                break;
                            }
                        }

                        bw.write(String.valueOf(x));
                        bw.write("\n");
                        bw.write("done");
                        bw.write("\n");
                        bw.flush();
                    }
                }

                if (request.equals("start cd")) {
                    receivedUser.setReady(true);

                    for (ArrayList<User> team :
                            teamList) {
                        if (team.get(0).getTeamID() == receivedUser.getTeamID()) {
                            while (true) {
                                Thread.sleep(200);
                                if (team.get(0).isReady() && team.get(1).isReady()) {
                                    break;
                                }
                            }
                            if (team.get(0).isReady() && team.get(1).isReady()) {
                                bw.write("start");
                                bw.write("\n");
                                bw.flush();
                            }
                        }
                    }
                }

                if (request.equals("start a game")) {
                    int n = 70;
                    String chall =
                            RandomString.getAlphaNumericString(n);
                    String reco = "";

                    for (ArrayList<User> team :
                            teamList) {
                        if (receivedUser.getTeamID() == team.get(0).getTeamID()) {
                            int t = receivedUser.getTeamID() % 2;
                            team.get(t).setSelected(true);


                            if (team.get(t).isSelected() == receivedUser.isSelected()) {
                                receivedUser.setTime(System.currentTimeMillis());
                                bw.write(chall);
                                bw.write("\n");
                                bw.flush();

                                while (true) {

                                    reco = br.readLine();
                                    if (reco.equals(chall)) {
                                        bw.write("Done");
                                        bw.write("\n");
                                        bw.flush();
                                        int x = (t + 1) % 2;
                                        stat.set(receivedUser.getTeamID(), true);
                                        team.get(x).setQ(false);
                                        break;
                                    } else {
                                        bw.write("try again");
                                        bw.write("\n");
                                        bw.flush();
                                    }
                                    Thread.sleep(250);
                                }
                            } else {
                                while (true) {
                                    Thread.sleep(300);

                                    if (stat.get(receivedUser.getTeamID())) {
                                        bw.write(chall);
                                        bw.write("\n");
                                        bw.flush();
                                        while (true) {
                                            reco = br.readLine();

                                            if (reco.equals(chall)) {
                                                bw.write("Done");
                                                bw.write("\n");
                                                bw.flush();
                                                team.get(t).setQ(false);
                                                receivedUser.setTime(System.currentTimeMillis());
                                                break;
                                            } else {
                                                bw.write("try again");
                                                bw.write("\n");
                                                bw.flush();
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                            long c = 0;
                            bw.write("wait");
                            bw.write("\n");
                            bw.flush();
                            while (true) {

                                Thread.sleep(300);
                                if (!team.get(0).isQ() && !team.get(1).isQ()) {
                                    if (team.get(0).getTime() > team.get(1).getTime()) {
                                        c = (team.get(0).getTime() - team.get(1).getTime()) / 1000;
                                        receivedUser.setScore(c);
                                    } else if (team.get(0).getTime() < team.get(1).getTime()) {
                                        c = (team.get(1).getTime() - team.get(0).getTime()) / 1000;
                                        receivedUser.setScore(c);
                                    }
                                    if (receivedUser.equals(team.get(0))) {
                                        var score = new ScoreObject();
                                        score.setScore(c);
                                        scoreList.add(score);
                                        if (scoreList.size() > 1)
                                            scoreList.sort(Comparator.naturalOrder());
                                    }
                                    bw.write("go");
                                    bw.write("\n");
                                    bw.flush();

                                    Thread.sleep(590);
                                    break;
                                }
                            }
                            int e = (int) c;
                            bw.write(e);

                            bw.flush();


                            if ( e == scoreList.get(0).score) {


                                bw.write("Congratulations, you got a new best time " + e);
                                bw.write("\n");
                                bw.flush();



                            } else {
                                bw.write("Better luck next time, your time is " + e);
                                bw.write("\n");
                                bw.flush();
                            }


                            System.out.println(scoreList.size());


                            OutputStream outputStream = connection.getOutputStream();
                            // create an object output stream from the output stream so we can send an object through it
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(scoreList);
                            objectOutputStream.flush();

                            if (team.get(0).isReady()) {
                                team.get(0).setReady(false);
                                team.get(0).setTeamd(false);
                                stat.set(receivedUser.getTeamID(), false);
                                team.get(0).setTeamID(78);
                                team.get(0).setQ(true);
                                team.get(0).setSelected(false);

                            }
                            if (team.get(1).isReady()) {
                                team.get(1).setReady(false);
                                team.get(1).setTeamd(false);
                                team.get(1).setTeamID(88);
                                team.get(1).setQ(true);
                                team.get(1).setSelected(false);
                            }
                            while (true) {
                                if (!team.get(1).isReady() && !team.get(0).isReady()) {
                                    teamList.remove(team);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }

                Thread.sleep(300);

                if (request.equals("q"))
                    break;
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private static boolean verify(User user, ArrayList<User> usersList) {

        for (User value : usersList) {
            if (user.getUsername().toLowerCase().equals(value.getUsername().toLowerCase()))
                if (user.getPassword().equals(value.getPassword())) {
                    return true;
                }
        }
        return false;
    }

    private static void register(User user, ArrayList<User> usersList) {
        usersList.add(user);
    }


    private static class RandomString {

        // function to generate a random string of length n
        static String getAlphaNumericString(int n) {
            // chose a Character random from this String

            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(n);

            for (int i = 0; i < n; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }
            return sb.toString();
        }
    }
}