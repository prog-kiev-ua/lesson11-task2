package ua.kovalev;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ServerApp {
    private AtomicLong counter = new AtomicLong(0);
    private int port = 8083;


    public void start() throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for(;;){
            Socket socket = serverSocket.accept();
            executorService.submit(new ClientRequest(socket, counter));
        }
    }
}
