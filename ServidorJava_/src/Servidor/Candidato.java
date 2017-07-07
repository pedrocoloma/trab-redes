package Servidor;

/*
  Classe Candidato
*/
public class Candidato {
    int codigo_votacao;
    String nome_candidato;
    String partido;
    int num_votos;
    
    /*
        Lista de candidatos em array
    */
    static Candidato[] candidatos = {
        new Candidato(40,"Airton Garcia","PSB",48951),
         new Candidato(15,"Netto Donato","PMDB",25016),
         new Candidato(43,"Bragatto","PV",23234),
         new Candidato(45,"Paulo Altomani","PSDB",14096),
         new Candidato(13,"Lineu Navarro","PT",7309)
    };
    
    /*
        Construtor sem o numero de votos
    */
    public Candidato(int codigo, String nome, String partido){
        codigo_votacao=codigo;
        nome_candidato=nome;
        this.partido=partido;
        this.num_votos=0;
    }
    
    /*
        Construtor com o numero de votos
    */
    public Candidato(int codigo, String nome, String partido,int votos){
        codigo_votacao=codigo;
        nome_candidato=nome;
        this.partido=partido;
        this.num_votos=votos;   
    }
    
    /*
        Setters e Getters
    */
    public int getCodigo(){
        return codigo_votacao;
    } 
    
    public String getNome(){
        return nome_candidato;   
    }
    
    public String getPartido(){
        return partido;   
    } 
    
    public int getQtd_votos(){
        return num_votos;
    }
    
    public void incremento_votos(){
        num_votos= num_votos + 1;
    }
    
    public void incremento_votos(int total ){
        num_votos= num_votos + total;
    } 
    
}
