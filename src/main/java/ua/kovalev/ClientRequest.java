package ua.kovalev;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest implements Runnable {
    private Socket socket;
    private AtomicLong counter;
    private String header;

    public ClientRequest(Socket socket, AtomicLong counter) {
        this.socket = socket;
        this.counter = counter;
        this.header = "HTTP/1.1 200 OK\r\n" + "Server: My_Server\r\n" + "Content-Type: text/html\r\n"
                + "Content-Length: " + "\r\n" + "Connection: close\r\n\r\n";
    }

    @Override
    public void run() {
        try (OutputStream os = socket.getOutputStream(); PrintWriter printWriter = new PrintWriter(os, false, Charset.forName("KOI8-R")); InputStream is = socket.getInputStream()) {
            byte [] bytes = new byte[is.available()];
            is.read(bytes);
            String req = new String(bytes);
            // фильтрую запрос на favicon.ico . Хром постоянно его шлёт вторым запросом после каждого основного запроса. Поэтому такой запрос я отбрасываю
            if (req.contains("GET /favicon.ico HTTP/1.1")){
                return;
            }
            printWriter.write(header);
            printWriter.write(String.format("<html><head><body><h1>Сервер имеет %d ядра</h1><h3>Номер запроса: %d</h3></body></html>", Runtime.getRuntime().availableProcessors(), counter.incrementAndGet()));
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
