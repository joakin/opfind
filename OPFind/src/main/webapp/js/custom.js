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

    if($("#ultconv").length)
    {
        $(".smallitem>span").children("div").hide();
        for(x=0;x<4;x++)$(".smallitem>span").children("div:not(.shown)").not(":visible").last().slideDown("slow").addClass("shown");
        timer=setInterval(showItem, 6000);
    }

});

function showItem()
{
    var list=$(".smallitem>span").children("div:not(.shown)").not(":visible");
    if(list.length==0)
        clearInterval(timer);
    else{
        list.last().slideDown("slow").addClass("shown");
        $(".smallitem>span").children("div.shown:visible").last().slideUp("slow");
    }
}