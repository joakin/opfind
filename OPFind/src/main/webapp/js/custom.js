 $(document).ready(function() {
    var texto=$(".searchterms").html();
    if(texto && texto!="")
    {
        texto = texto.replace(/\"/ig, "");
        var words = texto.split(" AND ");
        var i=0;
        for(i=0;i<words.length;i++)
        {
            $(".searchtext").highlight(words[i]);
        }
    }
});