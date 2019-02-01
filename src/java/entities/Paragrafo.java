/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "paragrafo")
@NamedQueries({@NamedQuery(name = "Paragrafo.findByParagrafoId", query = "SELECT p FROM Paragrafo p WHERE p.paragrafoId = :paragrafoId"), @NamedQuery(name = "Paragrafo.findAll", query = "SELECT p FROM Paragrafo p"), @NamedQuery(name = "Paragrafo.findByLike", query = "SELECT p FROM Paragrafo p WHERE p.paragrafoId LIKE :paragrafoId"), @NamedQuery(name = "Paragrafo.findByOficioId", query = "SELECT p FROM Paragrafo p WHERE p.oficioId= :oficioId ORDER BY p.ordem"), @NamedQuery(name = "Paragrafo.findByOficioIdAndParagrafoPrincipalId", query = "SELECT p FROM Paragrafo p WHERE p.oficioId= :oficioId AND p.paragrafoPrincipal= :paragrafoPrincipal ORDER BY p.ordem")})
public class Paragrafo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "paragrafoId", nullable = false, columnDefinition = "Id do Parágrafo")
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer paragrafoId;
    @Lob
    @Column(name = "corpo", nullable = false, columnDefinition = "Corpo")
    private String corpo;
    @Column (name = "ordem", nullable = false, columnDefinition = "Ordem no Ofício")
    private int ordem;
    @JoinColumn(name = "oficioId", referencedColumnName = "oficioId", columnDefinition = "Ofício")
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Oficio oficioId;
    @OneToMany(mappedBy = "paragrafoPrincipal",cascade=CascadeType.PERSIST)
    @OrderBy(value="ordem")
    private Collection<Paragrafo> paragrafoCollection;
    @JoinColumn(name = "paragrafoPrincipal", referencedColumnName = "paragrafoId", columnDefinition = "Parágrafo Principal")
    @ManyToOne(cascade=CascadeType.PERSIST)
    private Paragrafo paragrafoPrincipal;

    public Paragrafo() {
    }

    public Paragrafo(Integer id) {
        this.paragrafoId = id;
    }

    public Paragrafo(Integer id, String corpo) {
        this.paragrafoId = id;
        this.corpo = corpo;
    }

    public Integer getParagrafoId() {
        return paragrafoId;
    }

    public void setParagrafoId(Integer id) {
        this.paragrafoId = id;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public Oficio getOficioId() {
        return oficioId;
    }

    public void setOficioId(Oficio oficio) {
        this.oficioId = oficio;
    }

    public Collection<Paragrafo> getParagrafoCollection() {
        return paragrafoCollection;
    }

    public void setParagrafoCollection(Collection<Paragrafo> paragrafoCollection) {
        this.paragrafoCollection = paragrafoCollection;
    }

    public Paragrafo getParagrafoPrincipal() {
        return paragrafoPrincipal;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public void setParagrafoPrincipal(Paragrafo paragrafoPrincipal) {
        this.paragrafoPrincipal = paragrafoPrincipal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paragrafoId != null ? paragrafoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paragrafo)) {
            return false;
        }
        Paragrafo other = (Paragrafo) object;
        if ((this.paragrafoId == null && other.paragrafoId != null) || (this.paragrafoId != null && !this.paragrafoId.equals(other.paragrafoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Paragrafo[id=" + paragrafoId + "]";
    }

}
