/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author danieloliveira
 */
@Entity
@Table(name = "distribuicao")
@NamedQueries({@NamedQuery(name = "Distribuicao.findByDistribuicaoId", query = "SELECT d FROM Distribuicao d WHERE d.distribuicaoId = :distribuicaoId")})
public class Distribuicao implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private Integer distribuicaoId;

    @JoinColumn(name = "sicap", referencedColumnName = "processo", columnDefinition = "Sevidor Ausente")
    @ManyToOne
    private Processo processo;
    @JoinColumn(name = "remetente", referencedColumnName = "login", columnDefinition = "Usuário que fez a distribuição")
    @ManyToOne
    private Usuario remetente;
    @JoinColumn(name = "destinatario", referencedColumnName = "login", columnDefinition = "Usuário que receberá")
    @ManyToOne
    private Usuario destinatario;
    @Column(name = "envio", nullable = true, columnDefinition = "Dia de envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date envio;
    @Column(name = "recebimento", nullable = false, columnDefinition = "Dia de recebimento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recebimento;
    
    public Distribuicao(){
        
    }
    
    public Distribuicao(Integer distribuicaoId){
        this.distribuicaoId = distribuicaoId;
    }
    
    public Integer getDistribuicaoId() {
        return distribuicaoId;
    }

    public void setDistribuicaoId(Integer distribuicaoId) {
        this.distribuicaoId = distribuicaoId;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Date getEnvio() {
        return envio;
    }

    public void setEnvio(Date envio) {
        this.envio = envio;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Date getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(Date recebimento) {
        this.recebimento = recebimento;
    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

}
