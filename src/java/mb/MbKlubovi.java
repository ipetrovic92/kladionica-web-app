package mb;

import domain.Klub;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import sb.SBKlubLocal;

/**
 *
 * @author Ivan Petrovic
 */
@ManagedBean
@RequestScoped
public class MbKlubovi {

    Collection<Klub> kolekcijaKlubova;
    Klub odabraniKlub;

    @EJB
    SBKlubLocal sbKlub;

    /**
     * Creates a new instance of MbKlubovi
     */
    public MbKlubovi() {
    }

    @PostConstruct
    public void init() {
        kolekcijaKlubova = sbKlub.vratiSveKlubove();
        odabraniKlub = new Klub();
    }

    public Collection<Klub> getKolekcijaKlubova() {
        return kolekcijaKlubova;
    }

    public void setKolekcijaKlubova(Collection<Klub> kolekcijaKlubova) {
        this.kolekcijaKlubova = kolekcijaKlubova;
    }

    public Klub getOdabraniKlub() {
        return odabraniKlub;
    }

    public void setOdabraniKlub(Klub odabraniKlub) {
        this.odabraniKlub = odabraniKlub;
    }
    
}
