import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void server(int port, BooleanSearchEngine engine) {
        System.out.println("Запуск сервера с портом номером " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String searchWord = in.readLine();
                List<PageEntry> searchResult = engine.search(searchWord);

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();

                String response = searchResult.stream()
                        .map(gson::toJson)
                        .collect(Collectors.joining());

                out.println(response);
                clientSocket.close();
                out.close();
                in.close();
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        int port = 8989;
        server(port, engine);
    }
}