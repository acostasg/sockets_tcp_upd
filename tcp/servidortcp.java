
/**
 * Classe servidor java TCP Protocol TCP orientat a la connexió
 *
 * Importem les llibreries necesaries per als diferents metodes que utilitzam
 * per la programació de sockets
 *
 * java.net La libreria java.net ens permet instanciar les classes Socket y
 * ServerSocket (Servidor socket) per la comunicació a més de InetAddress que
 * s'utilitza en la aplicació de Client
 *
 * java.io Pel que fa java.io la seva importació ens pemet la utilització de
 * fluxes d'entrada i sortida del sistema, aixi com al serialització i el
 * sistema de arxius.
 *
 */
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class servidortcp {

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


        do {
            /**
             * Declaració objecte ServerSocket (Servidor socket)
             *
             * Inicialització de la variable "fin" (boolean verdader/false) per
             * controlar la execució de l'aplipació de servidor, aquesta
             * cambiara d'estat a true o verdader quan rebi l'order "fin" de la
             * apliciació client
             */
            ServerSocket socket;
            boolean fin = false;

            //control d'excepcions
            try {
                /**
                 *** Creem un socket servidor i li assignemt l'adreça ***
                 * Instanciació del objecte socket de la classe ServerSocket
                 * (Socket servidor) en el contructor del objecte obre un socket
                 * o connexio en el port 6001 que l'identificara en la
                 * maquina/servidor (recordem que el ports de l'1 al 1023 estan
                 * reservats)
                 *
                 */
                socket = new ServerSocket(6001);
                /**
                 *** S'estableix una connexió passiva *** Espera una execució
                 * fins que arribi pel port 6001 per part de la aplicació client
                 * i l'accepta. -- Sols ho fan les conexions orientades a la
                 * connexió --
                 */
                Socket socket_cli = socket.accept();

                /**
                 * Instanciació del ojecte DataInputStream de la classe
                 * DataInputStrem el qual recull els flux de dades d'entrada del
                 * servidor per part del client. L’estil seqüència de bytes o
                 * orientat a connexió TCP.
                 */
                DataInputStream in =
                        new DataInputStream(socket_cli.getInputStream());

                DataOutputStream out =
                        new DataOutputStream(socket_cli.getOutputStream());


                /**
                 * La lectura es realitza amb un bucle llegint els missatges de
                 * la aplicació client. S'aniran mostrant per pantalla fins que
                 * reben la comanda del client BYE
                 */
                do {
                    /* Intercanvi de dades amb el client que ha sol·licitat la comunicació
                     * llegitn les dades i escrivint-les en el socket.
                     */
                    //iniciem el missatge eliminan el contingut anterior de la variable
                    String mensaje = "";
                    // missatge des del flux de dades d'entrada, codificació UTF
                    mensaje = in.readUTF();

                    //condicional que determina si el inici del missatge es BYE
                    if (mensaje.startsWith(ADEU)) {
                        /*per tancar la conexio sortim del bucle, escrivim en pantalla
                        * BYE i a més comunican al client la fi de la connexió
                        */
                        System.out.println(ADEU); //escrivim BYE en la consola
                        out.writeUTF(ADEU); // enviem BYE com a respota al client
                        statusSalutacio = false; //reiniciam l'estat de la salutació
                        socket.close(); //tanquem la connexio TCP amb el client
                        fin = true;
                        continue; //surtim directament del bucle
                    }

                    //lògica/proces de comandes y resposta a l'aplicació client                  
                    String resposta = analisis(mensaje);
                    
                    //mostrem per pantalla el missatge del servidor
                    //i enviem la respota a la aplicació client
                    System.out.println(resposta);
                    out.writeUTF(resposta);

                } while (!fin);
            } catch (Exception e) {
                //Qualsevol execepticó de la aplicació de servidor tancaria 
                //la connexió i mostraria per pantalla o consola el error o exepció
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } while (true);
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