 // function in charge of redirection
    function redirection() {
    /*window.location = "http://localhost:8080";*/
    window.location.href = "/login";
    }

    // the function that redirects will be called after (15 seconds)
    var temp = setTimeout(redirection, 20000);

    // when moving anywhere in the document
    document.addEventListener("mousemove", function() {
    // clear the timer that was redirecting
    clearTimeout(temp);
    // and start it again
    temp = setTimeout(redirection, 20000);
    })