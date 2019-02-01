/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
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
 * @author Daniel
 */
@Entity
@Table(name = "procuracao")
@NamedQueries({@NamedQuery(name = "Procuracao.findByEmpresaCnpj", query = "SELECT p FROM Procuracao p WHERE p.procuracaoPK.empresaCnpj = :empresaCnpj"), @NamedQuery(name = "Procuracao.findByProcuradorCpf", query = "SELECT p FROM Procuracao p WHERE p.procuracaoPK.procuradorCpf = :procuradorCpf"), @NamedQuery(name = "Procuracao.findByValidade", query = "SELECT p FROM Procuracao p WHERE p.validade = :validade"), @NamedQuery(name = "Procuracao.findByLikeEmpresa", query = "SELECT p FROM Procuracao p WHERE p.procuracaoPK.empresaCnpj LIKE :empresaCnpj AND p.procuracaoPK.procuradorCpf = :procuradorCpf"), @NamedQuery(name = "Procuracao.findByLikeProcurador", query = "SELECT p FROM Procuracao p WHERE p.procuracaoPK.procuradorCpf LIKE :procuradorCpf AND p.procuracaoPK.empresaCnpj = :empresaCnpj")})
public class Procuracao implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProcuracaoPK procuracaoPK;
    @Column(name = "validade", columnDefinition = "Validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validade;
    @JoinColumn(name = "empresaCnpj", nullable=false, referencedColumnName = "empresaCnpj", columnDefinition = "CNPJ / CPF", insertable = false, updatable = false)
    @ManyToOne
    private Empresa empresaCnpj;
    @JoinColumn(name = "procuradorCpf", nullable=false, referencedColumnName = "procuradorCpf", columnDefinition = "CPF do Procurador", insertable = false, updatable = false)
    @ManyToOne
    private Procurador procuradorCpf;

    public Procuracao() {
    }

    public Procuracao(ProcuracaoPK procuracaoPK) {
        this.procuracaoPK = procuracaoPK;
    }

    public Procuracao(String empresaCnpj, String procuradorCpf) {
        this.procuracaoPK = new ProcuracaoPK(empresaCnpj, procuradorCpf);
    }

    public ProcuracaoPK getProcuracaoPK() {
        return procuracaoPK;
    }

    public void setProcuracaoPK(ProcuracaoPK procuracaoPK) {
        this.procuracaoPK = procuracaoPK;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public Empresa getEmpresaCnpj() {
        return empresaCnpj;
    }

    public void setEmpresaCnpj(Empresa empresaCnpj) {
        this.empresaCnpj = empresaCnpj;
        if(procuracaoPK == null){
            procuracaoPK = new ProcuracaoPK();
        }
        this.procuracaoPK.setEmpresaCnpj(empresaCnpj.getEmpresaCnpj());
    }

    public Procurador getProcuradorCpf() {
        return procuradorCpf;
    }

    public void setProcuradorCpf(Procurador procuradorCpf) {
        this.procuradorCpf = procuradorCpf;
        if(procuracaoPK == null){
            procuracaoPK = new ProcuracaoPK();
        }
        this.procuracaoPK.setProcuradorCpf(procuradorCpf.getProcuradorCpf());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procuracaoPK != null ? procuracaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Procuracao)) {
            return false;
        }
        Procuracao other = (Procuracao) object;
        if ((this.procuracaoPK == null && other.procuracaoPK != null) || (this.procuracaoPK != null && !this.procuracaoPK.equals(other.procuracaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Procuracao[procuracaoPK=" + procuracaoPK + "]";
    }

}
