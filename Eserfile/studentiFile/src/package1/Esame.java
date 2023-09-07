package package1;

public class Esame {
    private Studente studente;
    private Materia materia;
    private int voto;

    public Esame(Studente studente, Materia materia, int voto) {
        this.studente = studente;
        this.materia = materia;
        this.voto = voto;
    }

    public Studente getStudente() {
        return studente;
    }

    public Materia getMateria() {
        return materia;
    }

    public int getVoto() {
        return voto;
    }
}
