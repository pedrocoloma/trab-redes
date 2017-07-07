#include <sys/types.h>   // Definicao de tipos
#include <sys/socket.h>  // Biblioteca de estrutara de sockets
#include <netinet/in.h>  // Define os padroes de protocolo IP
#include <arpa/inet.h>   // Converte enderecos hosts
#include <stdio.h>       // Biblioteca padrao
#include <stdlib.h>      // Biblioteca padrao
#include <string.h>      // Para manipular as strings
#include <unistd.h>      // Define constantes POSIX
#include <errno.h>       // Utilizado para capturar mensagens de erro

#define QTE_CANDIDATOS 5 // Quantidade candidatos salvos
#define PORTA 22200      // Porta que o socket ouve
#define LEN 1024         // Tamanho dos dados recebidos


/*
  Classe Candidato
*/
struct no{
  int codigo_votacao;
  char nome_candidato[20];
  char partido[6];
  int num_votos;
  struct no *prox;
};
typedef struct no Candidato;

/*
  Contabiliza o voto
*/
int vota(char voto[], Candidato *Lista){
  int numero = atoi(voto);
  int i, pos = -1;
  printf("Usuario votou %d\n", numero);
  for (i = 0; i < QTE_CANDIDATOS; i++){
    if (Lista[i].codigo_votacao == numero){
      printf("Achou: %s\n", Lista[i].nome_candidato);
      pos = i;
    }
  }
  if(pos != -1){
    Lista[pos].num_votos += 1;
    printf("Voto computado com sucesso\n");
    return 1;
  }else{
    printf("Voto nao computado\n");
    return 0;
  }
}

/*
  Retornar todos os candidatos cadastrados (no caso do opcode 888)
*/
char *retorna_todos_os_candidatos(Candidato *Lista, int votos_brancos, int votos_nulos){
  int i;
  int total=0;
  char *resultado;
  char codigo[6];
  strcat(resultado, " -------------- Candidatos -----------\n");
  for(i = 0; i < QTE_CANDIDATOS; i++){   
    sprintf(codigo, "%d - ", Lista[i].codigo_votacao);
    strcat(resultado, codigo);
    strcat(resultado, Lista[i].partido);
    strcat(resultado, " - ");
    strcat(resultado, Lista[i].nome_candidato);
    strcat(resultado, " - ");
    sprintf(codigo, "%d", Lista[i].num_votos);
    strcat(resultado, codigo);
    strcat(resultado, " votos ");
    strcat(resultado, "\n");
    total += Lista[i].num_votos;
    
  }
  total += votos_brancos;
  total += votos_nulos;
  sprintf(codigo, "%d\n", votos_brancos);
  strcat(resultado, "\n    Brancos: ");
  strcat(resultado, codigo);
  sprintf(codigo, "%d\n", votos_nulos);
  strcat(resultado, "    Nulos: ");
  strcat(resultado, codigo);
  sprintf(codigo, "%d\n", total);
  strcat(resultado, "\n    Total: ");
  strcat(resultado, codigo);
  strcat(resultado, " -------------------------------------\n 999 - Listar todos os candidatos\n 888 - Votar\n");
  return resultado;
}

/*
  Funcao auxiliar que preenche a lista de candidatos
*/
Candidato *popula_a_lista(Candidato *Lista){
   Lista[0].codigo_votacao = 40;
  strcpy(Lista[0].nome_candidato, "Airton Garcia");
  strcpy(Lista[0].partido, "PSB");
  Lista[0].num_votos = 48951;

  Lista[1].codigo_votacao = 15;
  strcpy(Lista[1].nome_candidato, "Netto Donato");
  strcpy(Lista[1].partido, "PMDB");
  Lista[1].num_votos = 25016;

  Lista[2].codigo_votacao = 43;
  strcpy(Lista[2].nome_candidato, "Bragatto");
  strcpy(Lista[2].partido, "PV");
  Lista[2].num_votos = 23234;

  Lista[3].codigo_votacao = 45;
  strcpy(Lista[3].nome_candidato, "Paulo Altomani");
  strcpy(Lista[3].partido, "PSDB");
  Lista[3].num_votos = 14096;

  Lista[4].codigo_votacao = 13;
  strcpy(Lista[4].nome_candidato, "Lineu Navarro");
  strcpy(Lista[4].partido, "PT");
  Lista[4].num_votos = 7309;

  return Lista;
}

