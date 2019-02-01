/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author danieloliveira
 */
@Embeddable
public class AusenciaPK implements java.io.Serializable {
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "inicioAusencia", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioAusencia;

    public Date getInicioAusencia() {
        return inicioAusencia;
    }

    public void setInicioAusencia(Date inicioAusencia) {
        this.inicioAusencia = inicioAusencia;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
