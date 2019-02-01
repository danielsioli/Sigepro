/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.DAO;
import entities.Empresa;
import entities.Estatistica;
import entities.EstatisticaPK;
import entities.Oficio;
import entities.Paragrafo;
import entities.Processo;
import entities.Procuracao;
import entities.Procurador;
import entities.Usuario;
import entities.Documento;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ajaxtags.servlets.BaseAjaxServlet;
import org.ajaxtags.xml.AjaxXmlBuilder;

/**
 *
 * @author Daniel
 */
public class Control extends BaseAjaxServlet {

    @Override
    public String getXmlContent(HttpServletRequest request, HttpServletResponse response) {
        try {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
                request.setCharacterEncoding("LATIN1");
            }
            String method = request.getParameter("method");
            if (method != null) {
                String msg = isValidFields(request, response);
                if (msg.length() > 0) {
                    return msg;
                }
                return ("" + this.getClass().getMethod(method, new Class[]{HttpServletRequest.class, HttpServletResponse.class}).invoke(this, new Object[]{request, response})).replaceAll("&amp;", "&").replaceAll("&", "&amp;");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new AjaxXmlBuilder().toString();
    }

    public String documentoIncluir(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String login = "" + request.getSession().getAttribute("username");
        if (isUserAuthorized(login, "documentoIncluir", Oficio.class, -1)) {
            int oficioId = Integer.parseInt(request.getParameter("oficioId"));
            if (oficioId == -1) {
                Oficio newOficio = new Oficio();
                newOficio.setAssunto(request.getParameter("assunto"));
                newOficio.setLogin((Usuario) DAO.getInstance().find(Usuario.class, request.getParameter("assinante")));
                newOficio.setNome(request.getParameter("nome"));
                newOficio.setDono((Usuario) DAO.getInstance().find(Usuario.class, request.getParameter("dono")));
                newOficio.setPublico(new Integer(request.getParameter("publico")));
                Vector<Paragrafo> paragrafos = new Vector<Paragrafo>();
                Enumeration<String> parameterNames = request.getParameterNames();
                Paragrafo paragrafo = null;
                Paragrafo subParagrafo = null;
                Paragrafo item = null;
                int ordem = 1;
                while (parameterNames.hasMoreElements()) {
                    String parameterName = parameterNames.nextElement();
                    if (parameterName.startsWith("paragrafo") && !parameterName.endsWith("_id")) {
                        paragrafo = new Paragrafo();
                        paragrafo.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                        paragrafo.setOficioId(newOficio);
                        paragrafo.setParagrafoCollection(new Vector<Paragrafo>());
                        paragrafo.setOrdem(ordem++);
                        paragrafos.add(paragrafo);
                    } else if (parameterName.startsWith("subParagrafo") && !parameterName.endsWith("_id")) {
                        subParagrafo = new Paragrafo();
                        subParagrafo.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                        subParagrafo.setOficioId(newOficio);
                        subParagrafo.setParagrafoCollection(new Vector<Paragrafo>());
                        subParagrafo.setParagrafoPrincipal(paragrafo);
                        subParagrafo.setOrdem(ordem++);
                        paragrafo.getParagrafoCollection().add(subParagrafo);
                        paragrafos.add(subParagrafo);
                    } else if (parameterName.startsWith("item") && !parameterName.endsWith("_id")) {
                        item = new Paragrafo();
                        item.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                        item.setOficioId(newOficio);
                        item.setParagrafoPrincipal(subParagrafo);
                        item.setOrdem(ordem++);
                        subParagrafo.getParagrafoCollection().add(item);
                        paragrafos.add(item);
                    }
                }
                newOficio.setParagrafoCollection(paragrafos);
                DAO.getInstance().persist(new Oficio[]{newOficio});
                ajaxXmlBuilder.addItem("transactionOk", "true");
                ajaxXmlBuilder.addItem("message", "Ofício incluído com sucesso");
                ajaxXmlBuilder.addItem("oficioId", "" + newOficio.getOficioId());
                ajaxXmlBuilder.addItem("oficioNome", newOficio.getNome());
            } else {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Id Inesperado");
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
        }
        return ajaxXmlBuilder.toString();
    }

    public String documentoExcluir(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        int oficioId = Integer.parseInt(request.getParameter("oficioId"));
        String login = "" + request.getSession().getAttribute("username");
        if (oficioId != -1) {
            if (isUserAuthorized(login, "documentoExcluir", Oficio.class, -1)) {
                Oficio oficio = DAO.getInstance().find(Oficio.class, oficioId);
                if (oficio != null) {
                    DAO.getInstance().remove(new Oficio[]{oficio});
                    ajaxXmlBuilder.addItem("transactionOk", "true");
                    ajaxXmlBuilder.addItem("message", "Dados excluídos com sucesso");
                    ajaxXmlBuilder.addItem("oficioId", "" + oficioId);
                } else {
                    ajaxXmlBuilder.addItem("transactionOk", "false");
                    ajaxXmlBuilder.addItem("message", "oficioId = " + oficioId + " não existe no banco");
                }
            } else {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "oficioId =" + -1 + " não permitido");
        }
        return ajaxXmlBuilder.toString();
    }

    public String documentoAlterar(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        int oficioId = Integer.parseInt(request.getParameter("oficioId"));
        String login = "" + request.getSession().getAttribute("username");
        if (oficioId != -1) {
            if (isUserAuthorized(login, "documentoAlterar", Oficio.class, -1)) {
                Oficio oldOficio = DAO.getInstance().find(Oficio.class, oficioId);
                if (oldOficio != null) {
                    Oficio newOficio = new Oficio(oficioId);
                    newOficio.setAssunto(request.getParameter("assunto"));
                    newOficio.setLogin((Usuario) DAO.getInstance().find(Usuario.class, request.getParameter("assinante")));
                    newOficio.setNome(request.getParameter("nome"));
                    newOficio.setDono((Usuario) DAO.getInstance().find(Usuario.class, request.getParameter("dono")));
                    newOficio.setPublico(new Integer(request.getParameter("publico")));
                    Vector<Paragrafo> paragrafos = new Vector<Paragrafo>();
                    Enumeration<String> parameterNames = request.getParameterNames();
                    Paragrafo paragrafo = null;
                    Paragrafo subParagrafo = null;
                    Paragrafo item = null;
                    int ordem = 1;
                    while (parameterNames.hasMoreElements()) {
                        String parameterName = parameterNames.nextElement();
                        if (parameterName.startsWith("paragrafo") && !parameterName.endsWith("_id")) {
                            paragrafo = new Paragrafo();
                            try {
                                paragrafo.setParagrafoId(Integer.parseInt(request.getParameter(parameterName + "_id")));
                            } catch (Exception ex) {
                            }
                            paragrafo.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                            paragrafo.setOficioId(newOficio);
                            paragrafo.setParagrafoCollection(new Vector<Paragrafo>());
                            paragrafo.setOrdem(ordem++);
                            paragrafos.add(paragrafo);
                        } else if (parameterName.startsWith("subParagrafo") && !parameterName.endsWith("_id")) {
                            subParagrafo = new Paragrafo();
                            try {
                                subParagrafo.setParagrafoId(Integer.parseInt(request.getParameter(parameterName + "_id")));
                            } catch (Exception ex) {
                            }
                            subParagrafo.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                            subParagrafo.setOficioId(newOficio);
                            subParagrafo.setParagrafoCollection(new Vector<Paragrafo>());
                            subParagrafo.setParagrafoPrincipal(paragrafo);
                            subParagrafo.setOrdem(ordem++);
                            paragrafo.getParagrafoCollection().add(subParagrafo);
                            paragrafos.add(subParagrafo);
                        } else if (parameterName.startsWith("item") && !parameterName.endsWith("_id")) {
                            item = new Paragrafo();
                            try {
                                item.setParagrafoId(Integer.parseInt(request.getParameter(parameterName + "_id")));
                            } catch (Exception ex) {
                            }
                            item.setCorpo(request.getParameter(parameterName).replaceAll("#amp;", "&"));
                            item.setOficioId(newOficio);
                            item.setParagrafoPrincipal(subParagrafo);
                            item.setOrdem(ordem++);
                            subParagrafo.getParagrafoCollection().add(item);
                            paragrafos.add(item);
                        }
                    }
                    newOficio.setParagrafoCollection(paragrafos);
                    removeParagrafos(oldOficio, newOficio);
                    DAO.getInstance().merge(new Oficio[]{newOficio});
                    ajaxXmlBuilder.addItem("transactionOk", "true");
                    ajaxXmlBuilder.addItem("message", "Ofício alterado com sucesso");
                } else {
                    ajaxXmlBuilder.addItem("transactionOk", "false");
                    ajaxXmlBuilder.addItem("message", "oficioId = " + oficioId + " não existe no banco.");
                }
            } else {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "oficioId =" + -1 + " não permitido");
        }
        return ajaxXmlBuilder.toString();
    }

    private void removeParagrafos(Oficio oldOficio, Oficio newOficio) {
        Collection<Paragrafo> oldParagrafos = oldOficio.getParagrafoCollection();
        Collection<Paragrafo> newParagrafos = newOficio.getParagrafoCollection();
        Paragrafo[] oldParagrafosArray = new Paragrafo[1];
        oldParagrafosArray = oldParagrafos.toArray(oldParagrafosArray);
        Paragrafo[] newParagrafosArray = new Paragrafo[1];
        newParagrafosArray = newParagrafos.toArray(newParagrafosArray);
        for (int i = 0; i < oldParagrafosArray.length; i++) {
            boolean remove = true;
            for (int j = 0; j < newParagrafosArray.length; j++) {
                if (newParagrafosArray[j].getParagrafoId() != null && oldParagrafosArray[i].equals(newParagrafosArray[j])) {
                    remove = false;
                    break;
                }
            }
            if (remove) {
                DAO.getInstance().remove(new Paragrafo[]{oldParagrafosArray[i]});
            }
        }
    }

    public String incluir(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String tableName = request.getParameter("tabela");
        String login = "" + request.getSession().getAttribute("username");
        if (tableName != null || login != null) {
            try {
                Class tableClass = getTableClass(tableName);
                Field primaryKeyField = getPrimaryKeyField(tableClass);
                Object primaryKeyValue = getPrimaryKeyValue(primaryKeyField, request);
                if (isUserAuthorized(login, "incluir", tableClass, primaryKeyValue)) {
                    Object entity = getEntityFromDB(tableClass, primaryKeyField, primaryKeyValue);
                    if (entity != null) {
                        ajaxXmlBuilder.addItem("transactionOk", "false");
                        ajaxXmlBuilder.addItem("message", primaryKeyField.getName() + " = " + primaryKeyValue + " já existe no Banco de Dados. Favor usar a opção Alterar");
                    } else {
                        Field[] foreignKeyFields = getForeignKeyFields(tableClass);
                        boolean existsForeignEntities = true;
                        for (int i = 0; i < foreignKeyFields.length; i++) {
                            if (!existsEntity(foreignKeyFields[i].getType(), foreignKeyFields[i], request.getParameter(foreignKeyFields[i].getName()))) {
                                existsForeignEntities = false;
                                //TODO: mandar abrir tela de cadastro
                                ajaxXmlBuilder.addItem("transactionOk", "false");
                                ajaxXmlBuilder.addItem("message", foreignKeyFields[i].getName() + " = " + request.getParameter(foreignKeyFields[i].getName()) + " não existe no banco");
                                break;
                            }
                        }
                        if (existsForeignEntities) {
                            Object[] newEntity = new Object[1];
                            newEntity[0] = getEntity(tableClass, request);
                            DAO.getInstance().persist(newEntity);
                            ajaxXmlBuilder.addItem("transactionOk", "true");
                            ajaxXmlBuilder.addItem("message", "Dados incluídos com sucesso");
                        }
                    }
                } else {
                    //TODO: medidas de auditoria
                    ajaxXmlBuilder.addItem("transactionOk", "false");
                    ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Uma exceção foi lançada: " + ex.getLocalizedMessage());
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "Impossível determinar ação desejada. Favor contactar o administrador do sistema");
        }
        return ajaxXmlBuilder.toString();
    }

    public String excluir(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String tableName = request.getParameter("tabela");
        String login = "" + request.getSession().getAttribute("username");
        if (tableName != null || login != null) {
            try {
                Class tableClass = getTableClass(tableName);
                Field primaryKeyField = getPrimaryKeyField(tableClass);
                Object primaryKeyValue = getPrimaryKeyValue(primaryKeyField, request);
                if (isUserAuthorized(login, "excluir", tableClass, primaryKeyValue)) {
                    Object entity = getEntityFromDB(tableClass, primaryKeyField, primaryKeyValue);
                    if (entity == null) {
                        ajaxXmlBuilder.addItem("transactionOk", "false");
                        ajaxXmlBuilder.addItem("message", primaryKeyField.getName() + " = " + primaryKeyValue + " não existe no Banco de Dados");
                    } else {
                        Field[] fields = tableClass.getDeclaredFields();
                        boolean ableToExclude = true;
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].getAnnotation(OneToMany.class) != null || fields[i].getAnnotation(ManyToMany.class) != null) {
                                List<?> collection = (List<?>) tableClass.getMethod("get" + fields[i].getName().substring(0, 1).toUpperCase() + fields[i].getName().substring(1, fields[i].getName().length())).invoke(entity);
                                if (collection != null && collection.size() > 0) {
                                    //TODO: reclamar ou apagar tudo?
                                    //TODO: quando for usuário
                                    ableToExclude = false;
                                    ajaxXmlBuilder.addItem("transactionOk", "false");
                                    ajaxXmlBuilder.addItem("message", "Você deve excluir os(as) " + fields[i].getName().substring(0, fields[i].getName().indexOf("Collection")) + "s desse(a) " + tableClass.getSimpleName() + " primeiro");
                                    break;
                                }
                            }
                        }
                        if (ableToExclude) {
                            //TODO: excluir
                            Object[] oldEntity = new Object[]{entity};
                            oldEntity[0] = entity;
                            DAO.getInstance().remove(oldEntity);
                            ajaxXmlBuilder.addItem("transactionOk", "true");
                            ajaxXmlBuilder.addItem("message", "Dados excluídos com sucesso");
                        }
                    }
                } else {
                    //TODO: medidas de auditoria
                    ajaxXmlBuilder.addItem("transactionOk", "false");
                    ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
                }
            } catch (Exception ex) {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Uma exceção foi lançada: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "Impossível determinar ação desejada");
        }
        return ajaxXmlBuilder.toString();
    }

