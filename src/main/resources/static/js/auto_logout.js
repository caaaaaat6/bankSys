document.addEventListener("DOMContentLoaded", function (event) {

    var currentUrl = window.location.href;


    window.addEventListener("beforeunload", function (event) {

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/logout", true);
        xhr.send();
    });


});