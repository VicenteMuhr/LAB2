import java.time.LocalTime;
import java.util.*;

class Voto {
    private int id;
    private int votanteID;
    private int candidatoID;
    private String timestamp;

    public Voto(int id, int votanteID, int candidatoID) {
        this.id = id;
        this.votanteID = votanteID;
        this.candidatoID = candidatoID;
        this.timestamp = LocalTime.now().toString(); // tiempo automático
    }

    // Getters y setters
    public int getId() { return id; }
    public int getVotanteID() { return votanteID; }
    public int getCandidatoID() { return candidatoID; }
    public String getTimestamp() { return timestamp; }
    public void setID(int newID) {this.id = newID;}
    public void setVotanteID(int newVotanteID) {this.votanteID = newVotanteID;}
    public void setCandidatoID(int newCandidatoID) {this.candidatoID = newCandidatoID;}
    public void setTimeStamp(String newTimeStamp) {this.timestamp = newTimeStamp;}
}

class Candidato {
    private int id;
    private String nombre;
    private String partido;
    private Queue<Voto> votosRecibidos;

    public Candidato(int id, String nombre, String partido) {
        this.id = id;
        this.nombre = nombre;
        this.partido = partido;
        this.votosRecibidos = new LinkedList<>();
    }

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPartido(){return partido;}
    public Queue<Voto> getVotosRecibidos() { return votosRecibidos; }
    public void setID(int newID){this.id = newID;}
    public void setNombre(String newNombre) {this.nombre = newNombre;}
    public void setPartido(String newPartido) {this.partido = newPartido;}
    public void setVotosRecibidos(Queue<Voto> newVotosRecibidos) {this.votosRecibidos = newVotosRecibidos;}
    public void agregarVoto(Voto v) {
        votosRecibidos.add(v);
    }
}

class Votante {
    private int id;
    private String nombre;
    private boolean yaVoto;

    public Votante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.yaVoto = false;
    }

    // Getters y setters
    public int getId() { return id; }
    public boolean getYaVoto() { return yaVoto; }
    public void setID(int newID){this.id = newID;}
    public void setNombre(String newNombre) {this.nombre = newNombre;}
    public void setYaVoto(boolean newYaVoto) {this.yaVoto = newYaVoto;}
    public void marcarComoVotado() {this.yaVoto = true;}
}

class UrnaElectoral {
    private LinkedList<Candidato> listaCandidatos; // Corrección: camelCase
    private Stack<Voto> historialVotos;
    private Queue<Voto> votosReportados;
    private int idCounter;

    public UrnaElectoral() {
        this.listaCandidatos = new LinkedList<>();
        this.historialVotos = new Stack<>();
        this.votosReportados = new LinkedList<>();
        this.idCounter = 1; // Contador id
    }
    public LinkedList<Candidato> getListaCandidatos(){return this.listaCandidatos;}

