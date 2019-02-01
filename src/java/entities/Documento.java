/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

/**
 *
 * @author danieloliveira
 */
public abstract class Documento {
    
    public abstract String getModelo();
    public abstract void setModelo(String modelo);
    
    public String getTipoDocumento(){
        return this.getClass().getSimpleName();
    }
    
}
