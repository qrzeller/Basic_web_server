package src.server;

import com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by qtask on 17.03.2017.
 */
public class WebServer {
    private ServerSocket s = null;
    private String request = "";
    private OutputStream os = null;

    public WebServer(int socketPort){
        if(socketPort<1000){
            System.out.println("It's maybe not possible to bind to a port < 1000");
        }

        try {
            while(true){
            ServerSocket server = new ServerSocket(socketPort);
            s = server;



            System.out.println("--------------\n" +
                    "Waiting for remote client on port "+ socketPort+
            "\n--------------");
            Socket client = s.accept();//listen to connection, block if no connection
            InputStream is = client.getInputStream();//To read
            os = client.getOutputStream();//To write
            byte[]buff= new byte[100];
            while(-1 != is.read(buff)){
                request+=new String (buff, StandardCharsets.UTF_8);
                if(request.contains("\r\n\r\n"))break;
            }
            System.out.println(request);
            parseRequest(request);
        }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseRequest(String someRequest) throws IOException {
        String body = "You request : ";
        int start = someRequest.indexOf(" ");
        int end = someRequest.indexOf(" ",start+1);
        String path = someRequest.substring(start,end+1);
        System.out.println(path+"<--------------------");
        body+=path;
        String content = "<HTML>"+ body +"</HTML>";
        String response = "HTTP/1.1 200 OK\n" +
                "Date: Fri, 27 Jul 2017 17:57:53 GMT\n" +
                 "Server: Custom\n"+
                "Last-Modified: Wed, 22 Jul 2016 19:15:56 GMT\n"+
                "Content-Length: "+(content.length())+"\n"+
                "Content-Type: text/html\n"+
                "Connection: Closed\n\n"+
                 content +
                "\r\n\r\n";

        System.out.println("-------Response-------");
        System.out.println(response);
        os.write(response.getBytes(StandardCharsets.UTF_8));

        os.flush();
        os.close();
        s.close();


    }
}
