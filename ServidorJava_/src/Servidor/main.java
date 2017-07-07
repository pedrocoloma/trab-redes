package Servidor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

public class main {
    static InputStreamReader isr = new InputStreamReader(System.in);
    static BufferedReader br = new BufferedReader(isr);
    	public static void main  (String[] args) throws IOException {
        Servidor serv = new Servidor();
        System.out.println("Porta 40013... ");
        
        /*
            Inicia o servidor e espera a conexao do cliente
        */
        serv.start();
            while(true){
                if(!serv.isAlive()){
                    serv = new Servidor();
                    serv.start();
               }
            }
        }

}
