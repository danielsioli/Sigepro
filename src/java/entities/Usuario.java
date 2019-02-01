/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author danieloliveira
 */
@Entity
@Table(name = "usuario")
@NamedQueries({@NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.login = :login"), @NamedQuery(name = "Usuario.findByNome", query = "SELECT u FROM Usuario u WHERE u.nome = :nome"), @NamedQuery(name = "Usuario.findByCargo", query = "SELECT u FROM Usuario u WHERE u.cargo = :cargo"), @NamedQuery(name = "Usuario.findByPerfil", query = "SELECT u FROM Usuario u WHERE u.perfil = :perfil"), @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"), @NamedQuery(name = "Usuario.findByLike", query = "SELECT u FROM Usuario u WHERE u.login LIKE :login")})
public class Usuario implements Serializable, Principal {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "login", nullable = false, length = 20, columnDefinition = "Login")
    private String login;
    @Column(name = "nome", length = 45, columnDefinition = "Nome")
    private String nome;
    @Column(name = "cargo", length = 60, columnDefinition = "Cargo")
    private String cargo;
    @Column(name = "area", nullable = false, columnDefinition = "Área")
    @Enumerated(EnumType.STRING)
    private Area area;
    @Column(name = "sexo", nullable = false, columnDefinition = "Sexo")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Column(name = "perfil", nullable = false, columnDefinition = "Perfil")
    @Enumerated(EnumType.STRING)
    private Perfil perfil;
    @Column(name = "senha", nullable = false, length = 45, columnDefinition = "Senha")
    private String senha;
    @Column(name = "telefone", nullable = false, length = 18, columnDefinition = "Telefone")
    private String telefone;
    @ManyToMany(mappedBy = "loginCollection")
    private Collection<Botao> botaoIdCollection;
    @OneToMany(mappedBy = "login")
    private Collection<Oficio> oficiosAssinados;
    @OneToMany(mappedBy = "login")
    private Collection<Servico> servicosResponsabilizados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "login")
    private Collection<Historico> historicoCollection;
    @OneToMany(mappedBy = "loginSubstituto",cascade=CascadeType.ALL)
    private Collection<Usuario> loginSubstitutoCollection;
    @JoinColumn(name = "loginSubstituto", referencedColumnName = "login", columnDefinition = "Substituto")
    @ManyToOne
    private Usuario loginSubstituto;
    @Column(name = "tipoCargo", nullable = true, columnDefinition = "Tipo do Cargo")
    @Enumerated(EnumType.STRING)
    private TipoCargo tipoCargo;
    @Column(name = "credito", nullable = false, columnDefinition = "Crédito em distribuições")
    private int credito;
    @OneToMany(mappedBy = "login",cascade=CascadeType.ALL)
    @OrderBy("inicioAusencia DESC")
    private Collection<Ausencia> loginAusenciaCollection;
    @OneToMany(mappedBy = "dono",cascade=CascadeType.ALL)
    private Collection<Oficio> donoOficioCollection;
    @OneToMany(mappedBy = "remetente",cascade=CascadeType.ALL)
    private Collection<Distribuicao> distribuicoes;
    @OneToMany(mappedBy = "destinatario",cascade=CascadeType.ALL)
    private Collection<Distribuicao> recepcoes;
    @OneToMany(mappedBy = "login",cascade=CascadeType.ALL)
    @OrderBy("tipoDocumento DESC")
    private Collection<Estatistica> estatisticaCollection;
    
    public enum Perfil {

        ADMINISTRADOR,
        USUARIO_MOR,
        USUARIO_SENIOR,
        USUARIO_JUNIOR,
        CONVIDADO;
    }

    public enum Area {

        PVST,
        PVSTA,
        PVSTA1,
        PVSTA2,
        PVSTP,
        PVSTR,
        SPV;
    }
    
    public enum TipoCargo {
        Nenhum,
        Administrativo,
        Juridico,
        Tecnico;
    }

    public enum Sexo {
        HOMEM,
        MULHER;
    }
    private static final String STRING_ADMINISTRADOR = "Administrador";
    private static final String STRING_USUARIO_MOR = "Usuário Mor";
    private static final String STRING_USUARIO_SENIOR = "Usuário Sênior";
    private static final String STRING_USUARIO_JUNIOR = "Usuário Junior";
    private static final String STRING_CONVIDADO = "Convidado";
    
    private static final String STRING_TIPO_ADMINISTRATIVO = "Administrativo";
    private static final String STRING_TIPO_JURIDICO = "Jurídico";
    private static final String STRING_TIPO_TECNICO = "Técnico";
    private static final String STRING_TIPO_NENHUM = "";

    public Usuario() {
    }

    public Usuario(String login) {
        this.login = login;
        this.perfil = Perfil.CONVIDADO;
    }

    public Usuario(String login, Perfil perfil) {
        this.login = login;
        this.perfil = perfil;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Collection<Distribuicao> getDistribuicoes() {
        return distribuicoes;
    }

    public void setDistribuicoes(Collection<Distribuicao> distribuicoes) {
        this.distribuicoes = distribuicoes;
    }

    public Collection<Distribuicao> getRecepcoes() {
        return recepcoes;
    }

    public void setRecepcoes(Collection<Distribuicao> recepcoes) {
        this.recepcoes = recepcoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getTelefone() {
        return telefone;
    }

    public Collection<Oficio> getDonoOficioCollection() {
        return donoOficioCollection;
    }

    public void setDonoOficioCollection(Collection<Oficio> donoOficioCollection) {
        this.donoOficioCollection = donoOficioCollection;
    }

    public Collection<Ausencia> getLoginAusenciaCollection() {
        return loginAusenciaCollection;
    }

    public void setLoginAusenciaCollection(Collection<Ausencia> loginAusenciaCollection) {
        this.loginAusenciaCollection = loginAusenciaCollection;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(Integer credito){
        this.credito = credito.intValue();
    }
    
    public void setCredito(int credito) {
        this.credito = credito;
    }

    public static String getTipoCargoName(TipoCargo tipoCargo){
        if(tipoCargo.equals(TipoCargo.Administrativo)){
            return STRING_TIPO_ADMINISTRATIVO;
        }else if(tipoCargo.equals(TipoCargo.Juridico)){
            return STRING_TIPO_JURIDICO;
        }else if(tipoCargo.equals(TipoCargo.Tecnico)){
            return STRING_TIPO_TECNICO;
        }else if(tipoCargo.equals(TipoCargo.Nenhum)){
            return STRING_TIPO_NENHUM;
        }else{
            return null;
        }
    }
    
    public static String getPerfilName(Perfil perfil) {
        if (perfil.equals(Perfil.ADMINISTRADOR)) {
            return STRING_ADMINISTRADOR;
        } else if (perfil.equals(Perfil.USUARIO_MOR)) {
            return STRING_USUARIO_MOR;
        } else if (perfil.equals(Perfil.USUARIO_SENIOR)) {
            return STRING_USUARIO_SENIOR;
        } else if (perfil.equals(Perfil.USUARIO_JUNIOR)) {
            return STRING_USUARIO_JUNIOR;
        } else if (perfil.equals(Perfil.CONVIDADO)) {
            return STRING_CONVIDADO;
        } else {
            return null;
        }
    }

    public TipoCargo getTipoCargo() {
        return tipoCargo;
    }

    public void setTipoCargo(TipoCargo tipoCargo) {
        this.tipoCargo = tipoCargo;
    }

    public Collection<Botao> getBotaoIdCollection() {
        return botaoIdCollection;
    }

    public void setBotaoIdCollection(Collection<Botao> botaoIdCollection) {
        this.botaoIdCollection = botaoIdCollection;
    }

    public Collection<Oficio> getOficiosAssinados() {
        return oficiosAssinados;
    }

    public void setOficiosAssinados(Collection<Oficio> oficiosAssinados) {
        this.oficiosAssinados = oficiosAssinados;
    }

    public Collection<Servico> getServicosResponsabilizados() {
        return servicosResponsabilizados;
    }

    public void setServicosResponsabilizados(Collection<Servico> servicosResponsabilizados) {
        this.servicosResponsabilizados = servicosResponsabilizados;
    }

    public Collection<Historico> getHistoricoCollection() {
        return historicoCollection;
    }

    public void setHistoricoCollection(Collection<Historico> historicoCollection) {
        this.historicoCollection = historicoCollection;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getLoginSubstituto() {
        return loginSubstituto;
    }

    public void setLoginSubstituto(Usuario loginSubstituto) {
        this.loginSubstituto = loginSubstituto;
    }

    public Collection<Usuario> getLoginSubstitutoCollection() {
        return loginSubstitutoCollection;
    }

    public void setLoginSubstitutoCollection(Collection<Usuario> loginSubstitutoCollection) {
        this.loginSubstitutoCollection = loginSubstitutoCollection;
    }

    public Collection<Estatistica> getEstatisticaCollection() {
        return estatisticaCollection;
    }

    public Estatistica getMaiorEstatistica(){
        Iterator<Estatistica> iterator = estatisticaCollection.iterator();
        Estatistica maiorEstatistica = new Estatistica();
        maiorEstatistica.setFrequenciaUso(0.0);
        maiorEstatistica.setTipoDocumento("");
        maiorEstatistica.setModelo("");
        while(iterator.hasNext()){
            Estatistica estatistica = iterator.next();
            if(estatistica.getFrequenciaUso() > maiorEstatistica.getFrequenciaUso()){
                maiorEstatistica = estatistica;
            }
        }
        return maiorEstatistica;
    }
    
    public void setEstatisticaCollection(Collection<Estatistica> estatisticaCollection) {
        this.estatisticaCollection = estatisticaCollection;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return login;
    }

    @Override
    public String getName() {
        return login;
    }
}