    public String alterar(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String tableName = request.getParameter("tabela");
        String login = "" + request.getSession().getAttribute("username");
        if (tableName != null || login != null) {
            try {
                Class tableClass = getTableClass(tableName);
                Field primaryKeyField = getPrimaryKeyField(tableClass);
                Object primaryKeyValue = getPrimaryKeyValue(primaryKeyField, request);
                if (isUserAuthorized(login, "alterar", tableClass, primaryKeyValue)) {
                    Object entity = getEntityFromDB(tableClass, primaryKeyField, primaryKeyValue);
                    if (entity == null) {
                        ajaxXmlBuilder.addItem("transactionOk", "false");
                        ajaxXmlBuilder.addItem("message", primaryKeyField.getName() + " = " + primaryKeyValue + " não existe no Banco de Dados");
                    } else {
                        Object[] alteredEntity = new Object[1];
                        alteredEntity[0] = getEntity(tableClass, request);
                        if (tableName.equals("Usuario")) {
                            Usuario alteredUsuario = (Usuario) alteredEntity[0],
                                    bdUsuario = (Usuario) entity;
                            if (!alteredUsuario.getSenha().equals(bdUsuario.getSenha())) {
                                alteredUsuario.setSenha(DAO.getInstance().getCryptedPassword(alteredUsuario.getSenha()));
                                alteredEntity[0] = alteredUsuario;
                            }
                        }
                        DAO.getInstance().merge(alteredEntity);
                        ajaxXmlBuilder.addItem("transactionOk", "true");
                        ajaxXmlBuilder.addItem("message", "Dados alterados com sucesso");
                    }
                } else {
                    //TODO: medidas de auditoria
                    ajaxXmlBuilder.addItem("transactionOk", "false");
                    ajaxXmlBuilder.addItem("message", "Usuário não autorizado a realizar operação");
                }
            } catch (Exception ex) {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Uma exceção foi lançada: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "Impossível determinar ação desejada");
        }
        return ajaxXmlBuilder.toString();
    }

