package konverter;

import domain.Klub;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sb.SBKlubLocal;

/**
 *
 * @author Ivan Petrovic
 */
@FacesConverter(value = "klubCNV")
public class klubCNV implements Converter {

    @EJB
    SBKlubLocal sbKlub;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            Collection<Klub> kk = sbKlub.vratiSveKlubove();
            for (Klub k : kk) {
                if (k.getKlubID() == Integer.parseInt(value)) {
                    Klub nk = k;
                    System.out.println("klub konevrter: " + nk.getNazivKluba());
                    return nk;
                }
            }
        }
        return new Klub();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && (value instanceof Klub)) {
            Klub k = (Klub) value;
            return k.getKlubID() + "";
        }
        return "";
    }

}
