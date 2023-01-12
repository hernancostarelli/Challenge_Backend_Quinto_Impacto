 // FUNCTION IN CHARGE OF REDIRECTION
    function redirection() {
    /*window.location = "http://localhost:8080";*/
    window.location.href = "/login";
    }

    // THE FUNCTION THAT REDIRECTS WILL BE CALLED AFTER (62 SECONDS)
    var temp = setTimeout(redirection, 62000);

    // WHEN MOVING ANYWHERE IN THE DOCUMENT
    document.addEventListener("mousemove", function() {

    // CLEAR THE TIMER THAT WAS REDIRECTING
    clearTimeout(temp);

    // AND START IT AGAIN
    temp = setTimeout(redirection, 62000);
    })