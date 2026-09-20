// =====================================
// LOAD STUDENT INFORMATION
// =====================================

document.addEventListener("DOMContentLoaded", function () {
    const fullName = localStorage.getItem("fullName");
    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    if (fullName) {
        document.getElementById("userInfo").textContent =
            "Welcome, " + fullName;
    }
});


// =====================================
// MODULE CLICK
// =====================================

function openModule(moduleName) {
    alert(moduleName + " module is coming soon!");
}


// =====================================
// LOGOUT
// =====================================

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("fullName");
    localStorage.removeItem("role");

    window.location.href = "login.html";
}

document.getElementById("booksModule").addEventListener("click", function () {

    alert("Buy & Sell Books module is coming soon!");

});