    public String consultar(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            List<Object> objects = null;
            Enumeration<String> parameterNames = request.getParameterNames();
            Hashtable<String, Object> parameters = new Hashtable<String, Object>();
            String tableName = request.getParameter("tabela");
            Class tableClass = getTableClass(tableName);
            String unnamedQuery = "SELECT o FROM " + tableName + " o WHERE ";
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = request.getParameter(parameterName);
                if (!parameterName.equals("method") && !parameterName.equals("tabela") && parameterValue != null && parameterValue.length() > 0) {
                    Field field = tableClass.getDeclaredField(parameterName);
                    Class fieldClass = field.getType();
                    if (fieldClass.isEnum()) {
                        Object enumConstant = fieldClass.getMethod("valueOf", new Class[]{String.class}).invoke(null, request.getParameter(parameterName));
                        parameters.put(parameterName, enumConstant);
                        unnamedQuery += "o." + parameterName + " = :" + parameterName + " AND ";
                    } else if (fieldClass.equals(Date.class)) {
                        int day = Integer.parseInt(request.getParameter(parameterName).split("/")[0]);
                        int month = Integer.parseInt(request.getParameter(parameterName).split("/")[1]) - 1;
                        int year = Integer.parseInt(request.getParameter(parameterName).split("/")[2]);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, 23, 59, 59);
                        parameters.put(parameterName, new Date(calendar.getTimeInMillis()));
                        unnamedQuery += "o." + parameterName + " = :" + parameterName + " AND ";
                    } else if (fieldClass.equals(String.class)) {
                        parameters.put(parameterName, "%" + parameterValue + "%");
                        unnamedQuery += "o." + parameterName + " LIKE :" + parameterName + " AND ";
                    } else if (fieldClass.getName().equals("int")) {
                        parameters.put(parameterName, Integer.parseInt(parameterValue));
                        unnamedQuery += "o." + parameterName + " = :" + parameterName + " AND ";
                    } else if (fieldClass.equals(Usuario.class)) {
                        Usuario substituto = DAO.getInstance().find(Usuario.class, parameterValue);
                        parameters.put(parameterName, substituto);
                        unnamedQuery += "o." + parameterName + " = :" + parameterName + " AND ";
                    } else {
                        parameters.put(parameterName, "%" + parameterValue + "%");
                        unnamedQuery += "o." + parameterName + "." + parameterName + " LIKE :" + parameterName + " AND ";
                    }
                }
            }
            unnamedQuery = unnamedQuery.substring(0, unnamedQuery.length() - 5);
            objects = DAO.getInstance().findByUnnamedQuery(tableClass, unnamedQuery, parameters);
            stringBuffer.append("<div style=\"text-align:right;cursor:pointer\"><a onclick=\"document.getElementById('consultaTabela').style.display='none'\">Fechar</a></div>");
            stringBuffer.append(getSearchTable(tableClass, objects, request));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringBuffer.toString();
    }

    public String updateEstatistica(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        try {
            Usuario usuario = getUsuario("" + request.getSession().getAttribute("username"));
            //TODO: deverá ser criado uma classe Documento que será mãe de todos os documentos
            Class tableClass = getTableClass(request.getParameter("tipoDocumento"));
            Documento documento = (Documento) tableClass.newInstance();
            if (!documento.getTipoDocumento().equals("AR") && !documento.getTipoDocumento().equals("Etiqueta")) {
                Field primaryKeyField = getPrimaryKeyField(tableClass);
                String primaryKeyFieldClassName = primaryKeyField.getType().getName();
                if (primaryKeyFieldClassName.equals("java.lang.Integer")) {
                    documento = (Documento) DAO.getInstance().find(tableClass, Integer.parseInt(request.getParameter("modelo")));
                } else {
                    documento = (Documento) DAO.getInstance().find(tableClass, request.getParameter("modelo"));
                }
            }
            Collection<Estatistica> estatisticaCollection = usuario.getEstatisticaCollection();
            Iterator<Estatistica> iterator = estatisticaCollection.iterator();
            boolean novo = true;
            Estatistica estatistica = new Estatistica();
            while (iterator.hasNext()) {
                estatistica = iterator.next();
                if (estatistica.getTipoDocumento().equals(documento.getTipoDocumento())) {
                    if (estatistica.getModelo().equals(documento.getModelo())) {
                        estatistica.setFrequenciaUso(estatistica.getFrequenciaUso() + 1.0);
                        novo = false;
                        break;
                    }
                }
            }
            if (novo) {
                EstatisticaPK novaEstatisticaPK = new EstatisticaPK();
                novaEstatisticaPK.setLogin(usuario.getLogin());
                novaEstatisticaPK.setModelo(documento.getModelo());
                novaEstatisticaPK.setTipoDocumento(documento.getTipoDocumento());
                estatistica = new Estatistica();
                estatistica.setEstatisticaPK(novaEstatisticaPK);
                estatistica.setModelo(novaEstatisticaPK.getModelo());
                estatistica.setTipoDocumento(novaEstatisticaPK.getTipoDocumento());
                estatistica.setFrequenciaUso(1.0);
                estatistica.setLogin(usuario);
            }
            Object[] estatisticas = new Object[1];
            estatisticas[0] = estatistica;
            if (novo) {
                DAO.getInstance().persist(estatisticas);
            } else {
                DAO.getInstance().merge(estatisticas);
            }
            usuario.getEstatisticaCollection().add(estatistica);
            Object[] usuarios = new Object[1];
            usuarios[0] = usuario;
            DAO.getInstance().merge(usuarios);
            ajaxXmlBuilder.addItem("transactionOk", "true");
            ajaxXmlBuilder.addItem("message", "Estatistica atualizada");
        } catch (InstantiationException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ajaxXmlBuilder.toString();
    }

    private String getSearchTable(Class tableClass, List<Object> objects, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String tableName = request.getParameter("tabela");
        StringBuffer stringBuffer = new StringBuffer();
        if (objects != null && objects.size() > 0) {
            Field[] fields = tableClass.getDeclaredFields();
            stringBuffer.append("<table border=1>\n");
            stringBuffer.append("<tr>\n");
            for (int i = 0; i < fields.length; i++) {
                Column column = fields[i].getAnnotation(Column.class);
                if (column != null) {
                    stringBuffer.append("<td>\n");
                    stringBuffer.append(column.columnDefinition() + "\n");
                    stringBuffer.append("</td>\n");
                } else {
                    JoinColumn joinColumn = fields[i].getAnnotation(JoinColumn.class);
                    if (joinColumn != null) {
                        stringBuffer.append("<td>\n");
                        stringBuffer.append(joinColumn.columnDefinition() + "\n");
                        stringBuffer.append("</td>\n");
                    }
                }
            }
            stringBuffer.append("<td>\n</td>\n<td>\n</td>\n");
            stringBuffer.append("</tr>\n");
            for (int i = 0; i < objects.size(); i++) {
                Vector<String> primaryKeyName = new Vector<String>();
                Vector<String> primaryKeyValue = new Vector<String>();
                stringBuffer.append("<tr>\n");
                for (int j = 0; j < fields.length; j++) {
                    Column column = fields[j].getAnnotation(Column.class);
                    if (column != null) {
                        String fieldName = fields[j].getName();
                        String value = "" + tableClass.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length())).invoke(objects.get(i));
                        if (fields[j].getAnnotation(Id.class) != null) {
                            primaryKeyName.add(fields[j].getName());
                            primaryKeyValue.add(value);
                        }
                        stringBuffer.append("<td>\n");
                        if (fieldName.equals("senha")) {
                            stringBuffer.append("**********\n");
                        } else {
                            stringBuffer.append(value + "\n");
                        }
                        stringBuffer.append("</td>\n");
                    } else {
                        JoinColumn joinColumn = fields[j].getAnnotation(JoinColumn.class);
                        if (joinColumn != null) {
                            String fieldName = fields[j].getName();
                            String value = "" + tableClass.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length())).invoke(objects.get(i));
                            stringBuffer.append("<td>\n");
                            stringBuffer.append(value + "\n");
                            stringBuffer.append("</td>\n");
                            if (!joinColumn.insertable()) {
                                primaryKeyName.add(fields[j].getName());
                                primaryKeyValue.add(value);
                            }
                        } else if (fields[j].getAnnotation(EmbeddedId.class) != null) {
                        }
                    }
                }
                stringBuffer.append("<td>\n<a href=\"" + request.getContextPath() + "/excluir/" + tableName.toLowerCase() + ".jsp?");
                for (int t = 0; t < primaryKeyName.size(); t++) {
                    stringBuffer.append(primaryKeyName.get(t) + "=" + primaryKeyValue.get(t));
                    if (t != primaryKeyName.size() - 1) {
                        stringBuffer.append("&");
                    }
                }
                stringBuffer.append("\">Excluir</a>\n</td>\n<td>\n<a href=\"" + request.getContextPath() + "/alterar/" + tableName.toLowerCase() + ".jsp?");
                for (int t = 0; t < primaryKeyName.size(); t++) {
                    stringBuffer.append(primaryKeyName.get(t) + "=" + primaryKeyValue.get(t));
                    if (t != primaryKeyName.size() - 1) {
                        stringBuffer.append("&");
                    }
                }
                stringBuffer.append("\">Alterar</a>\n</td>\n");
                stringBuffer.append("</tr>\n");
            }
            stringBuffer.append("</table>");
        }
        return stringBuffer.toString();
    }

    public String getProcesso(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String sicap = request.getParameter("sicap");
        if (sicap != null) {
            Processo processo = DAO.getInstance().find(Processo.class, sicap);
            if (processo != null) {
                ajaxXmlBuilder.addItem("transactionOk", "true");
                String[] razaoSocial = processo.getEmpresaCnpj().getRazaoSocial().split(" ");
                ajaxXmlBuilder.addItem("message", processo.getSicap() + " - " + razaoSocial[0] + (razaoSocial.length > 1 ? " " + razaoSocial[1] : "") + " - " + processo.getServicoNum().getServicoNum());
                ajaxXmlBuilder.addItem("processo", processo.getSicap());
            } else {
                ajaxXmlBuilder.addItem("transactionOk", "false");
                ajaxXmlBuilder.addItem("message", "Processo não encontrado");
                ajaxXmlBuilder.addItem("processo", "");
            }
        } else {
            ajaxXmlBuilder.addItem("transactionOk", "false");
            ajaxXmlBuilder.addItem("message", "Processo não encontrado");
            ajaxXmlBuilder.addItem("processo", "");
        }
        return ajaxXmlBuilder.toString();
    }

    public String autoComplete(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String tableName = request.getParameter("tabela");
        String primaryKeyName = request.getParameter("primaryKeyName");
        String primaryKeyValue = request.getParameter("primaryKeyValue");
        if (tableName != null && primaryKeyValue != null && primaryKeyName != null) {
            DAO dao = DAO.getInstance();
            try {
                Class tableClass = getTableClass(tableName);
                List<?> tableObjects = null;
                String procuradorCpf = request.getParameter("procurador");
                String empresaCnpj = request.getParameter("empresa");
                String target = "";
                Hashtable<String, String> parameters = new Hashtable<String, String>();
                parameters.put(primaryKeyName, getNumbersOnly(primaryKeyValue) + "%");
                if (procuradorCpf != null && procuradorCpf.length() > 0) {
                    parameters.put("procuradorCpf", getNumbersOnly(procuradorCpf));
                    List<Procuracao> procuracoes = dao.findByNamedQuery(tableClass, "Procuracao.findByLikeEmpresa", parameters);
                    Vector<Empresa> empresas = new Vector();
                    for (int i = 0; i < procuracoes.size(); i++) {
                        empresas.add(procuracoes.get(i).getEmpresaCnpj());
                    }
                    tableObjects = empresas;
                } else if (empresaCnpj != null && empresaCnpj.length() > 0) {
                    parameters.put("empresaCnpj", getNumbersOnly(empresaCnpj));
                    List<Procuracao> procuracoes = dao.findByNamedQuery(tableClass, "Procuracao.findByLikeProcurador", parameters);
                    Vector<Procurador> procuradores = new Vector();
                    for (int i = 0; i < procuracoes.size(); i++) {
                        procuradores.add(procuracoes.get(i).getProcuradorCpf());
                    }
                    tableObjects = procuradores;
                } else {
                    tableObjects = dao.findByNamedQuery(tableClass, tableName + ".findByLike", parameters);
                }
                Field[] fields = tableClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    if (!Modifier.isStatic(fields[i].getModifiers()) && !fields[i].getName().endsWith("Collection") && !fields[i].getName().equals("serialVersionUID")) {
                        target += "," + fields[i].getName();
                    }
                }
                if (target.length() > 0) {
                    target = target.substring(1);
                }
                ajaxXmlBuilder.addItems(tableObjects, primaryKeyName, primaryKeyName);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ajaxXmlBuilder.toString();
    }

    public String updateField(HttpServletRequest request, HttpServletResponse response) {
        AjaxXmlBuilder ajaxXmlBuilder = new AjaxXmlBuilder();
        String tableName = request.getParameter("tabela");
        if (tableName != null) {
            DAO dao = DAO.getInstance();
            try {
                Class tableClass = getTableClass(tableName);
                Field primaryKeyField = getPrimaryKeyField(tableClass);
                Object primaryKeyValue = getPrimaryKeyValue(primaryKeyField, request);
                Object entity = getEntityFromDB(tableClass, primaryKeyField, primaryKeyValue);
                if (entity != null) {
                    Method[] methods = tableClass.getMethods();
                    int i;
                    for (i = 0; i < methods.length; i++) {
                        if (methods[i].getName().startsWith("get") && !methods[i].getName().endsWith("Collection") && !methods[i].getName().equals("getClass")) {
                            String fieldName = methods[i].getName().substring(3, 4).toLowerCase(Locale.ENGLISH) + methods[i].getName().substring(4, methods[i].getName().length());
                            try {
                                Field field = tableClass.getDeclaredField(fieldName);
                                if (field.getType().isEnum()) {
                                    ajaxXmlBuilder.addItem(fieldName, "" + methods[i].invoke(entity).toString());
                                } else if (field.getType().equals(Date.class)) {
                                    Date validade = (Date) methods[i].invoke(entity);
                                    Calendar calendar = Calendar.getInstance();
                                    if (validade != null) {
                                        calendar.setTime(validade);
                                        ajaxXmlBuilder.addItem(fieldName, "" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                                    } else {
                                        ajaxXmlBuilder.addItem(fieldName, "");
                                    }
                                } else {
                                    ajaxXmlBuilder.addItem(fieldName, "" + methods[i].invoke(entity));
                                }
                            } catch (NoSuchFieldException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ajaxXmlBuilder.toString();
    }

    private String getNumbersOnly(String rawString) {
        return rawString.replaceAll("[^0-9]", "");
    }

    public static String toUTF8(String text) {
        try {
            text = URLDecoder.decode(text, "ISO-8859-1");
            text = text.replaceAll("&amp;", "&");
            text = text.replaceAll("&lt;", "<");
            text = text.replaceAll("&gt;", ">");
        } catch (Exception ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        return text;
    }

    public static String getVariables(Usuario usuario, Procurador procurador, Processo processo, String text) {
        if (text.contains("@Empresa")) {
            text = text.replaceAll("@Empresa.getEmpresaCnpj@", processo.getEmpresaCnpj().getEmpresaCnpj());
            text = text.replaceAll("@Empresa.getRazaoSocial@", processo.getEmpresaCnpj().getRazaoSocial());
            text = text.replaceAll("@Empresa.getCep@", processo.getEmpresaCnpj().getCep());
            text = text.replaceAll("@Empresa.getEstado@", processo.getEmpresaCnpj().getEstado().toString());
            text = text.replaceAll("@Empresa.getCidade@", processo.getEmpresaCnpj().getCidade());
            text = text.replaceAll("@Empresa.getBairro@", processo.getEmpresaCnpj().getBairro());
            text = text.replaceAll("@Empresa.getLogradouro@", processo.getEmpresaCnpj().getLogradouro());
            text = text.replaceAll("@Empresa.getNumeroLogradouro@", processo.getEmpresaCnpj().getNumeroLogradouro());
            text = text.replaceAll("@Empresa.getComplementoLogradouro@", processo.getEmpresaCnpj().getComplementoLogradouro());
        }

        if (text.contains("@Processo")) {
            text = text.replaceAll("@Processo.getSicap@", processo.getSicap());
            text = text.replaceAll("@Processo.getFistel@", processo.getFistel());
            text = text.replaceAll("@Processo.getSitar@", processo.getSitar());
        }

        if (text.contains("@Procurador")) {
            text = text.replaceAll("@Procurador.getProcuradorCpf@", procurador.getProcuradorCpf());
            text = text.replaceAll("@Procurador.getNome@", procurador.getNome());
            text = text.replaceAll("@Procurador.getCargo@", procurador.getCargo());
            text = text.replaceAll("@Procurador.getPronome@", procurador.getPronome());
            text = text.replaceAll("@Procurador.getSexo@", procurador.getSexo().toString());
        }

        if (text.contains("@Procuracao")) {
            String validade = "SEM DATA DE VALIDADE";
            if (procurador.getProcuracaoCollection() != null) {
                Iterator<Procuracao> procuracoes = procurador.getProcuracaoCollection().iterator();
                while (procuracoes.hasNext()) {
                    Procuracao procuracao = procuracoes.next();
                    if (procuracao.getEmpresaCnpj().equals(processo.getEmpresaCnpj())) {
                        validade = DateFormat.getInstance().format(procuracao.getValidade());
                        break;
                    }
                }
            }
            text = text.replaceAll("@Procuracao.getValidade@", validade);
        }

        if (text.contains("@Servico")) {
            text = text.replaceAll("@Servico.getServicoNum@", processo.getServicoNum().getServicoNum());
            text = text.replaceAll("@Servico.getNome@", processo.getServicoNum().getNome());
            if (text.contains("@Servico.Usuario")) {
                text = text.replaceAll("@Servico.Usuario.getLogin@", processo.getServicoNum().getLogin().getLogin());
                text = text.replaceAll("@Servico.Usuario.getNome@", processo.getServicoNum().getLogin().getNome());
                text = text.replaceAll("@Servico.Usuario.getCargo@", processo.getServicoNum().getLogin().getCargo());
                text = text.replaceAll("@Servico.Usuario.getArea@", processo.getServicoNum().getLogin().getArea().toString());
                text = text.replaceAll("@Servico.Usuario.getSexo@", processo.getServicoNum().getLogin().getSexo().toString());
                text = text.replaceAll("@Servico.Usuario.getPerfilName@", Usuario.getPerfilName(processo.getServicoNum().getLogin().getPerfil()));
                text = text.replaceAll("@Servico.Usuario.getTelefone@", processo.getServicoNum().getLogin().getTelefone());
            }
        }

        if (text.contains("@Usuario")) {
            text = text.replaceAll("@Usuario.getLogin@", usuario.getLogin());
            text = text.replaceAll("@Usuario.getNome@", usuario.getNome());
            text = text.replaceAll("@Usuario.getCargo@", usuario.getCargo());
            text = text.replaceAll("@Usuario.getArea@", usuario.getArea().toString());
            text = text.replaceAll("@Usuario.getSexo@", usuario.getSexo().toString());
            text = text.replaceAll("@Usuario.getPerfilName@", Usuario.getPerfilName(usuario.getPerfil()));
            text = text.replaceAll("@Usuario.getTelefone@", usuario.getTelefone());
        }
        return text;
    }

    private Class getTableClass(String tableName) throws ClassNotFoundException {
        return Class.forName("entities." + tableName);
    }

    private Usuario getUsuario(String login) {
        return DAO.getInstance().find(Usuario.class, login);
    }

    private Field getPrimaryKeyField(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        Field primaryKeyField = null;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(Id.class) != null || fields[i].getAnnotation(EmbeddedId.class) != null) {
                primaryKeyField = fields[i];
                break;
            }
        }
        return primaryKeyField;
    }

    private Object getPrimaryKeyValue(Field primaryKeyField, HttpServletRequest request) {
        Object primaryKeyValue = null;
        if (primaryKeyField.getType().equals(String.class)) {
            primaryKeyValue = request.getParameter(primaryKeyField.getName());
        } else {
            try {
                primaryKeyValue = primaryKeyField.getType().newInstance();
                Field[] fields = primaryKeyField.getType().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    String fieldName = fields[i].getName();
                    if (!Modifier.isFinal(fields[i].getModifiers())) {
                        Method method;
                        if (fields[i].getType().equals(Date.class)) {
                            int day = Integer.parseInt(request.getParameter(fieldName).split("/")[0]);
                            int month = Integer.parseInt(request.getParameter(fieldName).split("/")[1]) - 1;
                            int year = Integer.parseInt(request.getParameter(fieldName).split("/")[2]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day, 00, 00, 00);
                            method = primaryKeyField.getType().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), Date.class);
                            method.invoke(primaryKeyValue, calendar.getTime());
                        } else {
                            method = primaryKeyField.getType().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), String.class);
                            method.invoke(primaryKeyValue, request.getParameter(fieldName));
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
        return primaryKeyValue;
    }

    private boolean existsEntity(Class tableClass, Field primaryKeyField, String primaryKeyValue) {
        Object entity = null;
        Column column = primaryKeyField.getAnnotation(Column.class);
        if (column != null) {
            if (column.nullable() && (primaryKeyValue == null || primaryKeyValue.length() == 0)) {
                return true;
            }
        } else {
            JoinColumn joinColumn = primaryKeyField.getAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                if (joinColumn.nullable() && (primaryKeyValue == null || primaryKeyValue.length() == 0)) {
                    return true;
                }
            }
        }
        String primaryKeyClassName = primaryKeyField.getType().getName();
        if (primaryKeyClassName.equals("java.lang.Integer")) {
            entity = DAO.getInstance().find(tableClass, Integer.parseInt(primaryKeyValue));
        } else if (primaryKeyClassName.equals("java.lang.Long")) {
            entity = DAO.getInstance().find(tableClass, Long.parseLong(primaryKeyValue));
        } else {
            entity = DAO.getInstance().find(tableClass, primaryKeyValue);
        }
        return entity != null;
    }

    private Object getEntityFromDB(Class tableClass, Field primaryKeyField, Object primaryKeyValue) {
        Object entity = null;
        String primaryKeyClassName = primaryKeyField.getType().getName();
        if (primaryKeyClassName.equals("java.lang.Integer")) {
            entity = DAO.getInstance().find(tableClass, Integer.parseInt("" + primaryKeyValue));
        } else if (primaryKeyClassName.equals("java.lang.Long")) {
            entity = DAO.getInstance().find(tableClass, Long.parseLong("" + primaryKeyValue));
        } else if (primaryKeyClassName.equals("java.lang.String")) {
            entity = DAO.getInstance().find(tableClass, "" + primaryKeyValue);
        } else {
            entity = DAO.getInstance().find(tableClass, primaryKeyValue);
        }
        return entity;
    }

    private Field[] getForeignKeyFields(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        Vector<Field> foreignKeyFields = new Vector<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(ManyToOne.class) != null) {
                foreignKeyFields.add(fields[i]);
            }
        }
        return foreignKeyFields.toArray(new Field[foreignKeyFields.size()]);
    }

    private <T> T getEntity(Class<T> tableClass, HttpServletRequest request) throws Exception {
        T entity = tableClass.newInstance();
        Field[] fields = tableClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            if (!Modifier.isFinal(fields[i].getModifiers())) {
                if (fields[i].getAnnotation(ManyToOne.class) != null) {//chave estrangeira

                    String foreignKeyValue = request.getParameter(fieldName);
                    if (foreignKeyValue != null && foreignKeyValue.length() > 0) {
                        Class foreignClass = fields[i].getType();
                        Object foreignObject = foreignClass.newInstance();
                        Field foreignPrimaryKeyField = getPrimaryKeyField(foreignClass);
                        foreignClass.getMethod("set" + foreignPrimaryKeyField.getName().substring(0, 1).toUpperCase() + foreignPrimaryKeyField.getName().substring(1, foreignPrimaryKeyField.getName().length()), new Class[]{foreignPrimaryKeyField.getType()}).invoke(foreignObject, foreignKeyValue);
                        tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{fields[i].getType()}).invoke(entity, foreignObject);
                    }
                } else if (fields[i].getAnnotation(OneToMany.class) != null) {//Collection
                } else if (fields[i].getAnnotation(ManyToMany.class) != null) {//Collection
                } else {
                    String fieldClassName = fields[i].getType().getName();
                    if (fieldClassName.equals("java.lang.Integer") || fieldClassName.equals("int")) {
                        tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{Integer.class}).invoke(entity, new Integer(request.getParameter(fieldName)));
                    } else if (fieldClassName.equals("java.lang.Long") || fieldClassName.equals("long")) {
                        tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{Long.class}).invoke(entity, Long.parseLong(request.getParameter(fieldName)));
                    } else {
                        Class fieldClass = Class.forName(fieldClassName);
                        if (fieldClass.isEnum()) {
                            Object enumConstant = fieldClass.getMethod("valueOf", new Class[]{String.class}).invoke(null, request.getParameter(fieldName));
                            tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{fieldClass}).invoke(entity, enumConstant);
                        } else if (fieldClass.equals(String.class)) {
                            if (fieldName.equals("senha") && request.getParameter("method").equals("incluir")) {
                                tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{String.class}).invoke(entity, DAO.getInstance().getCryptedPassword(request.getParameter(fieldName)));
                            } else {
                                tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{String.class}).invoke(entity, request.getParameter(fieldName));
                            }
                        } else if (fieldClass.equals(Date.class)) {
                            if (!request.getParameter(fieldName).isEmpty()) {
                                int day = Integer.parseInt(request.getParameter(fieldName).split("/")[0]);
                                int month = Integer.parseInt(request.getParameter(fieldName).split("/")[1]) - 1;
                                int year = Integer.parseInt(request.getParameter(fieldName).split("/")[2]);
                                Calendar calendar = Calendar.getInstance();
                                if (fieldName.startsWith("inicio")) {
                                    calendar.set(year, month, day, 00, 00, 00);
                                } else {
                                    calendar.set(year, month, day, 23, 59, 59);
                                }
                                tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{Date.class}).invoke(entity, new Date(calendar.getTimeInMillis()));
                            }
                        } else {
                            tableClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length()), new Class[]{fieldClass}).invoke(entity, getEntity(fieldClass, request));
                        }
                    }
                }
            }
        }
        return entity;
    }

    private boolean isUserAuthorized(String login, String method, Class tableClass, Object primaryKeyValue) {
        Usuario usuario = getUsuario(login);
        switch (usuario.getPerfil()) {
            case ADMINISTRADOR:
                return true;//pode tudo

            case USUARIO_MOR:
                if (method.equals("incluir") || method.equals("excluir")) {
                    return !tableClass.equals(Usuario.class);//não pode incluir nem excluir usuário

                } else if (method.equals("alterar") && tableClass.equals(Usuario.class)) {
                    return ("" + primaryKeyValue).equals(usuario.getLogin());//só pode alterar ele mesmo

                } else {
                    return true;
                }
            case USUARIO_SENIOR:
                if (method.equals("incluir") || method.equals("excluir")) {
                    return !tableClass.equals(Usuario.class);//não pode incluir nem excluir usuário

                } else if (method.equals("alterar") && tableClass.equals(Usuario.class)) {
                    return ("" + primaryKeyValue).equals(usuario.getLogin());//só pode alterar ele mesmo

                } else if (method.equals("documentoExcluir")) {
                    return false;
                } else {
                    return true;
                }
            case USUARIO_JUNIOR:
                if (method.equals("incluir") || method.equals("excluir")) {
                    return !tableClass.equals(Usuario.class);//não pode incluir nem excluir usuário

                } else if (method.equals("alterar") && tableClass.equals(Usuario.class)) {
                    return ("" + primaryKeyValue).equals(usuario.getLogin());//só pode alterar ele mesmo

                } else if (method.startsWith("documento")) {
                    return false;
                } else {
                    return true;
                }
            case CONVIDADO:
                if (method.equals("incluir") || method.equals("excluir")) {
                    return !tableClass.equals(Usuario.class);//não pode incluir nem excluir usuário

                } else if (method.equals("alterar") && tableClass.equals(Usuario.class)) {
                    return ("" + primaryKeyValue).equals(usuario.getLogin());//só pode alterar ele mesmo

                } else if (method.startsWith("documento")) {
                    return false;
                } else {
                    return true;
                }
            default:
                return false;//por default, não pode nada

        }
    }

    private String isValidFields(HttpServletRequest request, HttpServletResponse response) {
        //TODO: validar dados
        return "";
    }

    public String autoDistribute(HttpServletRequest request, HttpServletResponse response) {
        String[] sicaps = request.getParameterValues("sicap");
        String[] respostas = request.getParameterValues("resposta");
        String[] filas = request.getParameterValues("fila");
        String[] destinatarios = request.getParameterValues("destinatario");
        String destiny = "";
        if (sicaps.length == respostas.length && respostas.length == filas.length && filas.length == destinatarios.length) {
            for (int i = 0; i < sicaps.length; i++) {
                destiny += findDestiny(sicaps[i], respostas[i], filas[i], destinatarios[i]);
            }
        }
        return destiny;
    }

    public String findDestiny(String sicap, String resposta, String fila, String destinatario) {
        //TODO: algoritmo de distribuição
        return "";
    }
}
