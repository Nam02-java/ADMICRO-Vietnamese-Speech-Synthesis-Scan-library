package com.example.speech.aiservice.vn.service.input;

import java.util.Scanner;
public class ConsoleInputService {
    private void readInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL novel : ");
        String input = scanner.nextLine();
    }
}
