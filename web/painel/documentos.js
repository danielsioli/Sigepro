/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function initRequest() {
    var httpRequest = null;
    try {  // Firefox, Opera 8.0+, Safari
        httpRequest = new XMLHttpRequest();
    } catch (e) { // Internet Explorer
        try {
            httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {
                showAlert("Your browser does not support AJAX!");
            }
        }
    }
    return httpRequest;
}

function addOption(select,newOption){
    var url = "/sigepro/control.view?method=getProcesso&sicap=" + newOption;
    var httpRequest = initRequest();
    setDisabledButtons(true);
    showWaitMessage(1);
    //Enviar solicitação ao servidor
    httpRequest.onreadystatechange = function() {
        if (httpRequest.readyState == 4) {
            setDisabledButtons(false);
            if (httpRequest.status == 200) {
                if(parseMessages(httpRequest,'addProcesso')){
                    var option = new Option(newOption,newOption);
                    option.selected = true;
                    var length = document.getElementById(select).options.length;
                    if(length == 1 && document.getElementById(select).options[length - 1].value == ''){
                        document.getElementById(select).options[length - 1] = option;
                    }else{
                        document.getElementById(select).options[length] = option;
                    }
                }
                setDisabledButtons(false);
            } else if (httpRequest.status == 204){
                showMessage("Servidor não respondeu adequadamente.",60000,"failure");
                setDisabledButtons(false);
            }
        }
    };
    httpRequest.open("POST", url, true);
    httpRequest.send(null);
}

function removeOption(select){
    var options = document.getElementById(select).options;
    if(options){
        for(var i = 0, option; option = options[i]; i++){
            if(option.selected){
                document.getElementById(select).remove(i);
                i--;
            }
        }
        if(options.length == 0){
            document.getElementById(select).options[0] = new Option('Processos aqui!!!!','');
        }
    }
}

function showDocument(url,login,tipo,action){
    hideRichText();
    removeRichText();
    var msg = validate(tipo);
    if(msg.length == 0){
        var html = "<html><head></head><body><form id='formid' method='post' action='" + url + "'>";
        html += "<input type='hidden' name='login' value='" + login + "'/>";
        if(tipo == 'oficio'){
            html += "<input type='hidden' name='method' value='" + action + "'/>";
            html += prepareDocForm(html);
        }else if(tipo=='ar' || tipo=='etiqueta'){
            var options = document.getElementById("processos").options;
            if(options){
                if(options[0].value == ''){
                    alert("Nenhum processo selecionado");
                    return;
                }
                for(var i = 0, option; option = options[i]; i++){
                    html += "<input type='hidden' name='processos' value='" + option.value + "'/>";
                }
            }
        }
        if(document.getElementById('processo')){
            var processo = document.getElementById('processo');
            if(processo.value != ""){
                html += "<input type='hidden' name='processo' value='" + processo.value + "'/>";
                if(document.getElementById('procurador') && document.getElementById('procurador').style.display == "block"){
                    html += "<input type='hidden' name='procurador' value='" + document.getElementById('procurador').value + "'/>";
                }else if(document.getElementById('digitar')){
                    html += "<input type='hidden' name='procuradorNome' value='" + escape(document.getElementById('procuradorNome').value) + "'/>";
                    html += "<input type='hidden' name='procuradorPronome' value='" + escape(document.getElementById('procuradorPronome').value) + "'/>";
                    html += "<input type='hidden' name='procuradorCargo' value='" + escape(document.getElementById('procuradorCargo').value) + "'/>";
                    html += "<input type='hidden' name='procuradorSexo' value='" + escape(document.getElementById('procuradorSexo').value) + "'/>";
                }
                html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
                var documentWindow = window.open('',tipo);
                documentWindow.document.write(html);
            }else{
                if(tipo != "ar" && tipo != 'etiqueta'){
                    alert("Processo não selecionado");
                }else{
                    html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
                    var documentWindow = window.open('',tipo);
                    documentWindow.document.write(html);
                }
            }
        }else{
            alert("Processo nao encontrado");
        }
    }
}

