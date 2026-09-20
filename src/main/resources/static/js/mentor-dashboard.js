const API_URL = "/api";


document.addEventListener("DOMContentLoaded", function () {

    const token =
        localStorage.getItem("token");

    const role =
        localStorage.getItem("role");

    const fullName =
        localStorage.getItem("fullName");


    // Check login

    if (!token) {

        window.location.href =
            "login.html";

        return;
    }


    // Check mentor role

    if (role !== "MENTOR") {

        alert(
            "Access denied. Mentor account required."
        );

        window.location.href =
            "dashboard.html";

        return;
    }


    // Display mentor name

    document.getElementById(
        "mentorInfo"
    ).innerText =
        "Welcome, " +
        (fullName || "Mentor");


    loadMentorCourses();

});


async function loadMentorCourses() {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                API_URL + "/courses/mentor",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (!response.ok) {

            console.log(
                "Unable to load mentor courses"
            );

            return;
        }


        const courses =
            await response.json();


        document.getElementById(
            "courseCount"
        ).innerText =
            courses.length;


    } catch (error) {

        console.error(error);

    }

}


function comingSoon(moduleName) {

    alert(
        moduleName +
        " module is coming soon!"
    );

}


function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("fullName");

    localStorage.removeItem("role");


    window.location.href =
        "login.html";

}