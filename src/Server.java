import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket Servesocket;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server=  new  Server(serverSocket);
        server.StartServer();
    }

    public Server(ServerSocket socket) {
        this.Servesocket = socket;
    }

    public void StartServer() {

        try {

            while (!Servesocket.isClosed()) {

                Socket socket = Servesocket.accept();
                System.out.println("Un cliente se ha conectado");
                Cliente clientnew = new Cliente(socket);

                Thread hilo = new Thread(clientnew);
                hilo.start();

            }

        } catch (IOException e) {

        }
    }

    public void closeServer(){
        try {
            if (Servesocket != null){
                Servesocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
