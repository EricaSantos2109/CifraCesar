package cesarcipher;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorClietes extends Thread {
    private Socket cliente;
    private String nomeCliente;
	private BufferedReader leitor;
	private PrintWriter escrior;
    private static final Map<String, GerenciadorClietes> clientes = new HashMap<String, GerenciadorClietes>();

    public GerenciadorClietes(Socket cliente) {
        this.cliente = cliente;
        start();
    }

    /*
     * Sobreescrição do método run da classe Thread
     */

    @Override
    public void run() {
        try {
            leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            escrior = new PrintWriter(cliente.getOutputStream(), true);
            escrior.println("Escreva o Nome:");
            String msg = leitor.readLine();
            this.nomeCliente = msg;
            escrior.println("Olá, " + this.nomeCliente);
            clientes.put(this.nomeCliente, this);

            while (true) {
                msg = leitor.readLine();
                if (msg.equalsIgnoreCase("::SAIR")) {
                    this.cliente.close();
                } else if (msg.toLowerCase().startsWith("::msg")) {
                	String nomeDestinatario = msg.substring(5, msg.length());
                	System.out.println("Enviado para: " + nomeDestinatario);
                	GerenciadorClietes destinatario = clientes.get(nomeDestinatario);
                	if (destinatario == null) {
                		escrior.println("O cliente informado não existe!");
                	} else if (msg.equalsIgnoreCase("::listar-clientes")) {
                		StringBuffer strB = new StringBuffer();
                		for (String gc : clientes.keySet()) {
							strB.append(gc);
							strB.append(",");
						}
                	}else {
                		escrior.println("Digite uma mensagem para " + destinatario.getNomeCliente()
                		+ "\n no seguinte formato: 'mensagem a ser enviada' - (nº da chave). Ex:\n olá, charlie! - 2");
                		String texto = leitor.readLine();
                		int chave = Character.getNumericValue(texto.charAt(texto.trim().length()-1));
                		destinatario.getEscrior().println(this.nomeCliente + " disse: " + CesarCipher.encriptar(chave, texto)); 
                	}
                }else {
                    escrior.println(this.nomeCliente + " disse: " + msg);
                }
            }
        } catch (IOException e) {
            System.err.println("O Cliente fechou a conexão!");
            e.printStackTrace();
        }
    }
    public PrintWriter getEscrior() {
		return escrior;
	}
    
   public String getNomeCliente() {
	   return nomeCliente;
   }
    
    public BufferedReader getLeitor() {
		return leitor;
	}
}