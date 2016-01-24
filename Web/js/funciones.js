document.oncontextmenu = function(){return false}

//   Primera Grafica 

Parse.initialize("poner cliente id parse","poner aplicacion id parse");
//Recuperar datos de parse
var RegistroPasos= Parse.Object.extend("poner BD Parse");
var query = new Parse.Query(RegistroPasos);
query.greaterThan("Usuario", " ");
query.ascending("Fecha");
query.find({
    success: function(results) {
        //alert("Successfully retrieved " + results.length + " scores.");
        // Do something with the returned Parse.Object values

        var aUsuarios=new Array();
        var iusu,ifechas,ipasos=0;

        var aPasos=new Array();
        var aFechas=new Array();

        var tabla = '<table class="tab-score "><tr class="top-scrore-table"><td class="score-position "><b class="t1">Fecha</b></td><td><b class="t1">Pasos</b></td><td><b class="t1" >Usuario</b></td></tr>'
       
		for (var i = 0; i < results.length; i++) {
            var object = results[i];
            var npasos = object.get("Pasos");
            var fecha = object.get("Fecha");
            var usuario = object.get("Usuario");

            //formamos array Usuarios
            if (aUsuarios.indexOf(usuario) == -1) {
                aUsuarios.push(usuario);
                aFechas[aUsuarios.length-1]=new Array();
                aPasos[aUsuarios.length-1]=new Array();
            }
            iusu=aUsuarios.indexOf(usuario);


            //Formamos Array de pasos y fechas del usuario
            if (aFechas[iusu].indexOf(fecha) == -1) {
                var fechaC='"'+fecha+'"';
                aFechas[iusu].push(fechaC);
                aPasos[iusu].push("0");
            }
            ifechas=aFechas[iusu].indexOf(fechaC);
            var npasosC='"'+npasos+'"';
            aPasos[iusu][ifechas]=npasosC;

            //Visualizamos la tabla
            tabla += '<tr>' +
                '<td class="score-position">'+fecha+'</td>' +
                '<td class="score-position" style="margin-right: 96px">'+npasos+'</td>' +
                '<td class="score-position">'+usuario+'</td>' +
                '</tr>';


        }
		
        tabla += '</table>'
		///formateamos los datos para visualizarlos en plotly
        var data="var data=[";				
        for(var i = 0; i < aUsuarios.length; i++){
            eval("var trace" + i + "= {x: ["+aFechas[i]+"],y:["+aPasos[i]+"],mode: 'lines+markers',name:'"+aUsuarios[i]+"',type: 'bar'};");
            data = data+"trace" + i ;
            if(i<aUsuarios.length-1){
                data = data+",";
            }else{
                data = data+"];";
            }
        }

        eval(data);
		
        var layout = {legend: {
            y: 5,
            traceorder: 'reversed',
            font: {size: 16},
            yref: 'paper'
        }};
		
        Plotly.newPlot('myDiv', data, layout);

    },
    error: function(error) {
        alert("Error: " + error.code + " " + error.message);
    }
});


//   Segunda Grafica 

Parse.initialize("poner cliente id parse","poner aplicacion id parse");
//Recuperar datos de parse
var RegistroPasos= Parse.Object.extend("poner BD Parse");
var query = new Parse.Query(RegistroPasos);
query.greaterThan("Usuario", " ");
query.ascending("Fecha","Usuario");

query.find({
    success: function(results) {
        //alert("Successfully retrieved " + results.length + " scores.");
        // Do something with the returned Parse.Object values

        var iusu,ifechas,ipasos=0;

        var tPasos=new Array();
        var tUsuarios=new Array();

        var tabla = '<table class="tab-score "><tr class="top-scrore-table"><td class="score-position "><b class="t1">Usuario</b></td><td><b class="t1">Pasos</b></td></tr>'
        for (var i = 0; i < results.length; i++) {
            var object = results[i];
            var npasos = object.get("Pasos");
            var fecha = object.get("Fecha");
            var usuario = object.get("Usuario");

            //formamos Array TOTALES
            if (tUsuarios.indexOf(usuario) == -1) {
                tUsuarios.push(usuario);
                tPasos.push(0);
            }

            iusu=tUsuarios.indexOf(usuario);
            tPasos[iusu]=tPasos[iusu]+parseInt(npasos);
        }
        //alert ("--> "+aPasos[ifechas]+" -IF- "+ifechas+"-IU-"+iusu+"-Co-"+aPasos[ifechas][iusu]);
        //Visualizamos la tabla
        for (var i = 0; i < tUsuarios.length; i++) {
            //alert(tPasos[i].toLocaleString());
            var tPasosF = tPasos[i].toLocaleString();
            tabla += '<tr>' +
                '<td class="score-position">'+tUsuarios[i]+'</td>' +
                '<td class="score-position" style="margin-right: 96px" >'+tPasosF+'</td>' +
                '</tr>';
        }

        tabla += '</table>'
        document.getElementById('tablas1').innerHTML = tabla;
        var xValue = tUsuarios;
        var yValue = tPasos;
        var trace1 = {
            x: xValue,
            y: yValue,
            type: 'bar',           
            marker: {
                color: 'rgb(158,202,225)',
                opacity: 0.6,
                line: {
                    color: 'rbg(8,48,107)',
                    width: 1.5
                }
            }
        };

        var annotationContent = [];

        data = [trace1];

        layout = {
            //title: 'Ranking de Pasos',
            annotations: annotationContent
        };

        for( var i = 0 ; i < xValue.length ; i++ ){
            var result = {
                x: xValue[i],
                y: yValue[i],
                text: yValue[i],
                xanchor: 'center',
                yanchor: 'bottom',
                showarrow: true
            };
            annotationContent.push(result);
        };

        Plotly.newPlot('myDiv1', data, layout);
    },
    error: function(error) {
        alert("Error: " + error.code + " " + error.message);
    }
});

