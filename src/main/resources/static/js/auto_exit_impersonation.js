document.addEventListener("DOMContentLoaded", function (event) {
    window.addEventListener("beforeunload", function (event) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", "/employee/impersonate/exit", true);
        xhr.send();
    });
});