package Servidor;
import java.net.*; // Para Socket, ServerSocket
import java.io.*; // Para IOException e Input/OutputStream
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author rcunha
 */
public class Servidor extends Thread  {
	private static final int BUFSIZE = 50;
       
        @Override
       public void run () {
         Metodos_Servidor serv = new Metodos_Servidor();
         byte[] byteBuffer = new byte[BUFSIZE]; // Buffer de recpcao
         
         
         //Cria socket na porta inserida na main
	 ServerSocket servSock = null;
            try {
                servSock = new ServerSocket(Metodos_Servidor.porta);  
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
	 System.out.println("Servidor pronto para aceitar conexoes...");	
	
         //cria cliente e espera resposta
         Socket clntSock = null;
            try {
                clntSock = servSock.accept(); // Aceita a conexao com o cliente
                System.out.println("Atendimento do cliente " + clntSock.getInetAddress().getHostAddress() + " na porta " + clntSock.getPort());
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }


         //realiza as funcoes requeridas pelo cliente apos encerrar conexao (999 ou 888)
            while(true){
		 InputStream in = null;
             try {
                 in = clntSock.getInputStream();
             } catch (IOException ex) {
                 Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
             }
		 OutputStream out = null;
             try {
                 out = clntSock.getOutputStream();
             } catch (IOException ex) {
                 Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
             }
                 DataInputStream input = null;
             try {
                 input = new DataInputStream(clntSock.getInputStream());
             } catch (IOException ex) {
                 Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 in.read(byteBuffer);
             } catch (IOException ex) {
                 Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
             }
                 int i = byteBuffer[0];
                 System.out.println("Mensagem recebida número:  " + i);
                 
                 //chama os metodos do servidor para realizar 888
                 if(i==6){
                     serv.carregar_cliente(i, out, in);
                     break;
                 }
                 
                 //chama os metodos do servidor para realizar 888
                  if(i== 5){              
                    serv.atualizarLista_servidor(out, in, input);
                    break;
            }
                 else if(i== 7) 
                     break; 
	 }
               
          //Encerra sockets do cliente e servidor
            try {
                servSock.close();
                clntSock.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (int limpaTela = 0; limpaTela < 5; limpaTela++) {
                        System.out.println();
                }
         System.out.println("saiu co loop, fechou server");
      }
    }