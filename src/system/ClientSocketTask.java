package system;


import java.io.*;
import java.net.Socket;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ClientSocketTask implements Runnable {


    private String request = null;
    private User user = new User();
    ObjectInputStream objectInputStream;

    public ClientSocketTask() {
        this.user.setLoggedIn(false);
        this.user.setTeamd(false);
        this.user.setReady(false);
    }

    @Override
    public void run() {

        try {

            int port = 1234;
            String ip = "localhost";
            Socket connection = new Socket(ip, port); //Create a Client Socket for "localhost" address and port

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())); //Create a Request Buffer
            ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


            while (true) {

                if (!this.user.isLoggedIn() || this.user == null) {

                    System.out.print("INFO: Enter ´q´ to stop session\n");
                    System.out.print("Hello! Make a choice:   1- Register    2- Login\n");
                    System.out.print("Enter the choice:\n");
                    Scanner scanner = new Scanner(System.in);
                    String choice;

                    do {
                        choice = scanner.nextLine();
                        if (choice.equals("1")) {
                            request = "register";
                        } else if (choice.equals("2")) {
                            request = "login";
                        } else if (choice.equals("q"))
                            break;
                    } while (!choice.equals("1") && !choice.equals("2"));

                    System.out.println("Enter Your Username: ");
                    String username = scanner.next();
                    System.out.println("Enter Your Password: ");
                    String password = scanner.next();
                    user = new User(username, password);


                } else {
                    if (!this.user.isTeamd()) {
                        System.out.println("Hello " + this.user.getUsername() + " Score: " + this.user.getScore());
                        Scanner scanner = new Scanner(System.in);

                        System.out.println("Type ready to join a team or exit to quit.");
                        String choice = scanner.next().toLowerCase();

                        if (choice.equals("ready")) {
                            System.out.println("Team making ... Please be patient");
                            request = "make a team";
                        } else if (choice.equals("exit"))
                            break;
                    }

                    if (this.user.isTeamd() && !request.equals("start a game")) {
                        System.out.println("Type anything to start the game or exit to quit.");
                        Scanner scanner = new Scanner(System.in);
                        String choice = scanner.next();

                        if (choice.equals("exit"))
                            break;
                        user.setReady(true);
                        request = "start cd";
                    }

                }
                //           if(user.getScore() != 0)
                oos.writeObject(this.user);
                bw.write(request);
                bw.write("\n");
                bw.flush();

                Thread.sleep(300);


                if (request.equals("register")) {
                    String message = br.readLine();
                    System.out.println(message);
                }

                if (request.equals("login")) {
                    boolean message = Boolean.parseBoolean(br.readLine());
                    if (!message) {
                        System.out.println("login failed");
                    }

                    if (message) {
                        System.out.println("logged in successfully!");
                        user.setLoggedIn(true);
                    }
                }

                if (request.equals("make a team")) {
                    user.setTeamID(Integer.parseInt(br.readLine()));
                    String message = br.readLine();
                    System.out.println(message);
                    user.setTeamd(true);
                }

                if (request.equals("start a game")) {


                    System.out.println(br.readLine());
                    Scanner scanner = new Scanner(System.in);
                    String choose = "";
                    while (true) {
                        Thread.sleep(300);
                        String send = scanner.next();
                        bw.write(send);
                        bw.write("\n");
                        bw.flush();
                        choose = br.readLine();
                        System.out.println(choose);
                        if (choose.equals("Done")) {
                            break;
                        }
                    }

//                    Thread.sleep(300);

                    while (true) {
                        String msg = br.readLine();
                        if (msg.equals("go"))
                            break;
                    }

                    user.setScore(br.read());
                    System.out.println(br.readLine());
                    // create a DataInputStream so we can read data from it.
                    objectInputStream = new ObjectInputStream(connection.getInputStream());
                    List<ScoreObject> scoreObjectList = (List<ScoreObject>) objectInputStream.readObject();
                    scoreObjectList.forEach((msg) -> System.out.println(msg.score));


                    user.setTeamd(false);
                    user.setReady(false);
                    request = "q";
                }

                if (request.equals("start cd")) {
                    String message = br.readLine();
                    if (message.equals("start")) {
                        int i = 10;
                        while (i > 0) {
                            System.out.println("Remaining " + i + " Seconds");
                            i--;
                            Thread.sleep(100);    // 1000L = 1000ms = 1 second

                        }
                        request = "start a game";
                    }
                }


                if (request.equals("q")) {
                    oos.writeObject(this.user);
                    bw.write(request);
                    bw.write("\n");
                    bw.flush();
                    break;
                }
            }
            Thread.sleep(300);
            System.out.println("Connection will terminate");
            objectInputStream.close();
            bw.close();
            oos.close();
            br.close();
            connection.close();


        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

