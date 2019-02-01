/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Daniel
 */
@Embeddable
public class ProcuracaoPK implements Serializable {
    @Column(name = "empresaCnpj", nullable = false)
    private String empresaCnpj;
    @Column(name = "procuradorCpf", nullable = false)
    private String procuradorCpf;

    public ProcuracaoPK() {
    }

    public ProcuracaoPK(String empresaCnpj, String procuradorCpf) {
        this.empresaCnpj = empresaCnpj;
        this.procuradorCpf = procuradorCpf;
    }

    public String getEmpresaCnpj() {
        return empresaCnpj;
    }

    public void setEmpresaCnpj(String empresaCnpj) {
        this.empresaCnpj = empresaCnpj;
    }

    public String getProcuradorCpf() {
        return procuradorCpf;
    }

    public void setProcuradorCpf(String procuradorCpf) {
        this.procuradorCpf = procuradorCpf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empresaCnpj != null ? empresaCnpj.hashCode() : 0);
        hash += (procuradorCpf != null ? procuradorCpf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcuracaoPK)) {
            return false;
        }
        ProcuracaoPK other = (ProcuracaoPK) object;
        if ((this.empresaCnpj == null && other.empresaCnpj != null) || (this.empresaCnpj != null && !this.empresaCnpj.equals(other.empresaCnpj))) {
            return false;
        }
        if ((this.procuradorCpf == null && other.procuradorCpf != null) || (this.procuradorCpf != null && !this.procuradorCpf.equals(other.procuradorCpf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProcuracaoPK[empresaCnpj=" + empresaCnpj + ", procuradorCpf=" + procuradorCpf + "]";
    }

}
