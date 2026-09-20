document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    if (role !== "MENTOR") {
        alert("Access denied. Mentor account required.");
        window.location.href = "dashboard.html";
        return;
    }

});

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("fullName");
    localStorage.removeItem("role");

    window.location.href = "login.html";
}