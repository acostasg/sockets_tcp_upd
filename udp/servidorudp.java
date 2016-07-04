
/**
 * Classe servidor java Protocol UDP NO orientat a la connexió
 *
 * Importem les llibreries necesaries per als diferents metodes que utilitzarem
 * per la programació de sockets UDP
 *
 * java.net 
 * Implementa tota la funcionalitat dels sockets. La libreria java.net
 * ens permet instanciar les classes DatagramSocket i DatagramPacket, la primera
 * per als objectes sockets UDP i la segona per als paquets que s'han d'enviar.
 *
 * java.io 
 * Pel que fa java.io la seva importació ens pemet la utilització de
 * fluxes d'entrada i sortida del sistema, aixi com al serialització i el
 * sistema de arxius.
 *
 */
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class servidorudp {

    private static final String CAFE = "CO";
    private static final String TE = "TE";
    private static final String SUCRE = "SU";
    private static final String LLET = "MI";
    private static final String OK = "OK";
    private static final String SALUTACIO = "HI";
    private static final String ADEU = "BYE";
    private static final String BEGUDA = "BE";
    private static final String COMPLEMENTS = "CM";
    private static final String PERDO = "SORRY?";
    
    //mapa de respostes per cada ordre o comanda del client
    private static Map<String, String> mapaOrdres = new HashMap<>();
    
    //estat begudes i colecció de complements
    private static Map<String, String> estatBeguda = new HashMap<String, String>();
    
    // boolean (verdader/false) per saber si l'aplicació client a iniciat
    // la conversa amb la salutació correcta
    private static boolean statusSalutacio = false;

    /**
     * Metode static main, que tota aplicació java ha de tenir almenys en una
     * classe java, iniciara la aplicació java, permeten la entrada de valors,
     * en aquest cas no es necesari cap valor d'entrada
     *
     * @param argv
     */
    public static void main(String argv[]) {

        //respostes per escriure per pantalla i enviar/respondre al client
        mapaOrdres.put(SALUTACIO, "HI");
        mapaOrdres.put(OK, "OK");
        mapaOrdres.put(CAFE, "coffe");
        mapaOrdres.put(TE, "tea");
        mapaOrdres.put(SUCRE, "sugar");
        mapaOrdres.put(LLET, "milk");

        //mapa clau/valor per guardar la coleció de complements
        estatBeguda.put(COMPLEMENTS, "");

        /*
         * Declaració del objecte DatagramSocket per utilitzar un socket per
         * rebre datagrames i per enviar al client
         * 
         */
        DatagramSocket socket;

        //control d'excepcions
        try {
            /**
             *** Creem un socket i li assignemt l'adreça *** Instanciació del
             * objecte socket de la classe DatagramSocket en el contructor del
             * objecte assignem aquest socket o connexio en el port 6000 que
             * l'identificara en la maquina/servidor (recordem que el ports de
             * l'1 al 1023 estan reservats)
             *
             */
            socket = new DatagramSocket(6000);

            /**
             *  Intercanvi de dades amb el client NO orientat a la conexio
             * La lectura es realitza amb un bucle llegint els missatges de la
             * aplicació client. S'aniran mostrant per pantalla
             */
            do {

                //Array o agrupació de d'objectes byte per usarem per rebre/enviar els datagrames
                byte[] mensaje_bytes = new byte[256];
                byte[] mensaje_bytes_salida = new byte[256];

                //Instanciam l'objecte "paquete" de la classe DatagramPacket el qual 
                // es rebra el datagrama
                DatagramPacket paquete = new DatagramPacket(mensaje_bytes, 256);
                /*
                 *** Recepció o entrada del datagrama ***
                 * Rep un paquet de datagrama des d'aquest connector. Quan aquest mètode
                 * rep el paquet del client el buffer està ple de les dades rebudes.
                 * El paquet també conté el datagrama del remitent
                 * Adreça IP, i el nombre de port a la màquina del remitent.
                 */
                socket.receive(paquete);
                
                //iniciem el missatge eliminan el contingut anterior de la variable
                String mensaje = "";
                String missatge_sortida = "";
                
                //mitjant la classe String convertim el missatge en cadena de text
                mensaje = new String(mensaje_bytes).trim();

                if (mensaje.startsWith(ADEU)) {
                    System.out.println(ADEU); //escrivim BYE en la consola
                    statusSalutacio = false; //reiniciam l'estat de la salutació
                    missatge_sortida = ADEU;
                } else {
                    //mostrem aquesta cadena de texte que es el missatge per pantalla
                    missatge_sortida = analisis(mensaje);
                    System.out.println(missatge_sortida);
                }

                //obtenim les dades del remiten del paquet
                int port = paquete.getPort(); //el port UDP client
                InetAddress adressa = paquete.getAddress(); //adressa IP del client

                //Generem el missatge de sortida en Bytes
                mensaje_bytes_salida = missatge_sortida.getBytes();
                // Paquet de tornada amb el remiten del pagquet d'entrada  
                
                DatagramPacket paquetSortida = new DatagramPacket(mensaje_bytes_salida, missatge_sortida.length(), adressa, port);
                // Enviament del paquet de resposta amb protocl UDP al destinatari  
                socket.send(paquetSortida);
            } while (true);
        } //Qualsevol execepticó de la aplicació de servidor tancaria 
        //la connexió i mostraria per pantalla o consola el error o exepció
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    //lògica de comandes y resposta a l'aplicació client 
    private static String analisis(String mensaje) {
        //condicional per veure si la comanda es correcta
        if (mensaje.length() >= 2 && mapaOrdres.containsKey(mensaje.substring(0, 2))) {
            //condicional de control per la salutació inicial
            if (mensaje.startsWith(SALUTACIO)) {
                statusSalutacio = true;
            }

            if (statusSalutacio == true) {
                String descripcioOrdre = mapaOrdres.get(mensaje.substring(0, 2));
                String ordre = mensaje.substring(0, 2);

                //processament de la orde del client
                switch (ordre) {
                    case CAFE:
                    case TE:
                        if (estatBeguda.containsKey(BEGUDA)) {
                            descripcioOrdre = PERDO;
                        } else {
                            estatBeguda.put(BEGUDA, mapaOrdres.get(ordre));
                        }
                        break;
                    case SUCRE:
                    case LLET:
                        estatBeguda.put(COMPLEMENTS, estatBeguda.get(COMPLEMENTS) + "," + descripcioOrdre);
                        break;
                    case OK:
                        if (estatBeguda.containsKey(BEGUDA)) {
                            descripcioOrdre = estatBeguda.get(BEGUDA) + estatBeguda.get(COMPLEMENTS);
                            estatBeguda.remove(BEGUDA);
                            estatBeguda.put(COMPLEMENTS, "");
                        } else {
                            descripcioOrdre = PERDO;
                        }
                        break;
                }

                if (descripcioOrdre != null) {
                    if (!mensaje.startsWith(SALUTACIO)) {
                        mensaje = mensaje + ": " + descripcioOrdre;
                    } else {
                        mensaje = SALUTACIO;
                    }
                }

            } else {
                mensaje = mensaje + ": " + PERDO;
            }
        } else {
            mensaje = mensaje + ": " + PERDO;
        }

        //tornem el resultat de la ordre
        return mensaje;
    }
}
