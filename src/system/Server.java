package system;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 1234; // initialize port number
        ServerSocket serverSocket = new ServerSocket(port);// Start a new server socket


        List<ScoreObject> scoreList = new ArrayList<>(15);
        ArrayList<User> usersList = new ArrayList<>(15);
        ArrayList<ArrayList<User>> teamList = new ArrayList<>(15);
        ArrayList<Boolean> stat = new ArrayList<>(15);
        for(int i=0;i<15;i++)
            stat.add(false);
        int bestScore = 99999;

//       fillPlaysList(playsList, "src/system/plays.txt");

        ExecutorService executorService = Executors.newFixedThreadPool(15);

        System.out.println("wait for connections");

        while (true) {

            try {
                // Create Socket
                Socket connection = serverSocket.accept();
                ServerSocketTask serverTask = new ServerSocketTask(bestScore,connection, usersList, teamList, scoreList, bestScore, stat); // create a new socket task
                executorService.submit(serverTask);

            } catch (IOException e) {
                e.getStackTrace();
            }

        }

    }
}


