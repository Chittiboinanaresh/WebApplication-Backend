/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 *
 * @author 588se
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "created_on")
    protected LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "modified_on")
    protected LocalDateTime modifiedOn;

    @Column(name = "created_by")
    protected String createBy;

    @Column(name = "modified_by")
    protected String modifiedBy;

    @Column(name = "version")
    @Version  // chiarimenti -------------------------------------------------------------------------------------------------------
    protected long version;

    @Column(name = "deleted")
    protected boolean deleted = false;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEntity other = (AbstractEntity) obj;
        return Objects.equals(this.id, other.id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
/*
JPA
@MappedSuperclass
Indica una classe le cui informazioni di mappatura vengono applicate alle entità che ereditano da essa.
Una superclasse mappata non ha una tabella separata definita per essa.
Una classe designata con l'annotazione MappedSuperclass può essere mappata allo stesso modo di un'entità, 
tranne per il fatto che i mapping verranno applicati solo alle sue sottoclassi poiché non esiste alcuna tabella per la superclasse mappata stessa.
Quando applicati alle sottoclassi, i mapping ereditati verranno applicati nel contesto delle tabelle delle sottoclassi. 
Le informazioni sulla mappatura possono essere sovrascritte in tali sottoclassi utilizzando le annotazioni AttributeOverride e AssociationOverride o gli elementi XML corrispondenti.
 */

 /*
JPA
@Version
JPA utilizza un campo versione nelle entità per rilevare modifiche simultanee allo stesso record del datastore.
Quando il runtime JPA rileva un tentativo di modificare contemporaneamente lo stesso record,
genera un'eccezione alla transazione che tenta di eseguire il commit per ultimo.
È necessario considerare immutabili i campi della versione. La modifica del valore del campo ha risultati indefiniti.
Tutto ciò che fa è controllare / aggiornare la versione in ogni query di aggiornamento: 
UPDATE myentity SET mycolumn = 'new value', version = version + 1 WHERE version = [old.version].
Se qualcuno aggiorna il record, old.version non corrisponderà più a quello nel DB e la clausola where impedirà l'aggiornamento.
Le 'righe aggiornate' saranno 0, che JPA può rilevare per concludere che si è verificata una modifica simultanea.
 */

/*
JPA
@GenerateValue
Crea un id automaticamente
https://www.objectdb.com/java/jpa/entity/generated
*/

 /*
JAVA.IO
Serializable
Java fornisce un meccanismo, chiamato serializzazione di oggetti in cui un oggetto può essere rappresentato come una sequenza di byte che include i dati dell'oggetto,
nonché informazioni sul tipo di oggetto e sui tipi di dati memorizzati nell'oggetto.
Dopo che un oggetto serializzato è stato scritto in un file, può essere letto dal file e deserializzato,
ovvero le informazioni sul tipo e i byte che rappresentano l'oggetto e i suoi dati possono essere utilizzati per ricreare l'oggetto in memoria.
Il più impressionante è che l'intero processo è indipendente dalla JVM,
il che significa che un oggetto può essere serializzato su una piattaforma e deserializzato su una piattaforma completamente diversa.
Classi ObjectInputStream e ObjectOutputStream sono flussi di alto livello che contengono i metodi per serializzare e deserializzare un oggetto.
https://www.tutorialspoint.com/java/java_serialization.htm
 */
