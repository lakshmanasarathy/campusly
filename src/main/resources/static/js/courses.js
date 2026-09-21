const API_URL = "/api";


document.addEventListener(
    "DOMContentLoaded",
    function () {

        const token =
            localStorage.getItem("token");

        const role =
            localStorage.getItem("role");


        if (!token) {

            alert("Please login first.");

            window.location.href =
                "login.html";

            return;
        }


        if (role !== "STUDENT") {

            alert(
                "Student account required."
            );

            window.location.href =
                "mentor-dashboard.html";

            return;
        }


        loadCourses();
    }
);


// ======================================
// LOAD COURSES
// ======================================

async function loadCourses() {

    const token =
        localStorage.getItem("token");


    const container =
        document.getElementById(
            "courseContainer"
        );


    container.innerHTML =
        "<p>Loading courses...</p>";


    try {

        const response =
            await fetch(
                API_URL + "/courses",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        if (!response.ok) {

            throw new Error(
                "Failed to load courses"
            );
        }


        const courses =
            await response.json();


        displayCourses(courses);


    } catch (error) {

        console.error(error);

        container.innerHTML =
            "<p>Unable to load courses.</p>";
    }
}


// ======================================
// DISPLAY COURSES
// ======================================

function displayCourses(courses) {

    const container =
        document.getElementById(
            "courseContainer"
        );


    container.innerHTML = "";


    if (courses.length === 0) {

        container.innerHTML = `

            <div class="empty-message">

                <h3>
                    No courses available
                </h3>

                <p>
                    Mentors have not added
                    any courses yet.
                </p>

            </div>

        `;

        return;
    }


    courses.forEach(
        function (course) {

            const card =
                document.createElement(
                    "div"
                );


            card.className =
                "course-card";


            card.innerHTML = `

                <div class="course-content">

                    <h2>
                        ${course.title}
                    </h2>

                    <p>
                        ${course.description || ""}
                    </p>

                    <span>
                        Available
                    </span>

                    <button
                        class="primary-btn"
                        onclick="enroll(${course.id})">

                        Enroll

                    </button>

                </div>

            `;


            container.appendChild(card);
        }
    );
}


// ======================================
// ENROLL
// ======================================

async function enroll(courseId) {

    const token =
        localStorage.getItem("token");


    if (!token) {

        alert("Please login first.");

        window.location.href =
            "login.html";

        return;
    }


    try {

        const response =
            await fetch(
                API_URL +
                "/enrollments/" +
                courseId,
                {
                    method: "POST",

                    headers: {
                        "Authorization":
                            "Bearer " + token
                    }
                }
            );


        const message =
            await response.text();


        if (!response.ok) {

            alert(message);

            return;
        }


        alert(message);


    } catch (error) {

        console.error(error);

        alert(
            "Unable to enroll in course."
        );
    }
}


// ======================================
// LOGOUT
// ======================================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("fullName");

    localStorage.removeItem("role");

    window.location.href =
        "login.html";
}