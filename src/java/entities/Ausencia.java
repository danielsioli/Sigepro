/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author danieloliveira
 */
@Entity
@Table(name = "ausencia")
@NamedQueries({@NamedQuery(name = "Ausencia.findByLogin", query = "SELECT a FROM Ausencia a WHERE a.login = :login"), @NamedQuery(name = "Ausencia.findByDelay", query = "SELECT a FROM Ausencia a WHERE a.delay = :delay"), @NamedQuery(name = "Ausencia.findByInicioAusencia", query = "SELECT a FROM Ausencia a WHERE a.inicioAusencia = :inicioAusencia"), @NamedQuery(name = "Ausencia.findAll", query = "SELECT a FROM Ausencia a"), @NamedQuery(name = "Ausencia.findByLoginOrderByInicioAusencia", query = "SELECT a FROM Ausencia a WHERE a.login = :login ORDER BY a.inicioAusencia")})
public class Ausencia implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private AusenciaPK ausenciaPK;
    @JoinColumn(name = "login", referencedColumnName = "login", columnDefinition = "Sevidor Ausente", insertable = false, updatable = false)
    @ManyToOne
    private Usuario login;
    @Column(name = "delay", nullable = true, columnDefinition = "Delay em dias antes da ausência")
    private Integer delay;
    @Column(name = "inicioAusencia", nullable = true, columnDefinition = "Dia de início da Ausência", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date inicioAusencia;
    @Column(name = "tempoAusente", nullable = true, columnDefinition = "Tempo ausente em dias")
    private Integer tempoAusente;

    public Ausencia(){
        
    }
    
    public Ausencia(AusenciaPK ausenciaPK) {
        this.ausenciaPK = ausenciaPK;
    }

    public Ausencia(AusenciaPK ausenciaPK, Usuario login, Date inicioAusencia, Integer tempoAusente) {
        this.ausenciaPK = ausenciaPK;
        this.login = login;
        this.inicioAusencia = inicioAusencia;
        this.tempoAusente = tempoAusente;
    }
    
    public AusenciaPK getAusenciaPK() {
        return ausenciaPK;
    }

    public void setAusenciaPK(AusenciaPK ausenciaPK) {
        this.ausenciaPK = ausenciaPK;
    }

    public Integer getTempoAusente() {
        return tempoAusente;
    }

    public void setTempoAusente(Integer tempoAusente) {
        this.tempoAusente = tempoAusente;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Date getInicioAusencia() {
        return inicioAusencia;
    }

    public void setInicioAusencia(Date inicioAusencia) {
        this.inicioAusencia = inicioAusencia;
    }

    public Date getFimAusencia(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inicioAusencia);
        calendar.roll(Calendar.DAY_OF_MONTH, tempoAusente);
        return calendar.getTime();
    }
    
    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario login) {
        this.login = login;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ausencia)) {
            return false;
        }
        Ausencia other = (Ausencia) object;
        if ((this.ausenciaPK == null && other.ausenciaPK != null) || (this.ausenciaPK != null && !this.ausenciaPK.equals(other.ausenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ausenciaPK;
    }
}