    public boolean verificarVotante(Votante votante){
        for (Voto v : historialVotos) {
            if (v.getVotanteID() == votante.getId()) {
                for (Candidato c : listaCandidatos) {
                    if (c.getId() == v.getCandidatoID()) {
                        reportarVoto(c, v.getId());
                        System.out.println("Voto duplicado reportado.");
                        return true;
                    }
                }
            }
        }
        return votante.getYaVoto();
    }
    public void reportarVoto(Candidato candidato, int idVoto){
        boolean candidatoEnUrna = false;
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato c = listaCandidatos.get(i);
            if (c.getId() == candidato.getId()) {
                candidatoEnUrna = true;
                break;
            }
        }
        if (!candidatoEnUrna) {
            System.out.println("Error: El candidato no está registrado en la urna.");
            return;
        }
        Iterator<Voto> iterator = candidato.getVotosRecibidos().iterator();
        boolean votoEncontrado = false;
        while (iterator.hasNext()) {
            Voto v = iterator.next();
            if (v.getId() == idVoto) {
                votosReportados.add(v);
                iterator.remove();
                votoEncontrado = true;
                System.out.println("Voto reportado exitosamente.");
                break;
            }
        }
        if (!votoEncontrado) {
            System.out.println("Error: El voto no existe para este candidato.");
        }
    }
    public boolean registrarVoto(Votante votante, int candidatoID){
        if (verificarVotante(votante)) {
            return false;
        }

        // Buscar candidato con for clásico
        Candidato candidato = null;
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato c = listaCandidatos.get(i);
            if (c.getId() == candidatoID) {
                candidato = c;
                break;
            }
        }
        if (candidato == null) {
            System.out.println("Candidato no existe.");
            return false;
        }
        Voto nuevoVoto = new Voto(idCounter, votante.getId(), candidatoID);
        idCounter++;
        candidato.agregarVoto(nuevoVoto);
        historialVotos.push(nuevoVoto);
        votante.marcarComoVotado();

        return true;
    }
    public void agregarCandidato(Candidato c) {
        Candidato nuevoCandidato = new Candidato(c.getId(), c.getNombre(), c.getPartido());
        listaCandidatos.add(nuevoCandidato);;
    }
    public void mostrarResultados() {
        System.out.println("\n--- Resultados de la votación ---");
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato c = listaCandidatos.get(i);
            System.out.println("Candidato: " + c.getNombre() +
                    " | Partido: " + c.getPartido() +
                    " | Votos obtenidos: " + c.getVotosRecibidos().size());
        }
        System.out.println("----------------------------------\n");
    }
}
public class Main {
    public static void main(String[] args) {

        UrnaElectoral urna = new UrnaElectoral();

        Candidato c1 = new Candidato(1, "Ballerina Capuccina", "One piece");
        Candidato c2 = new Candidato(2, "Tralalero tralala", "HunterxHunter");
        urna.agregarCandidato(c1);
        urna.agregarCandidato(c2);

        Votante v1 = new Votante(1, "Lord Farquad");
        Votante v2 = new Votante(2, "Shrek");

        urna.registrarVoto(v1, 1); // Éxito
        urna.registrarVoto(v1, 1); // Fallo (ya votó)
        urna.registrarVoto(v2, 2); // Éxito

        urna.mostrarResultados();

        Scanner Scanner = new Scanner(System.in);
        System.out.println("Sistema de votaciones");

        while (true) {
            System.out.println("\n 1. Agregar Candidato");
            System.out.println(" 2. Agregar Voto");
            System.out.println(" 3. Mostrar Resultados de votaciones");
            System.out.println(" 4. Salir");
            System.out.println(" ingresa opcion: ");
            int opcion = Scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.print("ID del candidato: ");
                    int idCandidato = Scanner.nextInt();
                    Scanner.nextLine();

                    boolean existe = false;
                    LinkedList<Candidato> candidatos = urna.getListaCandidatos();
                    for (int i = 0; i < candidatos.size(); i++) {
                        Candidato c = candidatos.get(i);
                        if (c.getId() == idCandidato) {
                            existe = true;
                            break;
                        }
                    }
                    if (existe) {System.out.println("Error: ID ya registrado");break;}

                    System.out.print("Nombre: ");
                    String nombre = Scanner.nextLine();

                    System.out.print("Partido: ");
                    String partido = Scanner.nextLine();

                    Candidato nuevoCandidato = new Candidato(idCandidato, nombre, partido);
                    urna.agregarCandidato(nuevoCandidato);
                    System.out.println("Candidato registrado.");
                    break;
                case 2:
                    System.out.print("ID del votante: ");
                    int idVotante = Scanner.nextInt();
                    Scanner.nextLine();

                    System.out.print("Nombre del votante: ");
                    String nom = Scanner.nextLine();

                    System.out.print("ID del Candidato: ");
                    int idCandVoto = Scanner.nextInt();
                    Scanner.nextLine();

                    Votante votante = new Votante(idVotante, nom);
                    if (urna.registrarVoto(votante, idCandVoto)) {
                        System.out.println("Voto registrado correctamente");
                    } else {
                        System.out.println("Error al registrar voto");
                    }
                    break;
                case 3:
                    urna.mostrarResultados();
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    Scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }
}


