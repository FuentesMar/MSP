import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Cliente implements Runnable{

    public static ArrayList<Cliente> clienteArrayList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private  String clientusername;

    public Cliente(Socket socket){

        try{
            this.socket = socket;
            this.bufferedWriter =  new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            this.clientusername = bufferedReader.readLine();
            clienteArrayList.add(this);
            msjsalida("Servidor: " + clientusername + " ha entrado en el chat");
        }catch (IOException e){
            closeAll(socket,bufferedReader,bufferedWriter);
        }
    }


    @Override
    public void run(){
        String msjcliente;

        while(socket.isConnected()){
            try{
                msjcliente = bufferedReader.readLine();
                msjsalida(msjcliente);
            }catch (IOException e){
                closeAll(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void msjsalida(String msjmandado){
        for (Cliente cliente : clienteArrayList){
            try {
               if (!cliente.clientusername.equals(clientusername)){
                   cliente.bufferedWriter.write(msjmandado);
                   cliente.bufferedWriter.newLine();
                   cliente.bufferedWriter.flush();
               }
            }catch (IOException e){
                closeAll(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    public void removecliente(){
        clienteArrayList.remove(this);
        msjsalida("Servidor " + clientusername + " ha salido del chat");
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
            removecliente();
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
