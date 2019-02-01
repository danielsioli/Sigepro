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
@Table(name = "servico")
@NamedQueries({@NamedQuery(name = "Servico.findByServicoNum", query = "SELECT s FROM Servico s WHERE s.servicoNum = :servicoNum"), @NamedQuery(name = "Servico.findByNome", query = "SELECT s FROM Servico s WHERE s.nome = :nome"), @NamedQuery(name = "Servico.findAll", query = "SELECT s FROM Servico s"), @NamedQuery(name = "Servico.findByLike", query = "SELECT s FROM Servico s WHERE s.servicoNum LIKE :servicoNum")})
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "servicoNum", nullable = false, length = 3, columnDefinition = "Serviço")
    private String servicoNum;
    @Column(name = "nome", nullable = false, length = 70, columnDefinition = "Nome")
    private String nome;
    @JoinColumn(name = "login", referencedColumnName = "login", nullable = true, columnDefinition = "Responsável")
    @ManyToOne
    private Usuario login;
    @OneToMany(mappedBy = "servicoNum")
    private Collection<Processo> processoCollection;

    public Servico() {
    }

    public Servico(String num) {
        this.servicoNum = num;
    }

    public Servico(String num, String nome) {
        this.servicoNum = num;
        this.nome = nome;
    }

    public String getServicoNum() {
        return servicoNum;
    }

    public void setServicoNum(String num) {
        this.servicoNum = num;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario login) {
        this.login = login;
    }

    public Collection<Processo> getProcessoCollection() {
        return processoCollection;
    }

    public void setProcessoCollection(Collection<Processo> processoCollection) {
        this.processoCollection = processoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicoNum != null ? servicoNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servico)) {
            return false;
        }
        Servico other = (Servico) object;
        if ((this.servicoNum == null && other.servicoNum != null) || (this.servicoNum != null && !this.servicoNum.equals(other.servicoNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return servicoNum;
    }
}
