/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author danieloliveira
 */
@Embeddable
public class EstatisticaPK implements java.io.Serializable {
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "tipoDocumento", nullable = false)
    private String tipoDocumento;
    @Column(name = "modelo", nullable = false)
    private String modelo;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
}
