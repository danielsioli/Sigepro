var responseReceived = false;

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

function getAusencias(form, usuario){
    //Mostrar ausencias do usuário escolhido na tela
    var httpRequest = initRequest();
    if(httpRequest != null){
        //setDisabledButtons(true);
        responseReceived = false;
        //Enviar solicitação ao servidor
        httpRequest.onreadystatechange = function() {
            if (httpRequest.readyState == 4) {
                setDisabledButtons(false);
                if (httpRequest.status == 200) {
                    responseReceived = true;
                    //restoreScreen(form,parseMessages(httpRequest,"Consultar"));
                } else if (httpRequest.status == 204){
                    showMessage("Servidor não respondeu adequadamente.",60000,"failure");
                    restoreScreen(form,false);
                }
            }
        };
        httpRequest.open("POST", "/sigepro/control.view?method=consultar&login=" + usuario, true);
        httpRequest.send(null);
    }
}

function getResponse(form,tipo){
    var msg = isValidFields(form,tipo);
    if(msg.length == 0){
        var url = "/sigepro/control.view?";
        var inputs = form.getElementsByTagName("input");
        var selects = form.getElementsByTagName("select");
        for(var i = 0; i < inputs.length; i++){
            url += inputs[i].name + "=" + inputs[i].value.replace(/&/g,'@#@') + "&";
        }
        for(var i = 0; i < selects.length; i++){
            url += selects[i].name + "=" + selects[i].value + "&";
        }
        url = url.substring(0, url.length - 1);
        var httpRequest = initRequest();
        if(httpRequest != null){
            var confirmation = true;
            if(tipo == "Excluir" || tipo == "Alterar"){
                confirmation = confirm("Confirma operação?");
            }
            if(confirmation){
                setDisabledButtons(true);
                responseReceived = false;
                showWaitMessage(1);
                //Enviar solicitação ao servidor
                httpRequest.onreadystatechange = function() {
                    if (httpRequest.readyState == 4) {
                        setDisabledButtons(false);
                        if (httpRequest.status == 200) {
                            responseReceived = true;
                            restoreScreen(form,parseMessages(httpRequest,tipo));
                        } else if (httpRequest.status == 204){
                            showMessage("Servidor não respondeu adequadamente.",60000,"failure");
                            restoreScreen(form,false);
                        }
                    }
                };
                httpRequest.open("POST", url, true);
                httpRequest.send(null);
                //TODO indicar ao se a solicitação foi executada
            }
            return true;
        }
        return false;
    }else{
        alert(msg);
        return false;
    }
}

function addEstatistica(tipoDocumento,modelo){
    var url = "/sigepro/control.view?method=updateEstatistica&tipoDocumento="+tipoDocumento+"&modelo="+modelo;
    var httpRequest = initRequest();
    if(httpRequest != null){
        //Enviar solicitação ao servidor
        httpRequest.onreadystatechange = function() {
        };
        httpRequest.open("POST", url, true);
        httpRequest.send(null);
        return true;
    }
    return false;
}        

function setDisabledButtons(disabled){
    var buttons = document.getElementsByTagName("button");
    for(var i = 0; i < buttons.length; i++){
        buttons[i].disabled = disabled;
    }
}

function restoreScreen(form,eraseValues){
    if(eraseValues){
        createCookie(form);
        var inputs = form.getElementsByTagName("input");
        var selects = form.getElementsByTagName("select");
        for(var i = 0; i < inputs.length; i++){
            if(inputs[i].attributes["nullable"].value != "STATIC"){
                inputs[i].value = "";
            }
        }
        for(var i = 0; i < selects.length; i++){
            if(selects[i].attributes["nullable"].value != "STATIC"){
                selects[i].value = "";
            }
        }
    }
    
}

