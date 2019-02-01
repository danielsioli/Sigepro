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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "botao")
@NamedQueries({@NamedQuery(name = "Botao.findByBotaoId", query = "SELECT b FROM Botao b WHERE b.botaoId = :botaoId"), @NamedQuery(name = "Botao.findByName", query = "SELECT b FROM Botao b WHERE b.name = :name"), @NamedQuery(name = "Botao.findByUrl", query = "SELECT b FROM Botao b WHERE b.url = :url")})
public class Botao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "botaoId", nullable = false, columnDefinition = "Id do Botão")
    private Integer botaoId;
    @Column(name = "name", nullable = false, length = 45, columnDefinition = "Nome")
    private String name;
    @Column(name = "url", nullable = false, length = 255, columnDefinition = "URL")
    private String url;
    @JoinTable(name = "usuario_botao", joinColumns = {@JoinColumn(name = "botaoId", referencedColumnName = "botaoId")}, inverseJoinColumns = {@JoinColumn(name = "login", referencedColumnName = "login")})
    @ManyToMany
    private Collection<Usuario> loginCollection;
    @JoinTable(name = "botao_parametro", joinColumns = {@JoinColumn(name = "botaoId", referencedColumnName = "botaoId")}, inverseJoinColumns = {@JoinColumn(name = "parametroId", referencedColumnName = "parametroId")})
    @ManyToMany
    private Collection<Parametro> parametroIdCollection;

    public Botao() {
    }

    public Botao(Integer botaoId) {
        this.botaoId = botaoId;
    }

    public Botao(Integer botaoId, String name, String url) {
        this.botaoId = botaoId;
        this.name = name;
        this.url = url;
    }

    public Integer getBotaoId() {
        return botaoId;
    }

    public void setBotaoId(Integer botaoId) {
        this.botaoId = botaoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Collection<Usuario> getLoginCollection() {
        return loginCollection;
    }

    public void setLoginCollection(Collection<Usuario> loginCollection) {
        this.loginCollection = loginCollection;
    }

    public Collection<Parametro> getParametroIdCollection() {
        return parametroIdCollection;
    }

    public void setParametroIdCollection(Collection<Parametro> parametroIdCollection) {
        this.parametroIdCollection = parametroIdCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (botaoId != null ? botaoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Botao)) {
            return false;
        }
        Botao other = (Botao) object;
        if ((this.botaoId == null && other.botaoId != null) || (this.botaoId != null && !this.botaoId.equals(other.botaoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Botao[botaoId=" + botaoId + "]";
    }

}
