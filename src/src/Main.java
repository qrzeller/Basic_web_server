package src;

import src.server.WebServer;

public class Main{
    public static void main(String... args){

        new Thread(() -> {
            WebServer w = new WebServer(8181);
        }).start();




    }
}