// =====================================
// LOAD STUDENT INFORMATION
// =====================================

document.addEventListener("DOMContentLoaded", function () {

    const username =
        localStorage.getItem("username");

    if (username) {

        document.getElementById("studentName")
            .textContent = username;
    }

});


// =====================================
// MODULE CLICK
// =====================================

function openModule(moduleName) {

    alert(
        moduleName +
        " module is coming soon!"
    );

}


// =====================================
// LOGOUT
// =====================================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("username");

    localStorage.removeItem("role");

    window.location.href =
        "/login.html";
}