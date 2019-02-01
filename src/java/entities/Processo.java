/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "processo")
@NamedQueries({@NamedQuery(name = "Processo.findBySicap", query = "SELECT p FROM Processo p WHERE p.sicap = :sicap"), @NamedQuery(name = "Processo.findByFistel", query = "SELECT p FROM Processo p WHERE p.fistel = :fistel"), @NamedQuery(name = "Processo.findBySitar", query = "SELECT p FROM Processo p WHERE p.sitar = :sitar"), @NamedQuery(name = "Processo.findAll", query = "SELECT p FROM Processo p"), @NamedQuery(name = "Processo.findByLike", query = "SELECT p FROM Processo p WHERE p.sicap LIKE :sicap")})
public class Processo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "sicap", nullable = false, length = 17, columnDefinition = "Número do Processo")
    private String sicap;
    @Column(name = "fistel", length = 11, columnDefinition = "Fistel")
    private String fistel;
    @Column(name = "sitar", length = 8, columnDefinition = "Número da Entidade")
    private String sitar;
    @JoinColumn(name = "empresaCnpj", referencedColumnName = "empresaCnpj", columnDefinition = "CNPJ / CPF")
    @ManyToOne
    private Empresa empresaCnpj;
    @JoinColumn(name = "servicoNum", referencedColumnName = "servicoNum", columnDefinition = "Serviço")
    @ManyToOne
    private Servico servicoNum;
    @Column(name = "peso", nullable = false, columnDefinition = "Máximo de Solicitações Seguidas")
    private int peso;
    @Column(name = "solicitacoesSeguidas", nullable = false, columnDefinition = "Solicitações Seguidas")
    private int solicitacoesSeguidas;
    @OneToMany(mappedBy = "processo")
    private Collection<Distribuicao> distribuicoes;
    @Column(name = "idtContrato", nullable = true, length = 10, columnDefinition = "Flag do BDTA")
    private String idtContrato;
    @Column(name = "id", nullable = false, length = 10, columnDefinition = "Flag do BDTA")
    private String id;

    public Processo() {
    }

    public Processo(String sicap) {
        this.sicap = sicap;
    }

    public String getIdtContrato() {
        return idtContrato;
    }

    public void setIdtContrato(String idtContrato) {
        this.idtContrato = idtContrato;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSicap() {
        return sicap;
    }

    public void setSicap(String sicap) {
        this.sicap = sicap;
    }

    public String getFistel() {
        return fistel;
    }

    public void setFistel(String fistel) {
        this.fistel = fistel;
    }

    public String getSitar() {
        return sitar;
    }

    public void setSitar(String sitar) {
        this.sitar = sitar;
    }

    public Empresa getEmpresaCnpj() {
        return empresaCnpj;
    }

    public void setEmpresaCnpj(Empresa empresaCnpj) {
        this.empresaCnpj = empresaCnpj;
    }

    public Servico getServicoNum() {
        return servicoNum;
    }

    public void setServicoNum(Servico servicoNum) {
        this.servicoNum = servicoNum;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso.intValue();
    }

    public Collection<Distribuicao> getDistribuicoes() {
        return distribuicoes;
    }

    public void setDistribuicoes(Collection<Distribuicao> distribuicoes) {
        this.distribuicoes = distribuicoes;
    }

    public int getSolicitacoesSeguidas() {
        return solicitacoesSeguidas;
    }

    public void setSolicitacoesSeguidas(Integer solicitacoesSeguidas) {
        this.solicitacoesSeguidas = solicitacoesSeguidas.intValue();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sicap != null ? sicap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Processo)) {
            return false;
        }
        Processo other = (Processo) object;
        if ((this.sicap == null && other.sicap != null) || (this.sicap != null && !this.sicap.equals(other.sicap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Processo[sicap=" + sicap + "]";
    }

}
