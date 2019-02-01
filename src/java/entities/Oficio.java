/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.Documento;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "oficio")
@NamedQueries({@NamedQuery(name = "Oficio.findByOficioId", query = "SELECT o FROM Oficio o WHERE o.oficioId = :oficioId"), @NamedQuery(name = "Oficio.findByNome", query = "SELECT o FROM Oficio o WHERE o.nome = :nome"), @NamedQuery(name = "Oficio.findAll", query = "SELECT o FROM Oficio o"), @NamedQuery(name = "Oficio.findAllOrderByNome", query = "SELECT o FROM Oficio o ORDER By o.nome"), @NamedQuery(name = "Oficio.findByLike", query = "SELECT o FROM Oficio o WHERE o.oficioId LIKE :oficioId")})
public class Oficio extends Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "oficioId", nullable = false, columnDefinition = "Id do Ofício")
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Integer oficioId;
    @Column(name = "nome", nullable = false, length = 30, columnDefinition = "Nome")
    private String nome;
    @Column(name = "assunto", nullable = false, length = 280, columnDefinition = "Assunto")
    private String assunto;
    @OneToMany(mappedBy = "oficioId",cascade=CascadeType.ALL)
    @OrderBy(value="ordem")
    private Collection<Paragrafo> paragrafoCollection;
    @JoinColumn(name = "login", referencedColumnName = "login", columnDefinition = "Assinante")
    @ManyToOne
    private Usuario login;
    @JoinColumn(name = "dono", referencedColumnName = "login", nullable = false, columnDefinition = "Dono do Ofício")
    @ManyToOne
    private Usuario dono;
    @Column(name = "publico", nullable = false, length = 280, columnDefinition = "Se o ofício é visto por todos")
    private int publico;

    public Oficio() {
    }

    public Oficio(Integer id) {
        this.oficioId = id;
    }

    public Oficio(Integer id, String nome, String assunto) {
        this.oficioId = id;
        this.nome = nome;
        this.assunto = assunto;
    }

    public Integer getOficioId() {
        return oficioId;
    }

    public void setOficioId(Integer id) {
        this.oficioId = id;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public int getPublico() {
        return publico;
    }

    public void setPublico(Integer publico) {
        this.publico = publico.intValue();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public Collection<Paragrafo> getParagrafoCollection() {
        return paragrafoCollection;
    }

    public void setParagrafoCollection(Collection<Paragrafo> paragrafoCollection) {
        this.paragrafoCollection = paragrafoCollection;
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
        hash += (oficioId != null ? oficioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Oficio)) {
            return false;
        }
        Oficio other = (Oficio) object;
        if ((this.oficioId == null && other.oficioId != null) || (this.oficioId != null && !this.oficioId.equals(other.oficioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Oficio[id=" + oficioId + "]";
    }

    @Override
    public String getModelo() {
        return this.nome;
    }

    @Override
    public void setModelo(String modelo) {
        this.setNome(modelo);
    }
}
