package com.example.speech.aiservice.vn.service.console;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CommandListenerApp {
    public static void main(String[] args) {
        new CommandListenerApp().listenForCommands();
    }

    private void listenForCommands() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command: ");
            String commandInput = scanner.nextLine().trim().toUpperCase();

            CommandType commandType = CommandType.fromString(commandInput);

            if (commandType == CommandType.STOP) {
                System.out.println("STOP command received! Stopping workflow...");
                sendStopRequest();
            } else {
                System.out.println("Invalid command! Please enter a valid command (e.g., STOP).");
            }
        }
    }

    private void sendStopRequest() {
        try {
            URL url = new URL("http://localhost:8080/workflow/stop");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write("{}".getBytes());
            os.flush();
            os.close();

            System.out.println("Response Code: " + conn.getResponseCode());
        } catch (Exception e) {
            System.out.println("Error: Unable to send STOP request!");
            e.printStackTrace();
        }
    }
}
