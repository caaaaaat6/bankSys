document.addEventListener("DOMContentLoaded", function (event) {
    // // Get the URL of the current page
    // var currentUrl = window.location.href;
    //
    // // Check if the current page is the "deposit" page
    // if (currentUrl.indexOf("/current") !== -1) {
    //     if (event.currentTarget.performance.navigation.type !== 1) {// Add an event listener to detect when the user navigates away from the page
    //         window.addEventListener("beforeunload", function (event) {
    //             // Make a POST request to the logout URL to log the user out
    //             var xhr = new XMLHttpRequest();
    //             xhr.open("POST", "/logout", true);
    //             xhr.send();
    //         });
    //     }
    // }

    // // Get the URL of the current page
    // var currentUrl = window.location.href;
    //
    // // Check if the current page is the "deposit" page
    // // if (currentUrl.indexOf("/current") !== -1) {
    //     // Add an event listener to detect when the user navigates away from the page
    //     window.addEventListener("beforeunload", function (event) {
    //
    //
    //         if (event.currentTarget.performance.getEntriesByType('navigation')[0].type !== "reload") {
    //         // if (performance.navigation.type !== performance.navigation.TYPE_RELOAD) {
    //             // Make a POST request to the logout URL to log the user out
    //             var xhr = new XMLHttpRequest();
    //             xhr.open("POST", "/logout", true);
    //             xhr.send();
    //         }
    //     });


    var currentUrl = window.location.href;

    // Check if the current page is the "deposit" page
    // if (currentUrl.indexOf("/current") !== -1) {
    // Add an event listener to detect when the user navigates away from the page
    window.addEventListener("beforeunload", function (event) {


        // if (performance.navigation.type !== performance.navigation.TYPE_RELOAD) {
        // Make a POST request to the logout URL to log the user out
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/logout", true);
        xhr.send();

    });


});