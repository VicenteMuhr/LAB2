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
    public boolean verificarVotante(Votante votante){
        List<Voto> listaVotos = new ArrayList<>(historialVotos);
        for (int i = 0; i < listaVotos.size(); i++) {
            Voto v = listaVotos.get(i);
            if (v.getVotanteID() == votante.getId()) {
                return true;
            }
        }
        return false;
    }
    public void reportarVoto(Candidato candidato, int idVoto){
        Iterator<Voto> iterator = candidato.getVotosRecibidos().iterator();
        while (iterator.hasNext()) {
            Voto v = iterator.next();
            if (v.getId() == idVoto) {
                votosReportados.add(v);
                iterator.remove();
                System.out.println("Voto reportado.");
                return;
            }
        }
    }
    public boolean registrarVoto(Votante votante, int candidatoID){
        if (verificarVotante(votante)) {
            System.out.println("El votante " + votante.getId() + " ya votó.");
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
        listaCandidatos.add(c);
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

        Candidato c1 = new Candidato(1, "Ana", "Partido A");
        Candidato c2 = new Candidato(2, "Luis", "Partido B");
        urna.agregarCandidato(c1);
        urna.agregarCandidato(c2);

        Votante v1 = new Votante(101, "Juan");
        Votante v2 = new Votante(102, "María");

        urna.registrarVoto(v1, 1); // Éxito
        urna.registrarVoto(v1, 1); // Fallo (ya votó)
        urna.registrarVoto(v2, 2); // Éxito

        urna.mostrarResultados();

        urna.reportarVoto(c1, 1); // Reporta el voto de Juan
        urna.mostrarResultados();
    }
}


