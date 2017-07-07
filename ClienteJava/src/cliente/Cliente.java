package cliente;
        
import java.net.*; // Para Socket
import java.io.*; // Para IOException e Input/OutputStream
import java.util.Arrays;

public class Cliente {
    public static void main (String[] arg) throws IOException {
        /*
            Variaveis
        */
        int opcao = 0;
        boolean carregouLista = false;
        boolean votou = false;
        Candidato[] candidatos = new Candidato[6];
        int nulo=0, branco=0;
        int QTE_DE_CANDIDATOS = 5;
         
        /*
            Le a porta e endereco
        */
        System.out.println("Digite a porta");
        int porta=EntradaTeclado.leInt();
        System.out.println("Digite o endereco (localhost 1,cosmos.lasdpc.icmc.usp.br 2: ");
        int ler=EntradaTeclado.leInt();
        String serv="localhost";
        if(ler==2)serv="cosmos.lasdpc.icmc.usp.br";
        if(ler==1)serv="localhost";
        
        /*
            Cria um socket
        */
        Socket socket = new Socket(serv, porta);

        System.out.println("Eviando mensagem ...");
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream(); 
        
        /*
            Loop que roda enquanto o cliente nao finalizar a votacao
        */
        while(opcao != 888)
        {
            for (int limpaTela = 0; limpaTela < 5; limpaTela++) {
                System.out.println();
            }
            
            System.out.println("999: listar todos os candidatos");
            System.out.println("888: Finaliza a votacao e envia para o servidor");
            System.out.println("1: Votar");
            System.out.println("2: Votar em branco");
            System.out.println("3: Votar nulo");
            
            opcao = EntradaTeclado.leInt();
            /*
                Opcode para votar
            */
            if( (opcao == 1 && !carregouLista) || (opcao == 2 && !carregouLista) || (opcao == 3 && !carregouLista)) {
                for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
                        System.out.println();
                }
                
                System.out.println("A lista deve ser carregada antes da votação!");
            }
            if(opcao == 1 && carregouLista) {
                
                for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
                        System.out.println();
                }
                System.out.println("Digite o codigo de votacao:");
                int codigoCandidato = EntradaTeclado.leInt();
                
                for(int i = 0; i < QTE_DE_CANDIDATOS; i++){
                    
                    if(candidatos[i].getCodigo() == codigoCandidato){
                        candidatos[i].num_votos++;
                        System.out.println("Candidato: " + candidatos[i].getNome() +"\nPartido: " + candidatos[i].getPartido()+"\n");  
                        votou = true;
                        break;
                    }
                        
                }                       
            }
            
            /*
                Voto em branco
            */
            if(opcao == 2 && carregouLista){
                branco++;
                for (int limpaTela = 0; limpaTela < 4; limpaTela++) {
                        System.out.println();
                }
                System.out.println("Voto Branco.");
                votou = true;
            }
            
            /*
                Voto em nulo
            */
            if(opcao == 3 && carregouLista){
                nulo++;
                for (int limpaTela = 0; limpaTela < 4; limpaTela++) {
                        System.out.println();
                }
                System.out.println("Voto Nulo.");
                votou = true;
            }
            
            /*
                Opcode 888 - Vota
            */
            if(opcao == 888 && votou == true){
                byte[] byteBuffer = new byte[50];
                int verificaEstado=1;
                
             
                if(votou){
                 socket = new Socket (serv, porta);
                 
                 for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
                        System.out.println();
                }
                 
                 System.out.println("Conectado ao servidor outra vez... enviando a mensagem");
                 DataOutputStream newout2;
                            newout2 = new DataOutputStream(socket.getOutputStream());       
                 in = socket.getInputStream();

                 out = socket.getOutputStream(); 
                 out.flush();
                
                System.out.println("1");
                out.write(5);
                System.out.println("2");
                    for(int i = 0; i < QTE_DE_CANDIDATOS; i++){

                        byte[] byteAux = new byte[50];                        
                        /*
                            Envia o codigo de votacao
                        */
                        System.out.println("3");
                        newout2.writeInt(candidatos[i].codigo_votacao);
                        System.out.println("4");
                        in.read(byteBuffer);
                        System.out.println("5");
                        verificaEstado = byteBuffer[0];
                        if(verificaEstado == 1){
                            int qtdVotos = candidatos[i].num_votos;
                            DataOutputStream newout;
                            newout = new DataOutputStream(socket.getOutputStream());
                           
                            newout.writeInt(qtdVotos);                           
                        }
                        
                        /*
                            verifica se o servidor recebeu as informacoes
                        */
                        in.read(byteBuffer);
                        verificaEstado = byteBuffer[0];
                        if(verificaEstado == 1){                           
                        }                      
                    }
                    
                    /*
                        Enviando votos brancos e nulos
                    */
                    DataOutputStream newoutNU,newoutBR;
                    newoutNU = new DataOutputStream(socket.getOutputStream());
                    newoutBR = new DataOutputStream(socket.getOutputStream());

                   /*
                        Enviando voto nulo
                    */
                    newoutNU.writeInt(nulo);
                    in.read(byteBuffer);
                    /*
                        Enviando voto em branco
                    */
                    newoutBR.writeInt(branco);
                    in.read(byteBuffer);
                            
                    for (int limpaTela = 0; limpaTela < 4; limpaTela++) {
                        System.out.println();
                }
                    System.out.println("Sessão Finalizada.");
                   }
                 }
            
            /*
                Opcode 99 - carrega lista
            */                               
            if(opcao == 999 && carregouLista==false){
                
                for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
                        System.out.println();
                }
                
                System.out.println("enviar 999");
                out.write(6);
                System.out.println("enviado...Esperando");
                for (int limpaTela = 0; limpaTela < 4; limpaTela++) {
                        System.out.println();
                }
               
                int auxiliar;
                System.out.println(" --------------------- ");
                for(int i = 0; i < QTE_DE_CANDIDATOS; i++){
                    
                   byte[] codigo = new byte[50];
                   byte[] nome = new byte[50];
                   byte[] partido = new byte[50];
                   
                    /*
                        Codigo de votacao
                   */
                    in.read(codigo);
                    auxiliar = codigo[0];        
                    out.write(1);

                    /*
                        Nome do candidato
                    */
                    in.read(nome);
                    out.write(1);
                    
                    /*
                        Partido
                    */
                    in.read(partido);
                              
                   candidatos[i] = new Candidato(auxiliar, new String(nome), new String(partido));
                    
                   System.out.println("candidato " + i + ": \nCodigo:" +
                   candidatos[i].getCodigo() + " " + candidatos[i].getNome() + " " + candidatos[i].getPartido()+"\n");

                }        
                System.out.println("**********************************************");
                System.out.println("Lista Carregada.");
                 System.out.println();
                 System.out.println();
                carregouLista = true;
                
                socket.close();
            }
            
           if(opcao == 7){
               
               System.out.println("Fechando...");
               out.write(7);              
               break;
           }
            
        } 
        socket.close();
        for (int limpaTela = 0; limpaTela < 10; limpaTela++) {
            System.out.println();
        }
        System.out.println("Conexao encerrada.");
    }

}
