package cesarcipher;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket servidor = null;

        try {
            System.out.println("Inicializando do servidor");
            servidor = new ServerSocket(9876);
            System.out.println("Servidor Iniciado!");
            while(true){
                Socket cliente = servidor.accept();
                new GerenciadorClietes(cliente);
            }
        } catch (IOException e) {

            try {
                if (servidor != null) {
                    servidor.close();
                }
            } catch (IOException el) { }

            System.err.println("Porta ocupada ou "
                +"Servidor fechado!");
            e.printStackTrace();
        }

    }
}