int main(int argc, char *argv[ ]){

  /*
    Lista de candidatos
  */
  Candidato *Lista = (malloc(QTE_CANDIDATOS*sizeof(Candidato)));

  Lista = popula_a_lista(Lista);

  /*
    Define as variaveis
  */
   int sock, connected, bytes_recv, i, true = 1;
   char send_data [LEN] , recv_data[LEN], msg[LEN];
   struct sockaddr_in server_addr, client_addr;
   int sin_size;

   int votos_brancos = 0;
   int votos_nulos = 0;

   char *resultado_999;

   /* 
    Funcao socket(sin_family,socket_type,protocol_number) retorna um inteiro (socket descriptor), caso erro retorna -1
   */ 
   if ((sock = socket(AF_INET, SOCK_STREAM, 0)) == -1){
     perror("Erro no Socket");
     exit(1);
   }

 
   /* Funcao setsockopt(int socket, int level, int optname, void*optval, size_t optlen)
	Esta funcao seta o valor (optval) de uma opcao (optname) em um certo nivel (level) de camada de protocolo no socket
	int socket = descriptor do socket
	int level = nivel da camada do protocolo (SOL_SOCKET = Constante de nivel para o socket, outros: IPPROTO_IP, IPPROTO_TCP, IPPROTO_UDP)
	int optname = Opcao desejada para a alteracao
	optval = valor da opcao
	optlen = tamanho do valor
  	Neste exemplo iremos alterar o valor no nivel de socket para a opcao SO_REUSEADDR. Por default um socket criado aceita apenas
   	uma conexao por endereco, ou seja o valor de SO_REUSEADDR é igual FALSE (0). Para alterar esse valor e permitirmos que o
   	mesmo endereco possa receber varias conexoes devemos alterar o valor da opcao SO_REUSEADDR para TRUE (1).
   */

   if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &true,sizeof(int)) == -1){
      perror("Erro Setsockopt");
      exit(1);
   }


   /*
      Configuracao do endereco do socket
   */
   server_addr.sin_family = AF_INET;
   server_addr.sin_port = htons(PORTA);
   server_addr.sin_addr.s_addr = INADDR_ANY;
   bzero(&(server_addr.sin_zero),8);


   /*
    Funcao bind usada para informar o endereco do socket
   */
   if (bind(sock, (struct sockaddr *)&server_addr, sizeof(struct sockaddr)) == -1){
      perror("erro ao executar o bind");
      exit(1);
   }


   /* 
      Funcao que fica 'ouvindo' as conexoes no socket
   */
   if (listen(sock, 10) == -1){
      perror("Erro de Listen");
      exit(1);
   }

   printf("\nServidor TCP esperando por conexoes na porta 22200\n");
   fflush(stdout);


   /*
    Loop para receber diferentes conexoes
   */
   while(1){

      /*
        Funcao accept aceita uma nova conexao e cria um socket pra essa conexao
      */
      sin_size = sizeof(struct sockaddr_in);
      connected = accept(sock, (struct sockaddr *)&client_addr, &sin_size);
      printf("\nConexão aceita de (%s , %d)\n", inet_ntoa(client_addr.sin_addr), ntohs(client_addr.sin_port));
      strcpy(msg, "Bem vindo!\n 999 - Listar todos os candidatos\n 888 - Votar\n");
      send(connected, msg, strlen(msg), 0);

      /*
        Loop para trocar informacoes com o usuario
      */
      while (1){
/*
         printf("\n Envie (s or S para sair) : ");
         fgets(send_data, 1024, stdin);
         send(connected, send_data, strlen(send_data), 0);// Função send(int socket, void*buffer, size_t size, int flags)
*/
         sleep(1);

         if (strcmp(send_data,"s\n") == 0 || strcmp(send_data,"S\n") == 0){
            printf("\n Bye...\n");
            fflush(stdout);
            close(connected);
            break;
         }
         else{

            // Funcao recv (int socket, void *buffer, size_t size, int flags)        
            bytes_recv = recv(connected, recv_data, 1024, 0);

            recv_data[bytes_recv] = '\0';
               
            if (strcmp(recv_data,"s\n") == 0 || strcmp(recv_data,"S\n") == 0){
               close(connected);
               printf("\nO Cliente encerrou a conexao!\n");
               fflush(stdout);
               break;
            }
            else
               printf("\n Mensagem recebida: %s \n" , recv_data);
               /*
                  Opcode 999 - Retorna todos os candidatos
               */
               if(strcmp(recv_data, "999\n") == 0){
                  resultado_999 = retorna_todos_os_candidatos(Lista, votos_brancos, votos_nulos);
                  send(connected, resultado_999, strlen(resultado_999), 0);
               }

               /*
                  Opcode 888 - Vota
               */
               if (strcmp(recv_data, "888\n") == 0){
                 strcpy(msg, "Opcode pra receber os votos\n 1 - Votar\n 2 - Votar em branco \n 3 - Votar nulo\n");
                 send(connected, msg, strlen(msg), 0);
               }

               /*
                Caso o usuario opte por votar em um candidato
               */
               if (strcmp(recv_data, "1\n") == 0){
                 strcpy(msg, "Codigo de votacao: \n");
                 send(connected, msg, strlen(msg), 0);
                 bytes_recv = recv(connected, recv_data, 1024, 0);
                 recv_data[bytes_recv] = '\0';
                 int resultado = vota(recv_data, Lista);
                 if(resultado == 1){
                  strcpy(msg, "Voto computado com sucesso\n");
                  send(connected, msg, strlen(msg), 0);
                 }else if(resultado == 0){
                  strcpy(msg, "Candidato nao cadastrado\n");
                  send(connected, msg, strlen(msg), 0);
                 }else{
                  strcpy(msg, "Ocorreu um erro e seu voto nao foi computado\n");
                  send(connected, msg, strlen(msg), 0);
                 }
               }

               /*
                  Caso o usuario opte por votar em branco
               */
               if (strcmp(recv_data, "2\n") == 0){
                 votos_brancos++;
                 strcpy(msg, "Voto em branco computado!\n");
                 send(connected, msg, strlen(msg), 0);
               }

               /*
                  Caso o usuario opte por votar nulo
               */
               if (strcmp(recv_data, "3\n") == 0){
                 votos_nulos++;
                 strcpy(msg, "Voto nulo computado!\n");
                 send(connected, msg, strlen(msg), 0);
               }
           }
         
         fflush(stdout);
      }
   }
   
   close(sock);
   free(Lista);
   return 0;
}
