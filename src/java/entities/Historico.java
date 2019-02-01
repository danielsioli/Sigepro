/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "historico")
@NamedQueries({@NamedQuery(name = "Historico.findByHistoricoId", query = "SELECT h FROM Historico h WHERE h.historicoId = :historicoId"), @NamedQuery(name = "Historico.findByHora", query = "SELECT h FROM Historico h WHERE h.hora = :hora"), @NamedQuery(name = "Historico.findByTabelaModificada", query = "SELECT h FROM Historico h WHERE h.tabelaModificada = :tabelaModificada"), @NamedQuery(name = "Historico.findByChaveModificada", query = "SELECT h FROM Historico h WHERE h.chaveModificada = :chaveModificada"), @NamedQuery(name = "Historico.findByAcao", query = "SELECT h FROM Historico h WHERE h.acao = :acao"), @NamedQuery(name = "Historico.findAll", query = "SELECT h FROM Historico h"), @NamedQuery(name = "Historico.findByLike", query = "SELECT h FROM Historico h WHERE h.historicoId LIKE :historicoId")})
public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "historicoId", nullable = false, columnDefinition = "Id do Histórico")
    private Integer historicoId;
    @Column(name = "hora", nullable = false, columnDefinition = "Hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hora;
    @Column(name = "tabelaModificada", nullable = false, columnDefinition = "Tabela Modificada")
    private String tabelaModificada;
    @Column(name = "chaveModificada", nullable = false, columnDefinition = "Chave Modificada")
    private String chaveModificada;
    @Column(name = "acao", nullable = false, columnDefinition = "Ação")
    private String acao;
    @JoinColumn(name = "login", referencedColumnName = "login", columnDefinition = "Usuário")
    @ManyToOne
    private Usuario login;

    public Historico() {
    }

    public Historico(Integer id) {
        this.historicoId = id;
    }

    public Historico(Integer id, Date hora, String tabelaModificada, String chaveModificada, String acao) {
        this.historicoId = id;
        this.hora = hora;
        this.tabelaModificada = tabelaModificada;
        this.chaveModificada = chaveModificada;
        this.acao = acao;
    }

    public Integer getHistoricoId() {
        return historicoId;
    }

    public void setHistoricoId(Integer id) {
        this.historicoId = id;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getTabelaModificada() {
        return tabelaModificada;
    }

    public void setTabelaModificada(String tabelaModificada) {
        this.tabelaModificada = tabelaModificada;
    }

    public String getChaveModificada() {
        return chaveModificada;
    }

    public void setChaveModificada(String chaveModificada) {
        this.chaveModificada = chaveModificada;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario usuario) {
        this.login = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historicoId != null ? historicoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historico)) {
            return false;
        }
        Historico other = (Historico) object;
        if ((this.historicoId == null && other.historicoId != null) || (this.historicoId != null && !this.historicoId.equals(other.historicoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Historico[id=" + historicoId + "]";
    }

}
