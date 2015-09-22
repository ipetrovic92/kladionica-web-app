package mb;

import domain.Igrac;
import domain.Status;
import domain.Tiket;
import domain.TiketPK;
import domain.Tiketutakmica;
import domain.TiketutakmicaPK;
import domain.Utakmica;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.SelectEvent;
import sb.SBIgracLocal;
import sb.SBTiketLocal;
import sb.SBUtakmicaLocal;

/**
 *
 * @author Ivan Petrovic
 */
@ManagedBean
@SessionScoped
public class MbIgrac {

    Igrac noviIgrac; //igrac koji se registruje
    Igrac igrac; //igrac koji je online ili koji pokusava da se uloguje
    Tiket odabraniTiket; //tiket cije se utakmice prikazuju
    Tiket noviTiket; //uplata novog tiketa
    Utakmica odabranaUtakmica; //utakmica koja se dodaje na novi tiket
    List<Utakmica> sveUtakmice; //kolekcija kojom se puni combo
    List<Tiketutakmica> utakmiceNaTiketu; //kolekcija utakmica na tiketu
    String tip; //tip nove utakmice
    String kvota; //kvota nove utakmice
    Tiketutakmica novaUtakmica;
    int uplata;
    double isplata;

    @EJB
    SBIgracLocal sbIgrac;

    @EJB
    SBUtakmicaLocal sbUtakmica;

    @EJB
    SBTiketLocal sbTiket;

    /**
     * Creates a new instance of MbIgrac
     */
    public MbIgrac() {
    }

    @PostConstruct
    public void init() {
        noviIgrac = new Igrac();
        igrac = new Igrac();
        odabraniTiket = new Tiket();
        noviTiket = new Tiket(new TiketPK(igrac.getEmailIgrac(), new Date().getTime() + ""));
        odabranaUtakmica = new Utakmica();
        sveUtakmice = (List<Utakmica>) sbUtakmica.vratiSveUtakmice();
        utakmiceNaTiketu = new ArrayList<>();
        novaUtakmica = new Tiketutakmica();
        tip = "";
    }

    public Igrac getNoviIgrac() {
        return noviIgrac;
    }

    public void setNoviIgrac(Igrac noviIgrac) {
        this.noviIgrac = noviIgrac;
    }

    public Igrac getIgrac() {
        return igrac;
    }

    public void setIgrac(Igrac igrac) {
        this.igrac = igrac;
    }

    public Tiket getOdabraniTiket() {
        return odabraniTiket;
    }

    public void setOdabraniTiket(Tiket odabraniTiket) {
        this.odabraniTiket = odabraniTiket;
    }

    public Utakmica getOdabranaUtakmica() {
        return odabranaUtakmica;
    }

    public void setOdabranaUtakmica(Utakmica odabranaUtakmica) {
        if (odabranaUtakmica == null) {
            return;
        }
        this.odabranaUtakmica = odabranaUtakmica;
        updateKvota();
    }

    public Tiket getNoviTiket() {
        return noviTiket;
    }

    public void setNoviTiket(Tiket noviTiket) {
        this.noviTiket = noviTiket;
    }

    public List<Utakmica> getSveUtakmice() {
        return sveUtakmice;
    }

    public void setSveUtakmice(List<Utakmica> sveUtakmice) {
        this.sveUtakmice = sveUtakmice;
    }

    public String getKvota() {
        updateKvota();
        return kvota;
    }

    public void updateKvota() {
        switch (tip) {
            case "0":
                kvota = odabranaUtakmica.getKvotaNereseno() + "";
            case "1":
                kvota = odabranaUtakmica.getKvotaDomacin() + "";
            case "2":
                kvota = odabranaUtakmica.getKvotaGost() + "";
        }
    }

    public String getTip() {
        return tip;
    }

    public void setKvota(String kvota) {
        this.kvota = kvota;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Tiketutakmica getNovaUtakmica() {
        return novaUtakmica;
    }

    public void setNovaUtakmica(Tiketutakmica novaUtakmica) {
        this.novaUtakmica = novaUtakmica;
    }

    public List<Tiketutakmica> getUtakmiceNaTiketu() {
        return utakmiceNaTiketu;
    }

    public void setUtakmiceNaTiketu(List<Tiketutakmica> utakmiceNaTiketu) {
        this.utakmiceNaTiketu = utakmiceNaTiketu;
    }

    public int getIsplata() {
        isplata = 0;
        for (Tiketutakmica ut : utakmiceNaTiketu) {
            isplata += ut.getKvota();
        }
        isplata *= uplata;
        return (int) isplata;
    }

    public int getUplata() {
        return uplata;
    }

    public void setIsplata(int isplata) {
        this.isplata = isplata;
    }

    public void setUplata(int uplata) {
        this.uplata = uplata;
    }

    public String registrujNovogIgraca() {
        String poruka = sbIgrac.registrujIgraca(noviIgrac);
        FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage(poruka, "email: " + noviIgrac.getEmailIgrac()));
        return "index";
    }

    public String login() {
        try {
            Igrac i = sbIgrac.login(igrac);
            if (i == null) {
                throw new Exception("igrac nije ulogovan");
            }
            igrac = i;
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("Igrac je ulogovan", "email: " + igrac.getEmailIgrac()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("Greska pri logovanju", ex.getMessage()));
            return null;
        }
        return "igrac";
    }

    public String logout() {
        try {
            igrac = new Igrac();
            FacesContext.getCurrentInstance().addMessage("successMsg", new FacesMessage("Igrac je izlogovan"));
        } catch (Exception ex) {
        }
        return "index";
    }

    public void tiketJeOdabran(SelectEvent event) {
        odabraniTiket = (Tiket) event.getObject();
    }

    public String sacuvajTiket() {
        noviTiket.setDatumIgranja(new Date());
        noviTiket.setIgrac(igrac);
        noviTiket.setMoguciDobitak(isplata);
        noviTiket.setTiketPK(new TiketPK(igrac.getEmailIgrac(), new Date().getTime() + ""));
        noviTiket.setTiketutakmicaCollection(utakmiceNaTiketu);
        double ukupnaKvota = 0;
        for (Tiketutakmica ut : utakmiceNaTiketu) {
            ut.setStatusID(new Status(3));
            ukupnaKvota += ut.getKvota();
        }
        noviTiket.setUkupnaKvota(ukupnaKvota);
        noviTiket.setUplata(uplata);
        noviTiket.setStatusID(new Status(3));
        String poruka = sbTiket.zapamtiTiket(noviTiket);
        if (!poruka.contains("nije")) {
            igrac.setStanjeRacuna(igrac.getStanjeRacuna() - uplata);
            sbIgrac.zapamtiUplatuNovca(igrac);
        }
        FacesContext.getCurrentInstance().addMessage("poruke", new FacesMessage(poruka));
        noviTiket = new Tiket();
        return "igrac";
    }

    public void novaUtakmicaZaTiket(ActionEvent event) {
        novaUtakmica.setUtakmica(odabranaUtakmica);
        novaUtakmica.setTip(Integer.parseInt(tip));
        novaUtakmica.setKvota(Double.parseDouble(getKvota()));
        novaUtakmica.setTiketutakmicaPK(new TiketutakmicaPK(igrac.getEmailIgrac(), noviTiket.getTiketPK().getSifraTiketa(), odabranaUtakmica.getSifraUtakmice()));
        utakmiceNaTiketu.add(novaUtakmica);
        odabranaUtakmica = new Utakmica();
        novaUtakmica = new Tiketutakmica();

        FacesContext.getCurrentInstance().addMessage("poruke", new FacesMessage("Dodata utakmica na tiket"));
    }

}
