package cesarcipher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public CesarCipher cc;
    public static void main(String[] args) {

        try {
            final Socket cliente = new Socket("127.0.0.1", 9876);
            
            //lendo mensagem do servidor
            new Thread(){
                @Override
                public void run(){
                    try {
                        BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

                        while (true) {
                            String mensagem = leitor.readLine();
                            
                            try {
                            	// se for mensagem de cliente ele vai tentar
                            	// descriptografar
                            	String texto = leitor.readLine();
                        		int chave = Character.getNumericValue(texto.charAt(texto.length()-1));
                        		System.out.println(CesarCipher.decriptar(chave, texto));
                            } catch (Exception e) {
                            	// se não for, ele vai exibir a mensagem normalmente
                            	System.out.println("Server says: " + mensagem);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Impossível  ler a mensagem do Servidor");
                    }
                }
            }.start();

            PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
            BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));
            String msgTerminal = "";
            while (true) {
                msgTerminal = leitorTerminal.readLine();
                if (msgTerminal == null || msgTerminal.length() == 0) {
                    continue;
                }
                escritor.println(msgTerminal);
                if(msgTerminal.equalsIgnoreCase("::SAIR")){
                    System.exit(0);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Endereço de IP inválido!");
            e.printStackTrace();
        } catch(IOException e){
            System.out.println("Servidor pode estar fora do ar!!");
            e.printStackTrace();
        }
    }

}