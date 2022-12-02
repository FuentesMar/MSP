import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConexionCliente {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private  String username;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingresa tu usuario para el chat");
        String username = sc.nextLine();
        Socket soket = new Socket("localhost", 1234);
        ConexionCliente conexionCliente = new ConexionCliente(soket, username);
        conexionCliente.getMessage();
        conexionCliente.sendMessage();
    }


    public ConexionCliente(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.username= username;
        }catch (IOException e){
            closeAll(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String envirmensaje = sc.nextLine();
                bufferedWriter.write(username + ": " + envirmensaje);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    public void getMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msjaotrousuario;

                while(socket.isConnected()){
                    try {
                        msjaotrousuario = bufferedReader.readLine();
                        System.out.println(msjaotrousuario);
                    }catch (IOException e){
                        closeAll(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if( bufferedReader != null){
                bufferedReader.close();
            }
            if( bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

