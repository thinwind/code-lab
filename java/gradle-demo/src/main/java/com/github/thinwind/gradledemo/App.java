package com.github.thinwind.gradledemo;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        var i = 0;
        Scanner scanner = new Scanner(System.in);
        while (++i > 0){
            System.out.println(i+".Input the todo item name:");
            TodoItem item = new TodoItem();
            item.setName(scanner.nextLine());
            System.out.println(item);
        }
    }
}
