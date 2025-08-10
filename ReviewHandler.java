import java.io.*;
import java.net.*;
import java.sql.*;

public class ReviewHandler {
    public static void main(String[] args) {
        // MySQL connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/book_reviews";
        String dbUser = "root"; 
        String dbPassword = "password";

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

                    // Extract JSON data manually
                    String body = requestBody.toString();
                    String[] parts = body.split("\"");
                    String name = parts[3];
                    String rating = parts[7];
                    String review = parts[11];

                   
                    saveReviewToMySQL(jdbcURL, dbUser, dbPassword, name, Integer.parseInt(rating), review);
                }

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

    private static void saveReviewToMySQL(String jdbcURL, String user, String password, String name, int rating, String review) {
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");

            
            try (Connection connection = DriverManager.getConnection(jdbcURL, user, password)) {
                String sql = "INSERT INTO reviews (name, rating, review) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setInt(2, rating);
                statement.setString(3, review);
                statement.executeUpdate();
                System.out.println("Review saved to MySQL.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
