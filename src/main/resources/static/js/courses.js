const API_URL = "/api";


document.addEventListener(
    "DOMContentLoaded",
    function () {

        const token =
            localStorage.getItem("token");

        if (!token) {

            alert("Please login first.");

            window.location.href =
                "login.html";

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

function enroll(courseId) {

    const token =
        localStorage.getItem("token");

    if (!token) {

        alert("Please login first.");

        window.location.href =
            "login.html";

        return;
    }


    alert(
        "Enrollment functionality will be added next."
    );
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