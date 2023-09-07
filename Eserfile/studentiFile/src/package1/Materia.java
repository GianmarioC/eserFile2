package package1;

public class Materia {
    private int codice;
    private String nome;

    public Materia(int codice, String nome) {
        this.codice = codice;
        this.nome = nome;
    }

    public int getCodice() {
        return codice;
    }

    public String getNome() {
        return nome;
    }
}
