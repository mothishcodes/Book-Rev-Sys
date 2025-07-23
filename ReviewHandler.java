import java.io.*;
import java.net.*;

public class ReviewHandler {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server running on port 3000...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String line;
                StringBuilder requestBody = new StringBuilder();
                boolean isPost = false;

                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    if (line.startsWith("POST")) {
                        isPost = true;
                    }
                }

                if (isPost) {
                    char[] buffer = new char[1024];
                    int charsRead = in.read(buffer);
                    requestBody.append(buffer, 0, charsRead);

                    // Extract JSON-like data manually (simple)
                    String body = requestBody.toString();
                    String[] parts = body.split("\"");
                    String name = parts[3];
                    String rating = parts[7];
                    String review = parts[11];

                    saveReview(name, rating, review);
                }

                // Send HTTP response
                out.write("HTTP/1.1 200 OK\r\n");
                out.write("Content-Type: text/plain\r\n");
                out.write("Access-Control-Allow-Origin: *\r\n");
                out.write("\r\n");
                out.write("Review received");
                out.flush();

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveReview(String name, String rating, String review) {
        try (FileWriter writer = new FileWriter("reviews.txt", true)) {
            writer.write("Name: " + name + "\n");
            writer.write("Rating: " + rating + "\n");
            writer.write("Review: " + review + "\n");
            writer.write("----------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