function parseMessages(httpRequest,tipo) {
    var items = httpRequest.responseXML.getElementsByTagName("response")[0];
    var transactionOk = null;
    var message = null;
    if(tipo == 'Consultar'){
        if(createTable(httpRequest.responseText)){
            showMessage("Dados encontrados",60000,"success");
            return true;
        }else{
            showMessage("Nenhum dado foi encontrado",60000,"failure");
            return false;
        }
    }else if(tipo == 'GetProcesso'){
        var responseXML = httpRequest.responseXML;        
        if(items != null && items.childNodes.length == 3){
            if(items.childNodes[0].getElementsByTagName("name")[0].childNodes[0].nodeValue == "transactionOk"){
                transactionOk = items.childNodes[0].getElementsByTagName("value")[0].childNodes[0].nodeValue;
            }
            if(items.childNodes[1].getElementsByTagName("name")[0].childNodes[0].nodeValue == "message"){
                message = items.childNodes[1].getElementsByTagName("value")[0].childNodes[0].nodeValue;
            }
            if((transactionOk != null && (transactionOk == "true" || transactionOk == "false")) && message != null){
                if(transactionOk == "true"){
                    document.getElementById("processo").value = items.childNodes[2].getElementsByTagName("value")[0].childNodes[0].nodeValue;
                    document.getElementById("messageProcesso").innerHTML = "Processo atual: " + message;
                    document.getElementById("messageProcesso").style.color = "black";
                    showMessage("Processo encontrado",60000,"success");
                    if(document.getElementById('tipoDocumento')){
                        document.getElementById('tipoDocumento').onchange();
                    }
                    return true;
                }else{
                    document.getElementById("processo").value = "";
                    document.getElementById("messageProcesso").innerHTML = "Processo ainda não selecionado";
                    document.getElementById("messageProcesso").style.color = "red";
                    showMessage("Processo não encontrado",60000,"failure");
                    if(document.getElementById('tipoDocumento')){
                        document.getElementById('tipoDocumento').onchange();
                    }
                    return false;
                }
            }else{
                showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
                return false;
            }
        }else{
            showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
            return false;
        }
    }else{
        if(items != null && items.childNodes.length >= 2){
            if(items.childNodes[0].getElementsByTagName("name")[0].childNodes[0].nodeValue == "transactionOk"){
                transactionOk = items.childNodes[0].getElementsByTagName("value")[0].childNodes[0].nodeValue;
            }
            if(items.childNodes[1].getElementsByTagName("name")[0].childNodes[0].nodeValue == "message"){
                message = items.childNodes[1].getElementsByTagName("value")[0].childNodes[0].nodeValue;
            }
            if((transactionOk != null && (transactionOk == "true" || transactionOk == "false")) && message != null){
                showMessage(message,60000,transactionOk == "true" ? "success":"failure");
                return transactionOk == "true";
            }else{
                showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
                return false;
            }
        }else{
            showMessage("Servidor respondeu de maneira inesperada.",60000,"failure");
            return false;
        }
    }
}

function createCookie(form){
    var method = document.getElementById("method");
    if(method != null){
        if(method.value == 'incluir'){
            var inputs = form.getElementsByTagName("input");
            for(var i = 0; i < inputs.length; i++){
                if(inputs[i].attributes['key'].value == 'PRI'){
                    var date = new Date();
                    date.setTime(date.getTime() + 2*60*1000);//válido por dois minutos
                    var expires = "; expires=" + date.toGMTString();
                    document.cookie = 'last_' + inputs[i].name + '=' + inputs[i].value + expires + '; path=/';
                }
            }
        }else if(method.value == 'getProcesso'){
            var processo = document.getElementById('processo');
            var messageProcesso = document.getElementById('messageProcesso');
            if(processo != null){
                var date = new Date();
                date.setTime(date.getTime() + 24*60*60*1000);//válido por um dia
                var expires = "; expires=" + date.toGMTString();
                document.cookie = 'current_sicap=' + processo.value + expires + '; path=/';
                document.cookie = 'messageProcesso=' + messageProcesso.innerHTML + expires + '; path=/';
            }
        }
    }
}

function createTable(responseText){
    var consultaTabela = document.getElementById('consultaTabela');
    if(responseText.length > 0){
        consultaTabela.innerHTML = responseText;
        consultaTabela.style.display = "block";
        return true;
    }else{
        return false;
    }
}
function getScreenWithoutWhitspaces(){
    var screen = document.getElementById("tela");
    cleanWhitespace(screen);
    return screen;
}

var notWhitespace = /\S/;

function cleanWhitespace(node) {
    for (var x = 0; x < node.childNodes.length; x++) {
        var childNode = node.childNodes[x];
        if ((childNode.nodeType == 3)&&(!notWhitespace.test(childNode.nodeValue))) {
            // that is, if it's a whitespace text node
            node.removeChild(node.childNodes[x]);
            x--;
        }
        if (childNode.nodeType == 1) {
            // elements can have text child nodes of their own
            cleanWhitespace(childNode);
        }
    }
}