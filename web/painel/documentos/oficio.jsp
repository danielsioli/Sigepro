<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="entities.Oficio" %>
<%@ page import="entities.Paragrafo" %>
<%@ page import="dao.DAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="entities.Usuario" %>
<%@ page import="entities.Procuracao" %>
<%@ page import="entities.Procurador" %>
<%@ page import="entities.Processo" %>
<%@ page import="javax.security.auth.Subject" %>
<jsp:useBean id="hashtable" scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="processo" class="entities.Processo" scope="session"/>
<%
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);

            Usuario usuario = ((Usuario) ((Subject) session.getAttribute("javax.security.auth.subject")).getPrincipals().iterator().next());
            int oficioId = Integer.parseInt(request.getParameter("modelo"));
            int oficioBaseId = Integer.parseInt(request.getParameter("base"));
            Oficio oficio = null;
            Oficio oficioBase = null;
            if (oficioId != -1) {
                oficio = DAO.getInstance().find(Oficio.class, oficioId);
            } else {
                if (oficioBaseId == 0) {
                    oficio = new Oficio();
                } else {
                    oficioBase = DAO.getInstance().find(Oficio.class, oficioBaseId);
                    oficio = DAO.getInstance().find(Oficio.class, oficioBaseId);
                    oficio.setOficioId(oficioId);
                    oficio.setNome(null);
                }
            }
