/**
 * Classe client java TCP
 * Protocol TCP orientat a la connexió
 * 
 * Importem les llibreries necesaries per als diferents metodes que utilitzarem
 * per la programació de sockets
 * 
 * java.net
 * Implementa tota la funcionalitat dels sockets.
 * La libreria java.net ens permet instanciar les classes Socket per la comunicació
 * a més de InetAddress que s'utilitza en la aplicació de Client
 * 
 * java.io
 * Pel que fa java.io la seva importació ens pemet la utilització de fluxes
 * d'entrada i sortida del sistema, aixi com al serialització i el sistema de
 * arxius.
 * 
 */
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class clientetcp {
    /** constants per tractar amb les comandes
    * d'una forma escalabre, sempre podrem cambiar les comandes
    * sense modificar el codi
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
     * del protocol IP, del contrari tancara la aplicació client mostran 
     * "java clientetcp servidor"
     * 
     * @param argv 
     */
   public static void main(String argv[]) {
      if (argv.length == 0) {
         System.err.println("java clientetcp servidor");
         System.exit(1);
      }
      
     /**
      * Instanciació del objecte BufferedReader de la classe BufferedReader
      * el qual inicialitza el flux de dades amb Bytes amb cadenes de caracters
      * del client.  
      */
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

      /*** Crear un socket en l’espai de noms i amb el protocol volguts (TCP i IP).****
       * Declaració del objecte socket per realitzar la connexio i enviar dades
       * a més del objecte address que emagatzema la dirreció IP que lligarem 
       * el socket
       */
      Socket socket;
      boolean fin = false;
      InetAddress address;
      
      /*
       * Declarem e instanciem un array d'objectes byte tot i que no l'utilitzarem
       * en el protocol TCP.
       * 
       * Despres inicialitzem la variable "mensage" com cadena de text buida (String)
       */
      byte[] mensaje_bytes = new byte[256];
      String mensaje="";

      /*
       * Gestió d'erros o excepcions
       */
      try {
          /** Assignar una adreça al socket **
           * Amb le parametre inicial de la IP del servidor creem un objecte InetAdres
           * que conte aquesta adreça IP, per posteriorment lligarlo al socket
           */
         address=InetAddress.getByName(argv[0]);

          /**
           *** Creem un socket del client i li assignem l'adreça ***
           * Instanciació del objecte socket en el contructor del objecte  
           * obre un socket o connexio en el port 6001 a més assignació  o enllaç de
           * la IP de la maquina servidor per al transport.
           * 
           */
         socket = new Socket(address,6001);

         /*** Enviar una petició de connexió al servidor **
          * Asociació del flux de dades surtin (enviament)
          * connexió activa
          */
         DataOutputStream out =
            new DataOutputStream(socket.getOutputStream());
         
          /*** Espera de la respota del servidor **
          * Asociació del flux de dades entrant 
          */
         DataInputStream inServer =
            new DataInputStream(socket.getInputStream());
         
        //mapa de respostes
        Map<String, String> mapScreen = new HashMap<String, String>();
        
        //respostes del servidor per escriure per pantalla, menys BYE
        mapScreen.put(SALUTACIO, "HI");
        mapScreen.put(OK, "OK");
        mapScreen.put(CAFE, "coffe");
        mapScreen.put(TE, "tea");
        mapScreen.put(SUCRE, "sugar");
        mapScreen.put(LLET, "milk");
         
         /**
          * BLUCE - Intercanvi de dades --
          * S'envien les captures de teclat, escribint al socket (out) 
          * que enviara cap al servidor.
          * Amb l'ordre "fin" acabara el bucle surtin de la aplicació.
          */
         do {
            mensaje = in.readLine();
            out.writeUTF(mensaje);   
            
            String mensaje_server = null; 
            mensaje_server = inServer.readUTF();
 
            //lògica de comandes/ordres que s'han de tenir en compte,
            // per la seva escritura per pantalla o per surtir de l'aplicació
            if ( mensaje_server.toString().length() >= 2 && mapScreen.containsKey(mensaje_server.substring(0,2))){
                System.out.println(mensaje_server);
            } else {
                if (mensaje_server.startsWith(ADEU)) {
                    fin = true;
                    socket.close();
                } else {
                    System.err.println(mensaje_server); 
                }  
            }

         } while (!fin);
      }
      /*
       * El control d'errors o excepcions en cas de produir-se enviar l'error
       * per pantalla i tancara la aplicació.
       */
      catch (Exception e) {
         System.err.println(e.getMessage());
         System.exit(1);
      }
   }
}
