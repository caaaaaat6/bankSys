document.addEventListener("DOMContentLoaded", function() {
    // Function to toggle the visibility of the input label
    function toggleInputLabel() {
        var lastThreeRadios = $("input[name='employeeType']").slice(-3);
        if (lastThreeRadios.is(":checked")) {
            $("#departmentLabel").hide();
            $("#department").hide();
            $("#href_add_department").hide();
        } else {
            $("#departmentLabel").show();
            $("#department").show();
            $("#href_add_department").show();
        }
    }

    // Initial check on page load
    toggleInputLabel();

    // Event handler for radio button change event
    $("input[name='employeeType']").change(function() {
        toggleInputLabel();
    });
});
