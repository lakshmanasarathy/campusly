function enroll(courseName) {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first.");
        window.location.href = "login.html";
        return;
    }

    alert("Successfully enrolled in " + courseName + "!");
}