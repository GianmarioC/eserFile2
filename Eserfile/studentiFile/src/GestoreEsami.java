import java.io.*;
import java.util.*;
import package1.Esame;
import package1.Materia;
import package1.Studente;

public class GestoreEsami {
    public static void main(String[] args) {
        ArrayList<Studente> studenti = new ArrayList<>();
        ArrayList<Materia> materie = new ArrayList<>();
        ArrayList<Esame> esami = new ArrayList<>();

        // Leggi studenti da file
        try {
            BufferedReader studentReader = new BufferedReader(new FileReader("studenti.txt"));
            String line;
            while ((line = studentReader.readLine()) != null) {
                String[] parts = line.split(",");
                int studenteId = Integer.parseInt(parts[0]);
                String studenteNome = parts[1];
                Studente studente = new Studente(studenteId, studenteNome);
                studenti.add(studente);
            }
            studentReader.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file degli studenti: " + e.getMessage());
        }

        // Leggi materie da file
        try {
            BufferedReader materiaReader = new BufferedReader(new FileReader("materie.txt"));
            String line;
            while ((line = materiaReader.readLine()) != null) {
                String[] parts = line.split(",");
                int materiaCodice = Integer.parseInt(parts[0]);
                String materiaNome = parts[1];
                Materia materia = new Materia(materiaCodice, materiaNome);
                materie.add(materia);
            }
            materiaReader.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file delle materie: " + e.getMessage());
        }

        // Leggi esami da file
        try {
            BufferedReader esameReader = new BufferedReader(new FileReader("esami.txt"));
            String line;
            while ((line = esameReader.readLine()) != null) {
                String[] parts = line.split(",");
                int studenteId = Integer.parseInt(parts[0]);
                int materiaCodice = Integer.parseInt(parts[1]);
                int voto = Integer.parseInt(parts[2]);

                Studente studente = findStudentById(studenti, studenteId);
                Materia materia = findMateriaByCodice(materie, materiaCodice);

                if (studente != null && materia != null) {
                    Esame esame = new Esame(studente, materia, voto);
                    esami.add(esame);
                }
            }
            esameReader.close();
        } catch (IOException e) {
            System.out.println("Errore nella lettura del file degli esami: " + e.getMessage());
        }
        
        Scanner scanner = new Scanner(System.in);

        int scelta;
        do {
            System.out.println("Opzioni:");
            System.out.println("1. Aggiungi nuovo studente");
            System.out.println("2. Aggiungi nuova materia");
            System.out.println("3. Registra nuovo esame");
            System.out.println("4. Esci");
            System.out.print("Scelta: ");

            scelta = scanner.nextInt();

            switch (scelta) {
                case 1:
                    System.out.print("Inserisci l'ID dello studente: ");
                    int studenteId = scanner.nextInt();
                    System.out.print("Inserisci il nome dello studente: ");
                    String studenteNome = scanner.next();
                    Studente studente = new Studente(studenteId, studenteNome);
                    studenti.add(studente);
                    break;

                case 2:
                    System.out.print("Inserisci il codice della materia: ");
                    int materiaCodice = scanner.nextInt();
                    System.out.print("Inserisci il nome della materia: ");
                    String materiaNome = scanner.next();
                    Materia materia = new Materia(materiaCodice, materiaNome);
                    materie.add(materia);
                    break;

                case 3:
                    System.out.print("Inserisci l'ID dello studente: ");
                    int esameStudenteId = scanner.nextInt();
                    System.out.print("Inserisci il codice della materia: ");
                    int esameMateriaCodice = scanner.nextInt();
                    System.out.print("Inserisci il voto: ");
                    int voto = scanner.nextInt();

                    Studente esameStudente = findStudentById(studenti, esameStudenteId);
                    Materia esameMateria = findMateriaByCodice(materie, esameMateriaCodice);

                    if (esameStudente != null && esameMateria != null) {
                        Esame esame = new Esame(esameStudente, esameMateria, voto);
                        esami.add(esame);
                    } else {
                        System.out.println("Studente o materia non trovati.");
                    }
                    break;

                case 4:
                    System.out.println("Uscita dal programma.");
                    scanner.close();
                    break;

                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        } while (scelta != 4);
        // Calcola la media dei voti per ogni materia
        for (Materia materia : materie) {
            double mediaVoti = calcolaMediaVotiPerMateria(materia, esami);
            System.out.println("Media voti per " + materia.getNome() + ": " + mediaVoti);
        }

        // Calcola la media dei voti per ogni studente
        for (Studente studente : studenti) {
            double mediaVoti = calcolaMediaVotiPerStudente(studente, esami);
            System.out.println("Media voti per lo studente " + studente.getNome() + ": " + mediaVoti);
        }

        // Ordina gli esami per voto decrescente
        Collections.sort(esami, new Comparator<Esame>() {
            @Override
            public int compare(Esame esame1, Esame esame2) {
                return Integer.compare(esame2.getVoto(), esame1.getVoto());
            }
        });

        // Stampa i voti degli studenti per ciascuna materia
        stampaVotiPerMateria(materie, esami);

        // Scrivi i report su file di testo
        scriviReport("media_voti_per_materia.txt", creaReportMediaVotiPerMateria(materie, esami));
        scriviReport("media_voti_per_studente.txt", creaReportMediaVotiPerStudente(studenti, esami));
    }

    private static Studente findStudentById(ArrayList<Studente> studenti, int id) {
        for (Studente studente : studenti) {
            if (studente.getId() == id) {
                return studente;
            }
        }
        return null;
    }

    private static Materia findMateriaByCodice(ArrayList<Materia> materie, int codice) {
        for (Materia materia : materie) {
            if (materia.getCodice() == codice) {
                return materia;
            }
        }
        return null;
    }

    private static double calcolaMediaVotiPerMateria(Materia materia, ArrayList<Esame> esami) {
        int totaleVoti = 0;
        int numeroEsami = 0;

        for (Esame esame : esami) {
            if (esame.getMateria().equals(materia)) {
                totaleVoti += esame.getVoto();
                numeroEsami++;
            }
        }

        if (numeroEsami > 0) {
            return (double) totaleVoti / numeroEsami;
        } else {
            return 0.0; // Nessun voto per questa materia
        }
    }

    private static double calcolaMediaVotiPerStudente(Studente studente, ArrayList<Esame> esami) {
        int totaleVoti = 0;
        int numeroEsami = 0;

        for (Esame esame : esami) {
            if (esame.getStudente().equals(studente)) {
                totaleVoti += esame.getVoto();
                numeroEsami++;
            }
        }

        if (numeroEsami > 0) {
            return (double) totaleVoti / numeroEsami;
        } else {
            return 0.0; // Lo studente non ha sostenuto esami
        }
    }

    private static void stampaVotiPerMateria(ArrayList<Materia> materie, ArrayList<Esame> esami) {
        for (Materia materia : materie) {
            System.out.println("Voti per " + materia.getNome() + ":");
            for (Esame esame : esami) {
                if (esame.getMateria().equals(materia)) {
                    System.out.println("Studente: " + esame.getStudente().getNome() + ", Voto: " + esame.getVoto());
                }
            }
        }
    }

    private static String creaReportMediaVotiPerMateria(ArrayList<Materia> materie, ArrayList<Esame> esami) {
        StringBuilder report = new StringBuilder();
        for (Materia materia : materie) {
            double mediaVoti = calcolaMediaVotiPerMateria(materia, esami);
            report.append("Media voti per ").append(materia.getNome()).append(": ").append(mediaVoti).append("\n");
        }
        return report.toString();
    }

    private static String creaReportMediaVotiPerStudente(ArrayList<Studente> studenti, ArrayList<Esame> esami) {
        StringBuilder report = new StringBuilder();
        for (Studente studente : studenti) {
            double mediaVoti = calcolaMediaVotiPerStudente(studente, esami);
            report.append("Media voti per lo studente ").append(studente.getNome()).append(": ").append(mediaVoti).append("\n");
        }
        return report.toString();
    }

    private static void scriviReport(String nomeFile, String contenuto) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(nomeFile, true));
            writer.println(contenuto);
            writer.close();
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file: " + e.getMessage());
        }
    }
}
