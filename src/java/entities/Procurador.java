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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "procurador")
@NamedQueries({@NamedQuery(name = "Procurador.findByProcuradorCpf", query = "SELECT p FROM Procurador p WHERE p.procuradorCpf = :procuradorCpf"), @NamedQuery(name = "Procurador.findByNome", query = "SELECT p FROM Procurador p WHERE p.nome = :nome"), @NamedQuery(name = "Procurador.findByLike", query = "SELECT p FROM Procurador p WHERE p.procuradorCpf LIKE :procuradorCpf")})
public class Procurador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "procuradorCpf", nullable = false, length = 14, columnDefinition = "CPF")
    private String procuradorCpf;
    @Column(name = "pronome", nullable = false, length = 45, columnDefinition = "Pronome de Tratamento")
    private String pronome;
    @Column(name = "nome", nullable = false, length = 45, columnDefinition = "Nome")
    private String nome;
    @Column(name = "cargo", nullable = false, length = 45, columnDefinition = "Cargo")
    private String cargo;
    @Column(name = "sexo", nullable = false, columnDefinition = "Sexo")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "procuradorCpf")
    private Collection<Procuracao> procuracaoCollection;

    public enum Sexo{
        HOMEM,
        MULHER;
    }
    
    public Procurador() {
    }

    public Procurador(String cpf) {
        this.procuradorCpf = cpf;
    }

    public Procurador(String cpf, String nome) {
        this.procuradorCpf = cpf;
        this.nome = toLowerCase(nome);
    }

    public String getProcuradorCpf() {
        return procuradorCpf;
    }

    public void setProcuradorCpf(String cpf) {
        this.procuradorCpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getPronome() {
        return pronome;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public void setPronome(String pronome) {
        this.pronome = toLowerCase(pronome);
    }
    
    public void setNome(String nome) {
        this.nome = toLowerCase(nome);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = toLowerCase(cargo);
    }

    public Collection<Procuracao> getProcuracaoCollection() {
        return procuracaoCollection;
    }

    public void setProcuracaoCollection(Collection<Procuracao> procuracaoCollection) {
        this.procuracaoCollection = procuracaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (procuradorCpf != null ? procuradorCpf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Procurador)) {
            return false;
        }
        Procurador other = (Procurador) object;
        if ((this.procuradorCpf == null && other.procuradorCpf != null) || (this.procuradorCpf != null && !this.procuradorCpf.equals(other.procuradorCpf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return procuradorCpf;
    }
    
    private String toLowerCase(String rawString) {
        String lowerString = rawString.trim().toLowerCase();
        String[] words = lowerString.split(" ");
        lowerString = "";
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals("de") && !words[i].equals("da") && !words[i].equals("do") && !words[i].equals("em") && !words[i].equals("na") && !words[i].equals("no") && !words[i].equals("a") && !words[i].equals("e") && !words[i].equals("o")) {
                if (words[i].length() >= 2) {
                    words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length());
                }else{
                    words[i] = words[i].toUpperCase();
                }
            }
            lowerString += words[i] + " ";
        }
        lowerString = lowerString.substring(0, lowerString.length() - 1);
        return lowerString;
    }
    
}