%>
<br>
<form>
    <table>
        <td>
            <button type="button" id="gerar" name="gerar" onclick="addEstatistica('Oficio',document.getElementById('oficioId').value);return showDocument('/docs/oficio/oficio.jsp','<%=usuario.getLogin()%>','oficio','gerar')" <%= oficioId == -1 ? "disabled" : ""%>>Gerar</button>
        </td>
        <td>
            <button type="button" id="incluir" name="incluir" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Incluir','oficio')" <%= oficioId != -1 ? "disabled" : ""%>>Incluir</button>
        </td>
        <td>
            <button type="button" id="excluir" name="excluir" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Excluir','oficio')" <%= oficioId == -1 ? "disabled" : ""%>>Excluir</button>
        </td>
        <td>
            <button type="button" id="alterar" name="alterar" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Alterar','oficio')" <%= oficioId == -1 ? "disabled" : ""%>>Alterar</button>
        </td>
    </table>
    
    <input type=text id=oficioId name=oficioId value="<%=oficioId%>" style='display:none'>
    
    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" size="35" maxlength="30" nullable="false" key="" value="<%= oficio.getNome() != null ? oficio.getNome() : ""%>">
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <%
            Cookie cookies[] = request.getCookies();
            Cookie lastSicapCookie = null;
            Cookie currentSicapCookie = null;
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("last_sicap")) {
                        lastSicapCookie = cookies[i];
                    } else if (cookies[i].getName().equals("current_sicap")) {
                        currentSicapCookie = cookies[i];
                    }
                }
            }
            if (currentSicapCookie != null) {
                if (processo == null) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                } else if (processo.getSicap() == null) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                } else if (!processo.getSicap().equals(currentSicapCookie.getValue())) {
                    processo = DAO.getInstance().find(Processo.class, currentSicapCookie.getValue());
                }
            } else if (lastSicapCookie != null) {
                if (processo == null) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                } else if (processo.getSicap() == null) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                } else if (!processo.getSicap().equals(lastSicapCookie.getValue())) {
                    processo = DAO.getInstance().find(Processo.class, lastSicapCookie.getValue());
                }
            }
            if (oficioId != -1) {
                if (processo != null && processo.getSicap() != null) {
                    processo = DAO.getInstance().find(Processo.class, processo.getSicap());
                    Collection<Procuracao> procuracoes = processo.getEmpresaCnpj().getProcuracaoCollection();
    %>
    
    <label for="procurador">Procurador:</label>
    <select id="procurador" name="procurador" onchange="if(this.value == 'digitar'){this.style.display = 'none';document.getElementById('digitar').style.display = 'block'}" style="display:<%=procuracoes.size() == 0 ? "none" : "block"%>">
        <%
                    if (procuracoes != null && procuracoes.size() > 0) {
                        Iterator<Procuracao> procuracoesIterator = procuracoes.iterator();
                        while (procuracoesIterator.hasNext()) {
                            Procurador procurador = procuracoesIterator.next().getProcuradorCpf();
        %>
        <option value="<%=procurador.getProcuradorCpf()%>"><%=procurador.getNome()%></option>
        <%
                        }
                    }

        %>
        <option value="digitar">Digitar</option>
    </select>
    <div id=digitar style="display:<%=procuracoes.size() > 0 ? "none" : "block"%>">
        <div id=escolherProcuradorDiv style="display:<%=procuracoes.size() == 0 ? "none" : "block"%>">
            <input type=checkbox id=escolherProcurador name=escolherProcurador onclick="if(this.checked){this.checked = false;document.getElementById('digitar').style.display='none';document.getElementById('procurador').selectedIndex = 0;document.getElementById('procurador').style.display = 'block'}">Escolher Procurador</input>
            <br />
        </div>
        <label for="procuradorPronome">Pronome para o Procurador:</label>
        <input type=text id=procuradorPronome name=procuradorPronome />
        
        <label for="procuradorNome">Nome do Procurador:</label>
        <input type=text id=procuradorNome name=procuradorNome />
        
        <label for="procuradorCargo">Cargo do Procurador:</label>
        <input type=text id=procuradorCargo name=procuradorCargo />
        
        <label for="procuradorSexo">Cargo do Procurador:</label>
        <select id=procuradorSexo name=procuradorSexo>
            <option value="HOMEM" selected>HOMEM</option>
            <option value="MULHER">MULHER</option>
        </select>
    </div>
    <br />
    <%
                }
            }
    %>
    
    <label for="login">Assinante:</label>
    <%
            hashtable.clear();
            List<Usuario> usuarios = DAO.getInstance().findByNamedQuery(Usuario.class, "Usuario.findAll", hashtable);

    %>
    <select id="login" name="login" nullable="false" key="PRI">
        <option value='' <%=request.getParameter("login") == null ? "selected" : ""%>></option>
        <%
            for (int j = 0; j < usuarios.size(); j++) {
        %>
        <option value="<%=usuarios.get(j).getLogin()%>" <%=oficio.getLogin() != null && usuarios.get(j).equals(oficio.getLogin()) ? "selected" : ""%>><%=usuarios.get(j).getLogin() + " - " + usuarios.get(j).getNome()%></option>
        <%
            }
        %>
    </select>
    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
    
    <label for="publico">Ofício Público:</label>
    <select id=publico name=publico nullable=false <%=oficio.getDono() == null ? "" : (oficio.getDono().equals(usuario) ? "" : "disabled")%>>
        <option value=1 <%=oficio.getDono() == null ? "selected" : (oficio.getPublico() == 1 ? "selected" : "")%>>Sim</option>
        <option value=0 <%=oficio.getDono() != null && oficio.getPublico() == 0 ? "selected": ""%>>Não</option>
    </select>
    
    <label for=dono>Dono do Ofício:</label>
    <input type=text id=dono name=dono disabled value='<%=oficio.getOficioId() != -1 ? oficio.getDono().getLogin() : usuario.getLogin()%>'/>
    
    <label for="assunto">Assunto:</label>
    <table id="assuntoTable">
        <tr>
            <td>
                <textarea id="assunto" name="assunto" cols=56 rows=5><%=oficio.getAssunto() != null ? oficio.getAssunto() : ""%></textarea>
                <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
            </td>
            <td valign=top>
                <input type="checkbox" id="assuntoRichText" name="assuntoRichText" onclick="if(this.checked){getRichText('assunto');}else{hideThisRichText('assunto')}">Rich Text</input>
            </td>
        </tr>
    </table>
    
    <br />
    
    <input type="checkbox" id="showParagrafos1" name="showParagrafos1" <%=oficioId == -1 ? "checked" : ""%> onclick="if(this.checked){document.getElementById('showParagrafos2').checked = true;document.getElementById('paragrafos').style.display='block';}else{document.getElementById('showParagrafos2').checked = false;document.getElementById('paragrafos').style.display='none';}">Mostrar Parágrafos</input>
    <div id="paragrafos" style="display:<%=oficioId == -1 ? "block" : "none"%>">
        <%
            hashtable.clear();
            hashtable.put("oficioId", oficioBase != null ? oficioBase : oficio);
            Collection<Paragrafo> paragrafos = DAO.getInstance().findByNamedQuery(Paragrafo.class, "Paragrafo.findByOficioId", hashtable);
            if (paragrafos != null && paragrafos.size() > 0) {
                Iterator<Paragrafo> paragrafosIterator = paragrafos.iterator();
                int i = 1;
                while (paragrafosIterator.hasNext()) {
                    Paragrafo paragrafo = paragrafosIterator.next();
                    if (paragrafo.getParagrafoPrincipal() == null) {
        %>
        <label for="paragrafo_<%=i%>.">Parágrafo <%=i%>.</label>
        <table id="paragrafo_<%=i%>.Table">
            <tr>
                <td>
                    <textarea id="paragrafo_<%=i%>." name="paragrafo_<%=i%>." cols=56 rows=5 paragrafoId="<%=paragrafo.getParagrafoId() != null ? paragrafo.getParagrafoId() : ""%>"><%=paragrafo.getCorpo() != null ? paragrafo.getCorpo() : ""%></textarea>
                    <%if (i == 1) {%><img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório."><%}%>
                </td>
                <td valign=top>
                    <input type="checkbox" id="paragrafo_<%=i%>.RichText" name="paragrafo_<%=i%>.RichText" onclick="if(this.checked){getRichText('paragrafo_<%=i%>.');}else{hideThisRichText('paragrafo_<%=i%>.')}">Rich Text</input>
                    <select id="paragrafo_<%=i%>.MoreActions" name="paragrafo_<%=i%>.MoreActions" onchange="return moreActions('paragrafo_<%=i%>.',this.value);">
                        <option value='' style="color:silver">Mais Ações</option>
                        <option value='addParagrafoBefore'>+ Parágrafo Antes</option>
                        <option value='addParagrafoAfter'>+ Parágrafo Depois</option>
                        <option value='addSubParagrafo'>+ Sub Parágrafo</option>
                        <%if (i != 1) {%>
                        <option value='removeParagrafo'>- Este Parágrafo</option>
                        <%}%>
                    </select>
                </td>
            </tr>
        </table>
        <%
                    hashtable.clear();
                    hashtable.put("oficioId", oficioBase != null ? oficioBase : oficio);
                    hashtable.put("paragrafoPrincipal", paragrafo);
                    Collection<Paragrafo> subParagrafos = DAO.getInstance().findByNamedQuery(Paragrafo.class, "Paragrafo.findByOficioIdAndParagrafoPrincipalId", hashtable);
                    if (subParagrafos != null && subParagrafos.size() > 0) {
                        Iterator<Paragrafo> subParagrafosIterator = subParagrafos.iterator();
                        int j = 1;
                        while (subParagrafosIterator.hasNext()) {
                            Paragrafo subParagrafo = subParagrafosIterator.next();
        %>
        <label for="subParagrafo_<%=i%>.<%=j%>.">Sub Parágrafo <%=i%>.<%=j%>.</label>
        <table id="subParagrafo_<%=i%>.<%=j%>.Table">
            <tr>
                <td>
                    <textarea id="subParagrafo_<%=i%>.<%=j%>." name="subParagrafo_<%=i%>.<%=j%>." cols=56 rows=5 paragrafoId="<%=subParagrafo.getParagrafoId() != null ? subParagrafo.getParagrafoId() : ""%>"><%=subParagrafo.getCorpo() != null ? subParagrafo.getCorpo() : ""%></textarea>
                </td>
                <td valign=top>
                    <input type="checkbox" id="subParagrafo_<%=i%>.<%=j%>.RichText" name="subParagrafo_<%=i%>.<%=j%>.RichText" onclick="if(this.checked){getRichText('subParagrafo_<%=i%>.<%=j%>.');}else{hideThisRichText('subParagrafo_<%=i%>.<%=j%>.')}">Rich Text</input>
                    <select id="subParagrafo_<%=i%>.<%=j%>.MoreActions" name="subParagrafo_<%=i%>.<%=j%>.MoreActions" onchange="return moreActions('subParagrafo_<%=i%>.<%=j%>.',this.value);">
                        <option value='' style="color:silver">Mais Ações</option>
                        <option value='addParagrafoAfter'>+ Parágrafo Depois</option>
                        <option value='addSubParagrafoBefore'>+ Sub Parágrafo Antes</option>
                        <option value='addSubParagrafoAfter'>+ Sub Parágrafo Depois</option>
                        <option value='addItem'>+ Item</option>
                        <option value='removeSubParagrafo'>- Este Sub Parágrafo</option>
                    </select>
                </td>
            </tr>
        </table>
        <%
                hashtable.clear();
                hashtable.put("oficioId", oficioBase != null ? oficioBase : oficio);
                hashtable.put("paragrafoPrincipal", subParagrafo);
                Collection<Paragrafo> itens = DAO.getInstance().findByNamedQuery(Paragrafo.class, "Paragrafo.findByOficioIdAndParagrafoPrincipalId", hashtable);
                if (itens != null && itens.size() > 0) {
                    Iterator<Paragrafo> itensIterator = itens.iterator();
                    char c = 'a';
                    while (itensIterator.hasNext()) {
                        Paragrafo item = itensIterator.next();
        %>
        <label for="item_<%=i%>.<%=j%>.<%=c%>">Item <%=i%>.<%=j%>.<%=c%>)</label>
        <table id="item_<%=i%>.<%=j%>.<%=c%>Table">
            <tr>
                <td>
                    <textarea id="item_<%=i%>.<%=j%>.<%=c%>" name="item_<%=i%>.<%=j%>.<%=c%>"  cols=56 rows=5 paragrafoId="<%=item.getParagrafoId() != null ? item.getParagrafoId() : ""%>"><%=item.getCorpo() != null ? item.getCorpo() : ""%></textarea>
                </td>
                <td valign=top>
                    <input type="checkbox" id="item_<%=i%>.<%=j%>.<%=c%>RichText" name="item_<%=i%>.<%=j%>.<%=c%>RichText" onclick="if(this.checked){getRichText('item_<%=i%>.<%=j%>.<%=c%>');}else{hideThisRichText('item_<%=i%>.<%=j%>.<%=c%>')}">Rich Text</input>
                    <select id="item_<%=i%>.<%=j%>.<%=c%>MoreActions" name="item_<%=i%>.<%=j%>.<%=c%>MoreActions" onchange="return moreActions('item_<%=i%>.<%=j%>.<%=c%>',this.value);">
                        <option value='' style="color:silver">Mais Ações</option>
                        <option value='addSubParagrafoAfter'>+ Sub Parágrafo Depois</option>
                        <option value='addItemBefore'>+ Item Antes</option>
                        <option value='addItemAfter'>+ Item Depois</option>
                        <option value='removeItem'>- Este Item</option>
                    </select>
                </td>
            </tr>
        </table>
        <%
                                    c++;
                                }
                            }
                            j++;
                        }
                    }
                    i++;
                }
            }
        } else {
        %>
        <label for="paragrafo_1.">Parágrafo 1.</label>
        <table id="paragrafo_1.Table">
            <tr>
                <td>
                    <textarea id="paragrafo_1." name="paragrafo_1." cols=56 rows=5 paragrafoId=""></textarea>
                    <img src="${pageContext.request.contextPath}/img/watched_true.gif" alt="Indica que o campo é de preenchimento obrigatório.">
                </td>
                <td valign=top>
                    <input type="checkbox" id="paragrafo_1.RichText" name="paragrafo_1.RichText" onclick="if(this.checked){getRichText('paragrafo_1.');}else{hideThisRichText('paragrafo_1.')}">Rich Text</input>
                    <select id="paragrafo_1.MoreActions" name="paragrafo_1.MoreActions" onchange="return moreActions('paragrafo_1.',this.value);">
                        <option value='' style="color:silver">Mais Ações</option>
                        <option value='addParagrafoBefore'>+ Parágrafo Antes</option>
                        <option value='addParagrafoAfter'>+ Parágrafo Depois</option>
                        <option value='addSubParagrafo'>+ Sub Parágrafo</option>
                    </select>
                </td>
            </tr>
        </table>
        <%            }
        %>
        <input type="checkbox" id="showParagrafos2" name="showParagrafos2" <%=oficioId == -1 ? "checked" : ""%> onclick="if(this.checked){document.getElementById('showParagrafos1').checked = false;document.getElementById('paragrafos').style.display='block';}else{document.getElementById('showParagrafos1').checked = false;document.getElementById('paragrafos').style.display='none';}">Mostrar Parágrafos</input>
    </div>
    
    <br />
    <br />
    
    <table>
        <td>
            <button type="button" id="gerar" name="gerar" onclick="addEstatistica('Oficio',document.getElementById('oficioId').value);return showDocument('/docs/oficio/oficio.jsp','<%=usuario.getLogin()%>','oficio','gerar')" <%= oficioId == -1 ? "disabled" : ""%>>Gerar</button>
        </td>
        <td>
            <button type="button" id="incluir" name="incluir" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Incluir','oficio')" <%= oficioId != -1 ? "disabled" : ""%>>Incluir</button>
        </td>
        <td>
            <button type="button" id="excluir" name="excluir" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Excluir','oficio')" <%= oficioId == -1 ? "disabled" : ""%>>Excluir</button>
        </td>
        <td>
            <button type="button" id="alterar" name="alterar" onclick="return workWithDocument('<%=request.getContextPath()%>/control.view','Alterar','oficio')" <%= oficioId == -1 ? "disabled" : ""%>>Alterar</button>
        </td>
    </table>
</form>