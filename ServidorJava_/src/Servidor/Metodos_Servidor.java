package Servidor;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    Classe com os métodos que o servidor irá usar
*/
public class Metodos_Servidor {
    static int porta = 40013;
    static String serv;
    public static int nulo = 0; 
    public static int branco = 0;
    
    public static int QTE_DE_CANDIDATOS = 5;
    
    /*
        Opcode 999 - Retorna todos os candidatos
     */
    public void carregar_cliente(int i,OutputStream out,InputStream in){
        for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
            System.out.println();
        }
        for(int j=0; j < QTE_DE_CANDIDATOS; j++){
            byte[] byteAux = new byte[50];
            byte[] byteBuffer = new byte[50];
            String nome =Candidato.candidatos[j].getNome();
            byteAux=nome.getBytes();
            try {
                out.write(Candidato.candidatos[j].getCodigo());
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                in.read(byteBuffer);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            i = byteBuffer[0];
            if(i==1){
                try {
                    out.write(byteAux);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                in.read(byteBuffer);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            i = byteBuffer[0];
            if(i==1){
                nome =Candidato.candidatos[j].getPartido();
                byteAux=nome.getBytes();
                try {
                    out.write(byteAux);                                               
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /*
        Opcode 888 - Vota
     */
    public void atualizarLista_servidor(OutputStream out,InputStream in, DataInputStream input){
        String votosaux;
        int auxiliar;
        int k = 0,l=0,m=0;
        int num;
        System.out.println("1");
        try {
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Metodos_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
                System.out.println("2");
                for(int j = 0; j < QTE_DE_CANDIDATOS; j++){
                   byte[] codigo = new byte[50];
                   byte[] nome = new byte[50];
                   byte[] qtdVotos = new byte[7];
                   byte[] partido = new byte[50];
                try {
                    auxiliar=input.readInt();
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {                            
                    out.write(1);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {                            
                    k=input.readInt();
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    out.write(1);                            
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                   Candidato.candidatos[j].incremento_votos(k);
                }        
        try {
             l=input.readInt();
             out.write(1);
        } catch (IOException ex) {
            Logger.getLogger(Metodos_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nulo+=l;
        
        try {
            m=input.readInt();
            out.write(1);
        } catch (IOException ex) {
            Logger.getLogger(Metodos_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
            branco += m;
            for (int limpaTela = 0; limpaTela <5; limpaTela++) {
               System.out.println();
            }
            System.out.println("Nulos: " +  nulo);
            System.out.println("Brancos: " +  branco);
            for (int limpaTela = 0; limpaTela < 4; limpaTela++) {
                System.out.println();
            }
            for(int j = 0; j < QTE_DE_CANDIDATOS; j++){
                System.out.println(Candidato.candidatos[j].getCodigo() + " - " + Candidato.candidatos[j].getPartido() + " - " + Candidato.candidatos[j].getNome() + " - " + 
                       Candidato.candidatos[j].getQtd_votos() );
               System.out.println(" -------------------------------- ");
            }
    }
}
