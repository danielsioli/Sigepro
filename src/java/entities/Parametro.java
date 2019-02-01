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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "parametro")
@NamedQueries({@NamedQuery(name = "Parametro.findByParametroId", query = "SELECT p FROM Parametro p WHERE p.parametroId = :parametroId"), @NamedQuery(name = "Parametro.findByName", query = "SELECT p FROM Parametro p WHERE p.name = :name"), @NamedQuery(name = "Parametro.findByReferencia", query = "SELECT p FROM Parametro p WHERE p.referencia = :referencia")})
public class Parametro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "parametroId", nullable = false, columnDefinition = "Id do Parâmetro")
    private Integer parametroId;
    @Column(name = "name", nullable = false, length = 45, columnDefinition = "Nome")
    private String name;
    @Column(name = "referencia", nullable = false, length = 45, columnDefinition = "Referência")
    private String referencia;
    @ManyToMany(mappedBy = "parametroIdCollection")
    private Collection<Botao> botaoIdCollection;

    public Parametro() {
    }

    public Parametro(Integer id) {
        this.parametroId = id;
    }

    public Parametro(Integer id, String name, String referencia) {
        this.parametroId = id;
        this.name = name;
        this.referencia = referencia;
    }

    public Integer getParametroId() {
        return parametroId;
    }

    public void setParametroId(Integer id) {
        this.parametroId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Collection<Botao> getBotaoIdCollection() {
        return botaoIdCollection;
    }

    public void setBotaoIdCollection(Collection<Botao> botaoIdCollection) {
        this.botaoIdCollection = botaoIdCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parametroId != null ? parametroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametro)) {
            return false;
        }
        Parametro other = (Parametro) object;
        if ((this.parametroId == null && other.parametroId != null) || (this.parametroId != null && !this.parametroId.equals(other.parametroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Parametro[id=" + parametroId + "]";
    }

}