function workWithDocument(url,action,tipo){
    var confirmation = true;
    var params = '';
    if(action == 'Excluir' || action == 'Alterar'){
        confirmation = confirm("Confirma operação?");
    }
    if(confirmation){
        hideRichText();
        removeRichText();
        var msg = validate(tipo);
        if(msg.length == 0){
            params += 'method=documento' + action + '&tipo=' + tipo;
            if(tipo == 'oficio'){
                params = workWithOficio(params,action);
            }
            var httpRequest = initRequest();
            showWaitMessage(1);
            if(httpRequest != null){
                httpRequest.onreadystatechange = function() {
                    if (httpRequest.readyState == 4) {
                        if (httpRequest.status == 200) {
                            updateModeloDocumento(httpRequest,action,tipo);
                        }
                    }
                };
                httpRequest.open("POST", url, true);
                httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                httpRequest.setRequestHeader("Content-length", params.length);
                httpRequest.setRequestHeader("Connection", "close");
                httpRequest.send(params);
            }
        }else{
            alert(msg);
        }
    }
}

function validate(tipo){
    var msg = "";
    if(tipo == 'oficio'){
        if(!document.getElementById('oficioId') || document.getElementById('oficioId').value == ''){
            msg += "Ofício não selecionado\n";
        }
        if(!document.getElementById('nome') || document.getElementById('nome').value == ""){
            msg += "Nome do ofício não preenchido\n";
        }
        if(!document.getElementById('login') || document.getElementById('login').value == ""){
            msg += "Assinante não selecionado\n";
        }
        var textareas = document.getElementsByTagName('textarea');
        for (var i = 0, textarea; textarea = textareas[i]; i++) {
            if(textarea.innerHTML == ""){
                if(textarea.id != 'assunto'){
                    msg += "Corpo de " + textarea.id + " não preenchido\n";
                }else{
                    msg += "Assunto não preenchido\n";
                }
            }
        }
    }
    return msg;
}

function prepareDocForm(html){
    html += "<input type='hidden' name='oficioId' value='" + document.getElementById('oficioId').value + "'/>";
    html += "<input type='hidden' name='assinante' value='" + document.getElementById('login').value + "'/>";
    var textareas = document.getElementsByTagName('textarea');
    for (var i = 0, textarea; textarea = textareas[i]; i++) {
        var text = '';
        text = escape(textarea.innerHTML);
        html += "<input type='hidden' name='" + textarea.id + "' value='" + text + "'/>";
    }
    return html;
}

function workWithOficio(url,action){
    url += '&oficioId=' + document.getElementById('oficioId').value;
    if(action != 'Excluir'){
        url += '&nome=' + escape(document.getElementById('nome').value);
        url += '&assinante=' + document.getElementById('login').value;
        url += '&dono=' + document.getElementById('dono').value;
        url += '&publico=' + document.getElementById('publico').value;
        var textareas = document.getElementsByTagName('textarea');
        for (var i = 0, textarea; textarea = textareas[i]; i++) {
            url += '&' + textarea.id + "_id=" + textarea.paragrafoId;
            var text = '';
            text = escape(textarea.innerHTML);
            url += '&' + textarea.id + "=" + text;
        }
    }
    return url;
}

