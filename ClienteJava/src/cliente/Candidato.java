package cliente;

public class Candidato {
    int codigo_votacao;
    String nome_candidato;
    String partido;
    int num_votos;
    
    
    public Candidato(int codigo, String nome, String partido){
        codigo_votacao = codigo;
        nome_candidato = nome;
        this.partido = partido;
        this.num_votos = 0;
    }
    
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

}