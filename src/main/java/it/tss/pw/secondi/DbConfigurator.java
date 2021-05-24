/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi;

import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

/**
 *
 * @author 588se
 */
@DataSourceDefinition(
        className = DbConfigurator.MARIADB_CLASS_NAME,
        name = DbConfigurator.DS_JNDI_NAME,
        serverName = DbConfigurator.MARIADB_HOST,
        portNumber = DbConfigurator.MARIADB_PORT,
        user = DbConfigurator.MARIADB_USR,
        password = DbConfigurator.MARIADB_USER_PWD,
        databaseName = DbConfigurator.MARIADB_DATABASE_NAME,
        properties = {"validate-on-match=true", "background-validation=false"} // come si usa ?? ----------------------------------------------------
)
@Singleton()
@Startup
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DbConfigurator {

    public static final String MARIADB_HOST = "localhost";
    public static final int MARIADB_PORT = 3306;
    public static final String MARIADB_PROTOCOL = "tcp";
    public static final String MARIADB_USR = "588se";
    public static final String MARIADB_USER_PWD = "588se";
    public static final String MARIADB_DATABASE_NAME = "projectwork";
    public static final String MARIADB_CLASS_NAME = "org.mariadb.jdbc.MariaDbDataSource";
    public static final String DS_JNDI_NAME = "java:global/jdbc/pw-secondi";

    @Resource(lookup = DS_JNDI_NAME)
    private DataSource pw; 

    private String jdbcBaseUrl;

    @PostConstruct
    public void init() { 
        System.out.println("----------------------- Init DbConfiguration-----------------------");
        jdbcBaseUrl = "jdbc:mariadb://" + MARIADB_HOST + ":" + MARIADB_PORT + "/";
        checkDasource();
        migrate();
        System.out.println("----------------------- End DbConfiguration-----------------------");
    }

    private void checkDasource() {
        try (Connection connection = pw.getConnection()) {
            System.out.println(
                    connection.getMetaData().getDatabaseProductName() + "-"
                    + connection.getCatalog()
            );
            System.out.println("---------------- check datasource ok -------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean migrate() {
        Flyway flyway = Flyway
                .configure()
                .dataSource(jdbcBaseUrl + MARIADB_DATABASE_NAME, MARIADB_USR, MARIADB_USER_PWD)
                .baselineOnMigrate(true)
                .load();
        int result = flyway.migrate();
        System.out.println("-------------------------result migrate------------- " + result);
        return result > 0;
    }

}

/*
JAVAX.ANNOTATION.SQL
@DataSourceDefinition
Annotazione utilizzata per definire un DataSource contenitore da registrare con JNDI.
(In informatica Java Naming and Directory Interface (JNDI) è una API Java per servizi di directory che ricopre un ruolo molto importante all'interno di un application server. 
Permette ai client java di scoprire e ottenere dati e oggetti attraverso un nome. Tipici usi includono:
    connettere un'applicazione Java a un servizio di directory esterno (come un indirizzo di una base di dati o di un server LDAP).
    permettere a un Java Servlet di ottenere informazioni di configurazione fornite dal web container.)
DataSource può essere configurato impostando gli elementi di annotazione per le proprietà DataSource comunemente utilizzate. 
Ulteriori proprietà standard e specifiche del fornitore possono essere specificate usando l'elemento properties.
L'origine dati verrà registrata con il nome specificato nell'elemento name. 
Può essere definito in qualsiasi spazio dei nomi EE Jakarta valido, che determinerà l'accessibilità dell'origine dati da altri componenti.
Una classe di implementazione del driver JDBC del tipo appropriato, DataSource, ConnectionPoolDataSource o XADataSource, deve essere indicata dall'elemento className.
La disponibilità della classe di driver verrà assunta in fase di esecuzione.
 */
 /*
JAVAX.ANNOTATION.SQL
@Resource(lookup = DS_JNDI_NAME)
 */
 /*
JAVAX.EJB
@Singleton
All'avvio dell app si crea e rimane una sola instanza della classe 
https://docs.oracle.com/javaee/6/tutorial/doc/gipvi.html
 */
 /*
JAVAX.EJB
@Startup
Contrassegnare un bean singleton per l'inizializzazione desiderosa durante la sequenza di avvio dell'applicazione.
https://docs.oracle.com/javaee/6/tutorial/doc/gipvi.html
 */
 /*
JAVAX.EJB
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
Una transazione è una singola unità di elementi di lavoro, che segue le proprietà ACID. ACID è l'acronimo di Atomic, Coerent, Isolated e Durable.
Atomico: se uno degli oggetti di lavoro fallisce, l'intera unità verrà considerata guasta. Significa successo, tutti gli elementi vengono eseguiti correttamente.
Coerente: una transazione deve mantenere il sistema in stato coerente.
Isolato: ogni transazione viene eseguita indipendentemente da qualsiasi altra transazione.
Durevole: la transazione dovrebbe sopravvivere a un errore del sistema se è stata eseguita o impegnata.
I contenitori / server EJB sono server di transazioni e gestiscono la propagazione del contesto delle transazioni e le transazioni distribuite. Le transazioni possono essere gestite dal contenitore o mediante la gestione personalizzata del codice nel codice del bean.
Transazioni gestite dal contenitore: in questo tipo, il contenitore gestisce gli stati delle transazioni.
Transazioni gestite da bean: in questo tipo, lo sviluppatore gestisce il ciclo di vita degli stati delle transazioni.
https://www.tutorialspoint.com/ejb/ejb_transactions.htm
 */
 /*
ORG.FLYWAYDB.CORE
Flyway
 */