function updateModeloDocumento(httpRequest, action, tipo){
    var items = httpRequest.responseXML.getElementsByTagName("response")[0];
    var transactionOk = null;
    var message = null;
    if(items != null){
        if(items.childNodes[0].getElementsByTagName("name")[0].childNodes[0].nodeValue == "transactionOk"){
            transactionOk = items.childNodes[0].getElementsByTagName("value")[0].childNodes[0].nodeValue;
        }
        if(items.childNodes[1].getElementsByTagName("name")[0].childNodes[0].nodeValue == "message"){
            message = items.childNodes[1].getElementsByTagName("value")[0].childNodes[0].nodeValue;
        }
        if((transactionOk != null && (transactionOk == "true" || transactionOk == "false")) && message != null){
            showMessage(message,60000,transactionOk == "true" ? "success":"failure");
        }else{
            showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
            return false;
        }
    }else{
        showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
        return false;
    }
    if(transactionOk == "true"){
        if(action == 'Incluir'){
            if(items.childNodes[2].getElementsByTagName("name")[0].childNodes[0].nodeValue == "oficioId" && items.childNodes[3].getElementsByTagName("name")[0].childNodes[0].nodeValue == "oficioNome"){
                var select = document.getElementById('select_' + tipo);
                var selectBase = document.getElementById('select_base_' + tipo);
                var newModelo = document.createElement('option');
                var newModeloBase = document.createElement('option');
                newModelo.value = items.childNodes[2].getElementsByTagName("value")[0].childNodes[0].nodeValue;
                newModelo.text = items.childNodes[3].getElementsByTagName("value")[0].childNodes[0].nodeValue;
                
                newModeloBase.value = items.childNodes[2].getElementsByTagName("value")[0].childNodes[0].nodeValue;
                newModeloBase.text = items.childNodes[3].getElementsByTagName("value")[0].childNodes[0].nodeValue;
                
                var elOptOld = select.options[select.selectedIndex];  
                try {
                    select.add(newModelo, elOptOld); // standards compliant; doesn't work in IE
                    selectBase.add(newModeloBase,null);
                }
                catch(ex) {
                    select.add(newModelo, select.selectedIndex); // IE only
                    selectBase.add(newModeloBase);
                }
            }
            clearScreen(tipo);
        }else if(action == 'Excluir'){
            if(items.childNodes[2].getElementsByTagName("name")[0].childNodes[0].nodeValue == "oficioId"){
                var select = document.getElementById('select_' + tipo);
                var selectBase = document.getElementById('select_base_' + tipo);
                var options = select.options;
                for(var i = 0, option; option = options[i]; i++){
                    if(option.selected){
                        select.remove(i);
                        selectBase.remove(i);
                        break;
                    }
                }
            }
            clearScreen(tipo);
        }
        return true;
    }else{
        return false;
    }
}

function clearScreen(tipo){
    if(tipo == 'oficio'){
        document.getElementById('nome').value = "";
        document.getElementById('assunto').value = "";
        document.getElementById('oficioId').value = "";
        document.getElementById('login').selectedIndex = 0;
        document.getElementById('paragrafos').innerHTML = createParagrafo('paragrafo',1,1,'a',"","");
        document.getElementById('paragrafos').innerHTML += "<input type='checkbox' id='showParagrafos2' name='showParagrafos2' checked onclick='if(this.checked){document.getElementById(\"showParagrafos1\").checked = false;document.getElementById(\"paragrafos\").style.display=\"block\";}else{document.getElementById(\"showParagrafos1\").checked = false;document.getElementById(\"paragrafos\").style.display=\"none\";}'>Mostrar Parágrafos</input>";
        if(document.getElementById('procurador') != null){
            document.getElementById('procurador').style.display = "block";
            document.getElementById('procurador').selectedIndex = 0;
            document.getElementById('digitar').style.display = "none";
            document.getElementById('procuradorPronome').value = "";
            document.getElementById('procuradorNome').value = "";
            document.getElementById('procuradorCargo').value = "";
            document.getElementById('procuradorSexo').selectedIndex = 0;
        }
        document.getElementById('select_base_' + tipo).selectedIndex = 0;
        document.getElementById('select_' + tipo).selectedIndex = 0;
    }
}

function getDocumentScreen(modelo,base){
    removeRichText();
    if(modelo == -1 && base == 0){
        document.getElementById('telaDocumento').style.display = 'none';
        document.getElementById('base_oficio').style.display = "block";
    }
    if(modelo != -1){
        document.getElementById('select_base_oficio').selectedIndex = 0;
        document.getElementById('base_oficio').style.display = "none";
    }
    var url = '/sigepro/painel/documentos/oficio.jsp';
    var params = 'modelo=' + modelo + '&base=' + base;
    var telaDocumento = document.getElementById('telaDocumento');
    telaDocumento.innerHTML = "";
    var httpRequest = initRequest();
    if(httpRequest != null){
        httpRequest.onreadystatechange = function() {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    telaDocumento.innerHTML = httpRequest.responseText;
                    document.getElementById('telaDocumento').style.display = 'block';
                    createPlugins();
                }
            }
        };
        httpRequest.open("POST", url, true);
        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        httpRequest.setRequestHeader("Content-length", params.length);
        httpRequest.setRequestHeader("Connection", "close");
        httpRequest.send(params);
    }
}