//Tercera Grafica 

Parse.initialize("poner cliente id parse","poner aplicacion id parse");
//Recuperar datos de parse
var RegistroPasos= Parse.Object.extend("poner BD Parse");
var query = new Parse.Query(RegistroPasos);
query.greaterThan("Usuario", " ");
query.ascending("Fecha","Usuario");

query.find({
    success: function(results) {
       

        var aFechas=new Array();
        var iusu,ifechas,ipasos=0;
        var aPasos=new Array();
        var aUsuario=new Array();
        var tPasos=new Array();
        var tUsuarios=new Array();

        var tabla = '<table class="container tab-score" style="width: 760px;"><tr class="top-scrore-table"><td class="score-position "><b class="t1">Fecha</b></td><td><b class="t1">Pasos</b></td><td><b class="t1" >Usuario</b></td></tr>'
        for (var i = 0; i < results.length; i++) {
            var object = results[i];
            var npasos = object.get("Pasos");
            var fecha = object.get("Fecha");
            var usuario = object.get("Usuario");

            //formamos Array TOTALES
            var usuarioT='"'+usuario+'"';
            if (tUsuarios.indexOf(usuarioT) == -1) {
                tUsuarios.push(usuarioT);
                tPasos.push(0);
            }
            iusu=tUsuarios.indexOf(usuarioT);
            tPasos[iusu]=tPasos[iusu]+parseInt(npasos);

            //formamos Array Fechas
            var fechaC='"'+fecha+'"';
            fechaC = fechaC.replace("/","-");
            fechaC = fechaC.replace("/","-");
            //alert ( "fecha "+fechaC);
            if (aFechas.indexOf(fechaC) == -1) {
                aFechas.push(fechaC);
                aUsuario[aFechas.length-1]=new Array();
                aPasos[aFechas.length-1]=new Array();
            }
            ifechas=aFechas.indexOf(fechaC);
            //Formamos Array de pasos y usuarios
            var usuarioC='"'+usuario+'"';
            if (aUsuario[ifechas].indexOf(usuarioC) == -1) {
                aUsuario[ifechas].push(usuarioC);
                aPasos[ifechas].push("0");
            }
            iusu=aUsuario[ifechas].indexOf(usuarioC);
            var npasosC='"'+npasos+'"';
            var npasosF=parseInt(npasos).toLocaleString();
            aPasos[ifechas][iusu]=npasosC.toLocaleString();
            
            //Visualizamos la tabla
            tabla += '<tr>' +
                '<td class="score-position" style="font-weight: normal; font-size: 15px">'+fecha+'</td>' +
                '<td class="score-position" style="font-weight: normal; font-size: 15px">'+npasosF+'</td>' +
                '<td class="score-position" style="font-weight: normal; font-size: 15px">'+usuario+'</td>' +
                '</tr>';


        }
        tabla += '</table>'
        document.getElementById('tablas2').innerHTML = tabla;
        var xValue = tUsuarios;
        var yValue = tPasos;
        var data="var data=[";
        

        for(var i = 0; i < aFechas.length; i++){            
            eval(" var trace" + i + "= {x: ["+aUsuario[i]+"],y:["+aPasos[i]+"],name:"+aFechas[i]+",type: 'bar'};");           
            data = data+"trace" + i ;
            if(i<aFechas.length-1){
                data = data+",";
            }else{
                data = data+"];";
            }            
        }

        eval(data);
       
        var annotationContent = [];
        var layout = {
            yaxis:{title: 'Número de pasos'},
            xaxis:{title: 'Usuarios'},
            annotations: annotationContent,
            barmode: 'stack'
        };

        for( var i = 0 ; i < xValue.length ; i++ ){
            var result = {
                x: xValue[i],
                y: yValue[i],
                text: yValue[i],
                xanchor: 'center',
                yanchor: 'bottom',
                showarrow: false
            };
            //annotationContent.push(result);
        }


        Plotly.newPlot('myDiv2', data, layout);

    },
    error: function(error) {
        alert("Error: " + error.code + " " + error.message);
    }
});

