
/**
 * Classe client java Protocol UDP NO orientat a la connexió
 *
 * Importem les llibreries necesaries per als diferents metodes que utilitzarem
 * per la programació de sockets UDP
 *
 * java.net 
 * Implementa tota la funcionalitat dels sockets. La libreria java.net
 * ens permet instanciar les classes DatagramSocket i DatagramPacket, la primera
 * per als objectes sockets UDP i la segona per als paquets que s'han d'enviar
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

public class clienteudp {

    /**
     * constants per tractar amb les comandes d'una forma escalabre, sempre
     * podrem cambiar les comandes sense modificar el codi
     */
    private static final String CAFE = "CO";
    private static final String TE = "TE";
    private static final String SUCRE = "SU";
    private static final String LLET = "MI";
    private static final String OK = "OK";
    private static final String SALUTACIO = "HI";
    private static final String ADEU = "BYE";

    /**
     * Metode static main, que tota aplicació java ha de tenir almenys en una
     * classe java, iniciara la aplicació java, permeten la entrada de valors,
     * en aquest cas esperar el nom o IP del servidor per realitza la connexió
     * del protocol IP, del contrari tancara la aplicació client mostran "java
     * clientetcp servidor"
     *
     * @param argv
     */
    public static void main(String argv[]) {
        if (argv.length == 0) {
            System.err.println("java clienteudp servidor");
            System.exit(1);
        }

        /**
         * Declarem/inicialitzem el flux d'entrada per convertir els bytes
         * rebuts en cadenes de texte - buffer de lectura
         *
         */
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        /**
         * * Creació un socket en l’espai de noms i amb el protocol volguts
         * (UPD i IP).**** Declaració de socket datagram (UDP) Declaració del
         * objecte address que emagatzema la dirreció IP que lligarem el socket
         * UDP
         */
        DatagramSocket socket;
        InetAddress address;

        //Array o agrupació de d'objectes byte per usarem per rebre/envia els datagrames
        byte[] mensaje_bytes = new byte[256];
        byte[] mensaje_bytes_entrada = new byte[256];

        //iniciem el missatges eliminan els continguts anterior de la variable
        String mensaje = "";
        String mensaje_entrada = "";

        //Declaració del objecte DatamgramPacket, en definitiva crear un paquet
        //com a datagrame UDP.
        DatagramPacket paquete;
        DatagramPacket paqueteEntrada;

        //Mitjançant la funció getBytes, convertirm la cadena de texte del missatge
        // amb una agrupació de bytes.
        mensaje_bytes = mensaje.getBytes();
        mensaje_bytes_entrada = mensaje_entrada.getBytes();
        
        //mapa de respostes
        Map<String, String> mapScreen = new HashMap<String, String>();
        
        //respostes del servidor per escriure per pantalla, menys BYE
        mapScreen.put(SALUTACIO, "HI");
        mapScreen.put(OK, "OK");
        mapScreen.put(CAFE, "coffe");
        mapScreen.put(TE, "tea");
        mapScreen.put(SUCRE, "sugar");
        mapScreen.put(LLET, "milk");
         

        //control de errors o exepcions
        try {

            /**
             * Assignar una adreça al socket amb le parametre inicial de la
             * IP del servidor creem un objecte InetAdres que conte aquesta
             * adreça IP, per posteriorment enllaçar-lo al socket
             */
            socket = new DatagramSocket();
            address = InetAddress.getByName(argv[0]);

            /**
             * BLUCE S'envien les captures de teclat i les envia al servidor Amb
             * l'ordre "fin" acabara el bucle surtin de la aplicació.
             */
            do {
                // llegeix del teclar
                mensaje = in.readLine();

                //converció del missatge de una cadena de texte a bytes per enviar
                //mitjançat un datagrama
                mensaje_bytes = mensaje.getBytes();

                /*
                 * -- Intercanvi de dades --
                 * Instanciació i creació del paquet datagrama amb el missatge amb bytes,
                 * el tamany d'aquest, la dirreció del servidor IP per al protocol IP i 
                 * el port que utilitzarem al protocol UDP
                 */
                paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 6000);

                // Enviament del paquet o datagrama UDP
                // Envia un paquet de datagrama des d'aquest connector.
                socket.send(paquete);

                //El mensaje recibido vendrá en bytes
                mensaje_bytes_entrada = new byte[256];
                //Esperamos a recibir un paquete
                paqueteEntrada = new DatagramPacket(mensaje_bytes_entrada, 256);
                socket.receive(paqueteEntrada);

                //mitjant la classe String convertim el missatge en cadena de text
                mensaje_entrada = new String(mensaje_bytes_entrada).trim();
                //Imprimir el paquet rebut
                if (mensaje_entrada.length() >= 2 && mapScreen.containsKey(mensaje_entrada.substring(0, 2))) {
                    System.out.println(mensaje_entrada);
                } else {
                    System.err.println(mensaje_entrada);
                }

                //Amb l'ordre "BYE" acabara el bucle surtin de la aplicació.
            } while (!mensaje_entrada.startsWith(ADEU));
        } 
        /*
         * El control d'errors o excepcions en cas de produir-se enviar l'error
         * per pantalla i tancara la aplicació.
         */ catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
