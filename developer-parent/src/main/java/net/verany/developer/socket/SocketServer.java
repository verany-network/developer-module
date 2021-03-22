package net.verany.developer.socket;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    @SneakyThrows
    public void start(int port) {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        out.println();
        out.flush();
    }

    public void writeMessage(String channel, String message, JsonDocument document) {

    }

    @SneakyThrows
    public void stop() {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

}