function getARScreen(){
    var url = '/sigepro/painel/documentos/ar.jsp';
    var telaDocumento = document.getElementById('telaDocumento');
    telaDocumento.innerHTML = "";
    var httpRequest = initRequest();
    if(httpRequest != null){
        httpRequest.onreadystatechange = function() {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                    telaDocumento.innerHTML = httpRequest.responseText;
                    document.getElementById('telaDocumento').style.display = 'block';
                    createPlugins();
                }
            }
        };
        httpRequest.open("POST", url, true);
        httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        httpRequest.setRequestHeader("Content-length", 0);
        httpRequest.setRequestHeader("Connection", "close");
        httpRequest.send(null);
    }
}

function moreActions(paragrafo,action){
    document.getElementById(paragrafo + 'MoreActions').selectedIndex = 0;
    hideRichText();
    removeRichText();
    var paragrafosDiv = document.getElementById('paragrafos');
    var paragrafos = paragrafosDiv.getElementsByTagName('textarea');
    var a = 1;
    var b = 1;
    var c = 97; // caracter 'a'
    var html = "";
    for (var i = 0, currentParagrafo; currentParagrafo = paragrafos[i]; i++) {
        if(currentParagrafo.id.split('_')[0] == "paragrafo"){
            switch(action){
                case '':
                    break;
                case 'addParagrafoBefore':
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('paragrafo', a, b, c, "", "");
                        a++;
                    }
                    html += createParagrafo('paragrafo', a, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b = 1;
                    c = 97;
                    a++;
                    break;
                case 'addParagrafoAfter':
                    html += createParagrafo('paragrafo', a, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b = 1;
                    c = 97;
                    a++;
                    if(paragrafo == currentParagrafo.id){
                        if(i != paragrafos.length - 1){
                            var nextParagrafo = paragrafos[i+1];
                            while(nextParagrafo.id.split('_')[0] == "subParagrafo"){
                                i++;
                                html += createParagrafo("subParagrafo", a - 1, b, c, nextParagrafo.innerHTML, nextParagrafo.paragrafoId);
                                b++;
                                c = 97;
                                if(i != paragrafos.length - 1){
                                    var nextNextParagrafo = paragrafos[i+1];
                                    while(nextNextParagrafo.id.split('_')[0] == "item"){
                                        i++;
                                        html += createParagrafo("item", a - 1, b - 1, c, nextNextParagrafo.innerHTML, nextParagrafo.paragrafoId);
                                        c++;
                                        if(i != paragrafos.length - 1){
                                            nextNextParagrafo = paragrafos[i+1];
                                        }else{
                                            nextNextParagrafo.id = "foo_bar";//sair do while
                                        }
                                    }
                                }
                                if(i != paragrafos.length - 1){
                                    nextParagrafo = paragrafos[i+1];
                                }else{
                                    nextParagrafo.id = "foo_bar";//sair do while
                                }
                            }
                        }
                        html += createParagrafo('paragrafo', a, b, c, "");
                        b = 1;
                        c = 97;
                        a++;
                    }
                    break;
                case 'addSubParagrafo':
                    html += createParagrafo('paragrafo', a, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    a++;
                    b = 1;
                    c = 97;
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('subParagrafo', a - 1, b, c, "", "");
                        b++;
                    }
                    break;
                case 'removeParagrafo':
                    if(paragrafo != currentParagrafo.id){
                        html += createParagrafo('paragrafo', a, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                        a++;
                        b = 1;
                        c = 97;
                    }else{
                        if(i != paragrafos.length - 1){
                            var subParagrafo = paragrafos[i+1];
                            while(subParagrafo.id.split('_')[0] == "subParagrafo"){
                                i++;
                                if(i != paragrafos.length - 1){
                                    var item = paragrafos[i+1];
                                    while(item.id.split('_')[0] == "item"){
                                        i++;
                                        if(i != paragrafos.length - 1){
                                            item = paragrafos[i+1];
                                        }else{
                                            item.id = "foo_bar";//sair do while
                                        }
                                    }
                                }
                                if(i != paragrafos.length - 1){
                                    subParagrafo = paragrafos[i+1];
                                }else{
                                    subParagrafo.id = "foo_bar";//sair do while
                                }
                            }
                        }
                    }
                    break;
                default:
                    html += createParagrafo('paragrafo', a, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    a++;
                    b = 1;
                    c = 97;
            }
        }else if(currentParagrafo.id.split('_')[0] == "subParagrafo"){
            switch(action){
                case '':
                    break;
                case 'addParagrafoAfter':
                    html += createParagrafo('subParagrafo', a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b++;
                    c = 97;
                    if(paragrafo == currentParagrafo.id){
                        if(i != paragrafos.length - 1){
                            var nextSubParagrafo = paragrafos[i+1];
                            while(nextSubParagrafo.id.split('_')[0] == "subParagrafo"){
                                i++;
                                html += createParagrafo("subParagrafo", a - 1, b, c, nextSubParagrafo.innerHTML, nextSubParagrafo.paragrafoId);
                                b++;
                                c = 97;
                                if(i != paragrafos.length - 1){
                                    var nextNextSubParagrafo = paragrafos[i+1];
                                    while(nextNextSubParagrafo.id.split('_')[0] == "item"){
                                        i++;
                                        html += createParagrafo("item", a - 1, b - 1, c, nextNextSubParagrafo.innerHTML, nextNextSubParagrafo.paragrafoId);
                                        c++;
                                        if(i != paragrafos.length - 1){
                                            nextNextSubParagrafo = paragrafos[i+1];
                                        }else{
                                            nextNextSubParagrafo.id = "foo_bar";//sair do while
                                        }
                                    }
                                }
                                if(i != paragrafos.length - 1){
                                    nextSubParagrafo = paragrafos[i+1];
                                }else{
                                    nextSubParagrafo.id = "foo_bar";//sair do while
                                }
                            }
                        }
                        html += createParagrafo('paragrafo', a, b, c, "", "");
                        b = 1;
                        c = 97;
                        a++;
                    }
                    break;
                case 'addSubParagrafoBefore':
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('subParagrafo', a - 1, b, c, "", "");
                        b++;
                    }
                    html += createParagrafo("subParagrafo", a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b++;
                    c = 97;
                    break;
                case 'addSubParagrafoAfter':
                    html += createParagrafo('subParagrafo', a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b++;
                    c = 97;
                    if(paragrafo == currentParagrafo.id){
                        if(i != paragrafos.length - 1){
                            var nextItem = paragrafos[i+1];
                            while(nextItem.id.split('_')[0] == "item"){
                                i++;
                                html += createParagrafo("item", a - 1, b - 1, c, nextItem.innerHTML, nextItem.paragrafoId);
                                c++;
                                if(i != paragrafos.length - 1){
                                    nextItem = paragrafos[i+1];
                                }else{
                                    nextItem.id = "foo_bar";//sair do while
                                }
                            }
                        }
                        html += createParagrafo('subParagrafo', a - 1, b, c, "", "");
                        b++;
                        c = 97;
                    }
                    break;
                case 'addItem':
                    html += createParagrafo('subParagrafo', a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b++;
                    c = 97;
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('item', a - 1, b - 1, c, "", "");
                        c++;
                    }
                    break;
                case 'removeSubParagrafo':
                    if(paragrafo != currentParagrafo.id){
                        html += createParagrafo('subParagrafo', a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                        b++;
                        c = 97;
                    }else{
                        if(i != paragrafos.length - 1){
                            var item = paragrafos[i+1];
                            while(item.id.split('_')[0] == "item"){
                                i++;
                                if(i != paragrafos.length - 1){
                                    item = paragrafos[i+1];
                                }else{
                                    item.id = "foo_bar";//sair do while
                                }
                            }
                        }
                    }
                    break;
                default:
                    html += createParagrafo("subParagrafo", a - 1, b, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    b++;
                    c = 97;
            }
        }else if(currentParagrafo.id.split('_')[0] == "item"){
            switch(action){
                case '':
                    break;
                case 'addSubParagrafoAfter':
                    html += createParagrafo("item", a - 1, b - 1, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    c++;
                    if(paragrafo == currentParagrafo.id){
                        if(i != paragrafos.length - 1){
                            var nextItem = paragrafos[i+1];
                            while(nextItem.id.split('_')[0] == "item"){
                                i++;
                                html += createParagrafo("item", a - 1, b - 1, c, nextItem.innerHTML, nextItem.paragrafoId);
                                c++;
                                if(i != paragrafos.length - 1){
                                    nextItem = paragrafos[i+1];
                                }else{
                                    nextItem.id = "foo_bar";//sair do while
                                }
                            }
                        }
                        html += createParagrafo("subParagrafo", a - 1, b, c, "", "");
                        b++;
                        c = 97;
                    }
                    break;
                case 'addItemBefore':
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('item', a - 1, b - 1, c, "", "");
                        c++;
                    }
                    html += createParagrafo("item", a - 1, b - 1, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    c++;
                    break;
                case 'addItemAfter':
                    html += createParagrafo("item", a - 1, b - 1, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    c++;
                    if(paragrafo == currentParagrafo.id){
                        html += createParagrafo('item', a - 1, b - 1, c, "", "");
                        c++;
                    }
                    break;
                case 'removeItem':
                    if(paragrafo != currentParagrafo.id){
                        html += createParagrafo('item', a - 1, b - 1, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                        c++;
                    }
                    break;
                default:
                    html += createParagrafo("item", a - 1, b - 1, c, currentParagrafo.innerHTML, currentParagrafo.paragrafoId);
                    c++;
            }
        }
    }
    html += "<input type='checkbox' id='showParagrafos2' name='showParagrafos2' checked onclick='if(this.checked){document.getElementById(\"showParagrafos1\").checked = false;document.getElementById(\"paragrafos\").style.display=\"block\";}else{document.getElementById(\"showParagrafos1\").checked = false;document.getElementById(\"paragrafos\").style.display=\"none\";}'>Mostrar Parágrafos</input>";
    paragrafosDiv.innerHTML = html;
}

function createParagrafo(tipo,a,b,c,innerHTML,id){
    var paragrafoId = "";
    var paragrafoNome = "";
    var parenteses = "";
    var nivel = "";
    if(tipo == 'paragrafo'){
        nivel = a + ".";
        paragrafoId = tipo + "_" + nivel;
        paragrafoNome = "Parágrafo";
    }else if(tipo == 'subParagrafo'){
        nivel = a + "." + b + ".";
        paragrafoId = tipo + "_" + nivel;
        paragrafoNome = "Sub Parágrafo";
    }else if(tipo == 'item'){
        nivel = a + "." + b + "." + String.fromCharCode(c);
        paragrafoId = tipo + "_" + nivel;
        paragrafoNome = "Item";
        parenteses = ")";
    }
    var html = "";
    html +="<label for='" + paragrafoId + "'>" + paragrafoNome + " " + nivel + parenteses + "</label>\n";
    html +="<table id='" + paragrafoId + "Table'>\n";
    html +="  <tr>\n";
    html +="    <td>\n";
    html +="      <textarea id='" + paragrafoId + "' name='" + paragrafoId + "' cols=56 rows=5 paragrafoId='" + id + "'>" + innerHTML + "</textarea>\n";
    html +="    </td>\n";
    html +="    <td valign=top>\n";
    html +="      <input type='checkbox' id='" + paragrafoId + "RichText' name='" + paragrafoId + "RichText' onclick=\"if(this.checked){getRichText('" + paragrafoId + "');}else{hideThisRichText('" + paragrafoId + "')}\">Rich Text</input>\n";
    html +="      <select id='" + paragrafoId + "MoreActions' name='" + paragrafoId + "MoreActions' onchange=\"return moreActions('" + paragrafoId + "',this.value);\">\n";
    html +="        <option value='' style='color:silver'>Mais Ações</option>\n";
    if(tipo == 'paragrafo'){
        html +="        <option value='addParagrafoBefore'>+ Parágrafo Antes</option>\n";
        html +="        <option value='addParagrafoAfter'>+ Parágrafo Depois</option>\n";
        html +="        <option value='addSubParagrafo'>+ Sub Parágrafo</option>\n";
        if(a != 1){
            html +="        <option value='removeParagrafo'>- Este Parágrafo</option>\n";
        }
    }else if(tipo == 'subParagrafo'){
        html +="        <option value='addParagrafoAfter'>+ Parágrafo Depois</option>\n";
        html +="        <option value='addSubParagrafoBefore'>+ Sub Parágrafo Antes</option>\n";
        html +="        <option value='addSubParagrafoAfter'>+ Sub Parágrafo Depois</option>\n";
        html +="        <option value='addItem'>+ Item</option>\n";
        html +="        <option value='removeSubParagrafo'>- Este Sub Parágrafo</option>\n";
    }else if(tipo == 'item'){
        html +="        <option value='addSubParagrafoAfter'>+ Sub Parágrafo Depois</option>\n";
        html +="        <option value='addItemBefore'>+ Item Antes</option>\n";
        html +="        <option value='addItemAfter'>+ Item Depois</option>\n";
        html +="        <option value='removeItem'>- Este Item</option>\n";
    }
    html +="      </select>\n";
    html +="    </td>\n";
    html +="  </tr>\n";
    html +="</table>\n";
    return html;
}
                                            
function createPlugins(){
    createVariablePlugin();
    tinymce.PluginManager.add('variable', sigepro.plugins.Variable);
}                                

function removeRichText(){
    var textareas = document.getElementsByTagName("textarea");
    for(var i = 0; i < textareas.length; i++){
        if(tinyMCE.get(textareas[i].id) != undefined){
            tinymce.EditorManager.remove(tinyMCE.get(textareas[i].id));
        }
    }
}

function hideThisRichText(paragrafo){
    if(tinyMCE.get(paragrafo) != undefined){
        tinyMCE.get(paragrafo).hide();
        document.getElementById(paragrafo + 'RichText').checked = false;
    }
}

function hideRichText(){
    var textareas = document.getElementsByTagName("textarea");
    for(var i = 0; i < textareas.length; i++){
        hideThisRichText(textareas[i].id);
    }
}

function getRichText(paragrafo){
    if(tinyMCE.get(paragrafo) == undefined){
        tinyMCE.init({
            plugins : "-variable,iespell,preview",
            mode : "exact",
            elements : paragrafo,
            auto_focus : paragrafo,
            theme : "advanced",
            theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,sub,sup,|,preview,code,|,undo,undo,redo,link,unlink,|,forecolor,backcolor,|,variaveis",
            theme_advanced_buttons2 : "",
            theme_advanced_buttons3 : "",
            theme_advanced_toolbar_location : "top",
            theme_advanced_toolbar_align : "left",
            theme_advanced_statusbar_location : "bottom",
            theme_advanced_resizing : true,
            tab_focus : ':prev,:next',
            forced_root_block : false,
            force_p_newlines : false,
            force_br_newlines : true,
            invalid_elements : "p,br",
            extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
        });
    }else{
        tinyMCE.get(paragrafo).show();
    }
}

function createVariablePlugin(){
    tinymce.create('sigepro.plugins.Variable', {
        createControl: function(n, cm) {
            switch (n) {
                case 'variaveis':
                    var c = cm.createMenuButton('variaveis', {
                        title : 'Adicionar variável',
                        image : '/sigepro/painel/documentos/jscripts/tiny_mce/plugins/example/img/example.gif',
                        icons : false
                    });

                    c.onRenderMenu.add(function(c, m) {
                        var sub;
                        sub = m.addMenu({title : 'Empresa'});
                        sub.add({title : 'CNPJ / CPF', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getEmpresaCnpj@');
                            }});
                        sub.add({title : 'Razão Social', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getRazaoSocial@');
                            }});
                        sub.add({title : 'CEP', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getCep@');
                            }});
                        sub.add({title : 'Estado', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getEstado@');
                            }});
                        sub.add({title : 'Cidade', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getCidade@');
                            }});
                        sub.add({title : 'Bairro', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getBairro@');
                            }});
                        sub.add({title : 'Logradouro', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getLogradouro@');
                            }});
                        sub.add({title : 'Número do Logradouro', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getNumeroLogradouro@');
                            }});
                        sub.add({title : 'Complemento do Logradouro', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Empresa.getComplementoLogradouro@');
                            }});
                        
                        sub = m.addMenu({title : 'Processo'});
                        sub.add({title : 'Número do Processo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Processo.getSicap@');
                            }});
                        sub.add({title : 'Fistel', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Processo.getFistel@');
                            }});
                        sub.add({title : 'Número da Entidade', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Processo.getSitar@');
                            }});
                        
                        sub = m.addMenu({title : 'Procuração'});
                        sub.add({title : 'Validade da Procuração', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procuracao.getValidade@');
                            }});
                        
                        sub = sub.addMenu({title : 'Procurador'});
                        sub.add({title : 'CPF', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procurador.getProcuradorCpf@');
                            }});
                        sub.add({title : 'Nome', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procurador.getNome@');
                            }});
                        sub.add({title : 'Cargo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procurador.getCargo@');
                            }});
                        sub.add({title : 'Pronome', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procurador.getPronome@');
                            }});
                        sub.add({title : 'Sexo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Procurador.getSexo@');
                            }});
                        
                        sub = m.addMenu({title : 'Serviço'});
                        sub.add({title : 'Número', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.getServicoNum@');
                            }});
                        sub.add({title : 'Nome', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.getNome@');
                            }});
                        
                        sub = sub.addMenu({title : 'Responsável'});
                        sub.add({title : 'Login', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getLogin@');
                            }});
                        sub.add({title : 'Nome', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getNome@');
                            }});
                        sub.add({title : 'Cargo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getCargo@');
                            }});
                        sub.add({title : 'Área', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getArea@');
                            }});
                        sub.add({title : 'Sexo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getSexo@');
                            }});
                        sub.add({title : 'Perfil', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getPerfilName@');
                            }});
                        sub.add({title : 'Telefone', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Servico.Usuario.getTelefone@');
                            }});
                        
                        
                        sub = m.addMenu({title : 'Usuário'});
                        sub.add({title : 'Login', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getLogin@');
                            }});
                        sub.add({title : 'Nome', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getNome@');
                            }});
                        sub.add({title : 'Cargo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getCargo@');
                            }});
                        sub.add({title : 'Área', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getArea@');
                            }});
                        sub.add({title : 'Sexo', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getSexo@');
                            }});
                        sub.add({title : 'Perfil', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getPerfilName@');
                            }});
                        sub.add({title : 'Telefone', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Usuario.getTelefone@');
                            }});
                        
                        sub = m.addMenu({title : 'Data'});
                        sub.add({title : 'Hora', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Data.Hora@');
                            }});
                        sub.add({title : 'Dia da semana', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Data.DiaSemana@');
                            }});
                        sub.add({title : 'Dia do mês', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Data.DiaMes@');
                            }});
                        sub.add({title : 'Mês', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Data.Mes@');
                            }});
                        sub.add({title : 'Ano', onclick : function() {
                                tinyMCE.activeEditor.execCommand('mceInsertContent', false, '@Data.Ano@');
                            }});
                    });

                    // Return the new menu button instance
                    return c;
            }

            return null;
        }
    });
}
