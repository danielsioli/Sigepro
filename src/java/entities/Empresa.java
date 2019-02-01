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
@Table(name = "empresa")
@NamedQueries({@NamedQuery(name = "Empresa.findByEmpresaCnpj", query = "SELECT e FROM Empresa e WHERE e.empresaCnpj = :empresaCnpj"), @NamedQuery(name = "Empresa.findByRazaoSocial", query = "SELECT e FROM Empresa e WHERE e.razaoSocial = :razaoSocial"), @NamedQuery(name = "Empresa.findByCep", query = "SELECT e FROM Empresa e WHERE e.cep = :cep"), @NamedQuery(name = "Empresa.findByEstado", query = "SELECT e FROM Empresa e WHERE e.estado = :estado"), @NamedQuery(name = "Empresa.findByCidade", query = "SELECT e FROM Empresa e WHERE e.cidade = :cidade"), @NamedQuery(name = "Empresa.findByBairro", query = "SELECT e FROM Empresa e WHERE e.bairro = :bairro"), @NamedQuery(name = "Empresa.findByLogradouro", query = "SELECT e FROM Empresa e WHERE e.logradouro = :logradouro"), @NamedQuery(name = "Empresa.findByNumeroLogradouro", query = "SELECT e FROM Empresa e WHERE e.numeroLogradouro = :numeroLogradouro"), @NamedQuery(name = "Empresa.findByComplementoLogradouro", query = "SELECT e FROM Empresa e WHERE e.complementoLogradouro = :complementoLogradouro"), @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"), @NamedQuery(name = "Empresa.findByLike", query = "SELECT e FROM Empresa e WHERE e.empresaCnpj LIKE :empresaCnpj")})
public class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "empresaCnpj", nullable = false, length = 18, columnDefinition = "CNPJ / CPF")
    private String empresaCnpj;
    @Column(name = "razaoSocial", nullable = false, length = 255, columnDefinition = "Razão Social")
    private String razaoSocial;
    @Column(name = "cep", nullable = false, length = 10, columnDefinition = "CEP")
    private String cep;
    @Column(name = "estado", nullable = false, columnDefinition = "Estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @Column(name = "cidade", nullable = false, length = 45, columnDefinition = "Cidade")
    private String cidade;
    @Column(name = "bairro", nullable = false, length = 100, columnDefinition = "Bairro")
    private String bairro;
    @Column(name = "logradouro", nullable = false, length = 100, columnDefinition = "Logradouro")
    private String logradouro;
    @Column(name = "numeroLogradouro", nullable = false, length = 10, columnDefinition = "Número do Logradouro")
    private String numeroLogradouro;
    @Column(name = "complementoLogradouro", length = 100, columnDefinition = "Complemento")
    private String complementoLogradouro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaCnpj")
    private Collection<Processo> processoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresaCnpj")
    private Collection<Procuracao> procuracaoCollection;
    @Column(name = "idtEntidade", nullable = false, length = 10, columnDefinition = "Flag do BDTA")
    private String idtEntidade;

    public enum Estado{
        AC,
        AL,
        AM,
        AP,
        BA,
        CE,
        DF,
        ES,
        GO,
        MA,
        MG,
        MS,
        MT,
        PA,
        PB,
        PE,
        PI,
        PR,
        RJ,
        RN,
        RO,
        RR,
        RS,
        SC,
        SE,
        SP,
        TO
    }
    
    public Empresa() {
    }

    public Empresa(String cnpj) {
        this.empresaCnpj = cnpj;
    }

    public Empresa(String cnpj, String razaoSocial, String cep, Estado estado, String cidade, String bairro, String logradouro, String numeroLogradouro) {
        this.empresaCnpj = cnpj;
        this.razaoSocial = toLowerCase(razaoSocial);
        this.cep = cep;
        this.estado = estado;
        this.cidade = toLowerCase(cidade);
        this.bairro = toLowerCase(bairro);
        this.logradouro = toLowerCase(logradouro);
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getIdtEntidade() {
        return idtEntidade;
    }

    public void setIdtEntidade(String idtEntidade) {
        this.idtEntidade = idtEntidade;
    }

    public String getEmpresaCnpj() {
        return empresaCnpj;
    }

    public void setEmpresaCnpj(String cnpj) {
        this.empresaCnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial.replaceAll("@#@","&");
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = toLowerCase(razaoSocial);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = toLowerCase(cidade);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = toLowerCase(bairro);
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = toLowerCase(logradouro);
    }

    public String getNumeroLogradouro() {
        return numeroLogradouro;
    }

    public void setNumeroLogradouro(String numeroLogradouro) {
        this.numeroLogradouro = numeroLogradouro;
    }

    public String getComplementoLogradouro() {
        return complementoLogradouro;
    }

    public void setComplementoLogradouro(String complementoLogradouro) {
        this.complementoLogradouro = toLowerCase(complementoLogradouro);
    }

    public Collection<Processo> getProcessoCollection() {
        return processoCollection;
    }

    public void setProcessoCollection(Collection<Processo> processoCollection) {
        this.processoCollection = processoCollection;
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
        hash += (empresaCnpj != null ? empresaCnpj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.empresaCnpj == null && other.empresaCnpj != null) || (this.empresaCnpj != null && !this.empresaCnpj.equals(other.empresaCnpj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return empresaCnpj;
    }
    
    private String toLowerCase(String rawString) {
        /*String lowerString = rawString.trim().toLowerCase();
        String[] words = lowerString.split(" ");
        lowerString = "";
        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals("de") && !words[i].equals("da") && !words[i].equals("do") && !words[i].equals("em") && !words[i].equals("na") && !words[i].equals("no") && !words[i].equals("a") && !words[i].equals("e") && !words[i].equals("o")) {
                if (words[i].length() >= 2) {
                    if(!words[i].equals("ltda") && !words[i].equals("s/a") && !words[i].equals("s.a") && !words[i].equals("s.a.") && !words[i].equals("me")){
                        if(words[i].substring(0,1).equals("(") || words[i].substring(0,1).equals("\"")){
                            words[i] = words[i].substring(0, 1) + words[i].substring(1, 2).toUpperCase() + words[i].substring(2, words[i].length()).toLowerCase();
                        }else{
                            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length()).toLowerCase();
                        }
                    }else{
                        words[i] = words[i].toUpperCase();
                    }
                }else{
                    words[i] = words[i].toUpperCase();
                }
            }
            lowerString += words[i] + " ";
        }
        lowerString = lowerString.substring(0, lowerString.length() - 1);
        return lowerString;*/
        return rawString;
    }
    
}
