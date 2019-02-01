/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author danieloliveira
 */
@Entity
@Table(name = "estatistica")
@NamedQueries({@NamedQuery(name = "Estatistica.findByEstatisticaPK", query = "SELECT e FROM Estatistica e WHERE e.estatisticaPK = :estatisticaPK"), @NamedQuery(name = "Estatistica.findByTipoDocumento", query = "SELECT e FROM Estatistica e WHERE e.tipoDocumento = :tipoDocumento"), @NamedQuery(name = "Estatistica.findByModelo", query = "SELECT e FROM Estatistica e WHERE e.modelo = :modelo"), @NamedQuery(name = "Estatistica.findByLogin", query = "SELECT e FROM Estatistica e WHERE e.login = :login"), @NamedQuery(name = "Estatistica.findByFrequenciaUso", query = "SELECT e FROM Estatistica e WHERE e.frequenciaUso = :frequenciaUso")})
public class Estatistica implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private EstatisticaPK estatisticaPK;
    @Column(name = "tipoDocumento", nullable = false, insertable = false, updatable = false)
    private String tipoDocumento;
    @Column(name = "modelo", nullable = false, insertable = false, updatable = false)
    private String modelo;
    @JoinColumn(name = "login", referencedColumnName = "login", columnDefinition = "Sevidor Ausente", insertable = false, updatable = false)
    @ManyToOne
    private Usuario login;
    @Column(name = "frequenciaUso", nullable = false)
    private double frequenciaUso;

    public Estatistica() {
    }

    public Estatistica(EstatisticaPK estatisticaPK) {
        this.estatisticaPK = estatisticaPK;
    }
    
    public Estatistica(EstatisticaPK estatisticaPK, String tipoDocumento, String modelo, Usuario login, double frequenciaUso) {
        this.estatisticaPK = estatisticaPK;
        this.tipoDocumento = tipoDocumento;
        this.modelo = modelo;
        this.login = login;
        this.frequenciaUso = frequenciaUso;
    }

    public EstatisticaPK getEstatisticaPK() {
        return estatisticaPK;
    }

    public void setEstatisticaPK(EstatisticaPK estatisticaPK) {
        this.estatisticaPK = estatisticaPK;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario login) {
        this.login = login;
    }

    public double getFrequenciaUso() {
        return frequenciaUso;
    }

    public void setFrequenciaUso(double frequenciaUso) {
        this.frequenciaUso = frequenciaUso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estatisticaPK != null ? estatisticaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estatistica)) {
            return false;
        }
        Estatistica other = (Estatistica) object;
        if ((this.estatisticaPK == null && other.estatisticaPK != null) || (this.estatisticaPK != null && !this.estatisticaPK.equals(other.estatisticaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Estatistica[estatisticaPK=" + estatisticaPK + "]";
    }

}
