 $(document).ready(function() {
    var texto=$(".searchterms").html();
    if(texto && texto!="")
    {
        texto = texto.replace(/\"/ig, "");
        alert(texto);
        var words = texto.split(" AND ");
        var i=0;
        for(i=0;i<words.length;i++)
        {
            alert(words[i]);
            $(".searchtext").highlight(words[i]);
        }
    }
});