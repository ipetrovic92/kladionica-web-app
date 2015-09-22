package mb;

import domain.Administrator;
import domain.Igrac;
import domain.Obrada;
import domain.Utakmica;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import sb.SBAdminLocal;
import sb.SBIgracLocal;
import sb.SBUtakmicaLocal;

/**
 *
 * @author Ivan Petrovic
 */
@ManagedBean
@SessionScoped
public class MbAdmin {

    Administrator admin;
    Utakmica novaUtakmica;
    Utakmica odabranaUtakmica;
    Collection<Utakmica> sveUtakmice;
    Collection<Igrac> sviIgraci;
    Igrac odabraniIgrac;

    @EJB
    SBAdminLocal sbAdmin;

    @EJB
    SBUtakmicaLocal sbUtakmica;

    @EJB
    SBIgracLocal sbIgrac;
    
    /**
     * Creates a new instance of MbAdmin
     */
    public MbAdmin() {
    }

    @PostConstruct
    public void init() {
        admin = new Administrator();
        novaUtakmica = new Utakmica();
        odabranaUtakmica = new Utakmica();
        sveUtakmice = sbUtakmica.vratiSveUtakmice();
        sviIgraci = sbIgrac.vratiSveIgrace();
        odabraniIgrac = new Igrac();
    }

    public Administrator getAdmin() {
        return admin;
    }

    public void setAdmin(Administrator admin) {
        this.admin = admin;
    }

    public Utakmica getNovaUtakmica() {
        return novaUtakmica;
    }

    public void setNovaUtakmica(Utakmica novaUtakmica) {
        this.novaUtakmica = novaUtakmica;
    }

    public Collection<Utakmica> getSveUtakmice() {
        return sveUtakmice;
    }

    public void setSveUtakmice(Collection<Utakmica> sveUtakmice) {
        this.sveUtakmice = sveUtakmice;
    }

    public Utakmica getOdabranaUtakmica() {
        return odabranaUtakmica;
    }

    public void setOdabranaUtakmica(Utakmica odabranaUtakmica) {
        this.odabranaUtakmica = odabranaUtakmica;
    }

    public Collection<Igrac> getSviIgraci() {
        return sviIgraci;
    }

    public void setSviIgraci(Collection<Igrac> sviIgraci) {
        this.sviIgraci = sviIgraci;
    }

    public Igrac getOdabraniIgrac() {
        return odabraniIgrac;
    }

    public void setOdabraniIgrac(Igrac odabraniIgrac) {
        this.odabraniIgrac = odabraniIgrac;
    }

    public String login() {
        try {
            Administrator a = sbAdmin.login(admin);
            if (a == null) {
                throw new Exception("Administrator nije ulogovan");
            }
            admin = a;
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("admin je ulogovan", "email: " + admin.getEmailAdministrator()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("Greska pri logovanju", ex.getMessage()));
            return null;
        }
        return "admin";
    }

    public String logout() {
        try {
            admin = new Administrator();
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("admin je izlogovan"));
        } catch (Exception ex) {
        }
        return "index";
    }

    public String sacuvajNovuUtakmicu() {
        novaUtakmica.setEmailAdministrator(admin);
        novaUtakmica.setSifraUtakmice(new Date().getTime() + "");
        novaUtakmica.setRezultat(" ");
        novaUtakmica.setObradaID(new Obrada(1));
        String poruka = sbUtakmica.zapamtiUtakmicu(novaUtakmica);
        FacesContext.getCurrentInstance().addMessage("poruke", new FacesMessage(poruka, ""));
        novaUtakmica = new Utakmica();
        return "admin";
    }

    public void utakmicaOdabrana(SelectEvent event) {
        odabranaUtakmica = (Utakmica) event.getObject();
    }
    
    public void igracOdabran(SelectEvent event) {
        odabraniIgrac = (Igrac) event.getObject();
    }
    
    public String sacuvajRezultatUtakmice(){
        String poruka = sbUtakmica.zapamtiRezultatUtakmice(odabranaUtakmica);
        FacesContext.getCurrentInstance().addMessage("poruke", new FacesMessage(poruka, ""));
        novaUtakmica = new Utakmica();
        return "admin";
    }
    
    public String sacuvajNovoStanjeRacuna(){
        String poruka = sbIgrac.zapamtiUplatuNovca(odabraniIgrac);
        FacesContext.getCurrentInstance().addMessage("poruke", new FacesMessage(poruka, ""));
        odabraniIgrac = new Igrac();
        return "admin";
    }

}
