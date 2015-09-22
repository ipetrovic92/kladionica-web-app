package konverter;

import domain.Utakmica;
import java.util.Collection;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sb.SBUtakmicaLocal;

/**
 *
 * @author Ivan Petrovic
 */
@FacesConverter(value = "utakmicaCNV")
public class utakmicaCNV implements Converter {

    @EJB
    SBUtakmicaLocal sbUtakmica;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isEmpty()) {
            Collection<Utakmica> ku = sbUtakmica.vratiSveUtakmice();
            for (Utakmica ut : ku) {
                if (ut.getSifraUtakmice().equals(value)) {
                    Utakmica nu = ut;
                    return nu;
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null && (value instanceof Utakmica)) {
            Utakmica u = (Utakmica) value;
            return u.getSifraUtakmice() + "";
        }
        return "";
    }

}
