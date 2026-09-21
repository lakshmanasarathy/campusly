const API_URL = "/api";


// ======================================
// PAGE LOAD
// ======================================

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

    loadCourses();

});


// ======================================
// LOAD COURSES
// ======================================

async function loadCourses() {

    const token = localStorage.getItem("token");

    try {

        const response = await fetch(
            API_URL + "/courses/mentor",
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load courses");
        }

        const courses = await response.json();

        displayCourses(courses);

    } catch (error) {

        console.error(error);

        document.getElementById("coursesContainer").innerHTML =
            "<p>Unable to load courses.</p>";

    }

}


// ======================================
// DISPLAY COURSES
// ======================================

function displayCourses(courses) {

    const container =
        document.getElementById("coursesContainer");

    container.innerHTML = "";

    if (courses.length === 0) {

        container.innerHTML = `
            <div class="empty-message">

                <h3>No courses yet</h3>

                <p>
                    Create your first course.
                </p>

            </div>
        `;

        return;
    }


    courses.forEach(function (course) {

        const card =
            document.createElement("div");

        card.className = "book-card";

        card.innerHTML = `

            <h3>
                ${course.title}
            </h3>

            <p>
                ${course.description || ""}
            </p>

            <p id="count-${course.id}">
                Enrolled Students: Loading...
            </p>

            <button
                class="primary-btn"
                onclick="viewStudents(${course.id})">

                View Students

            </button>

            <button
                class="delete-btn"
                onclick="deleteCourse(${course.id})">

                Delete

            </button>

        `;

        container.appendChild(card);

        loadEnrollmentCount(course.id);

    });

}


// ======================================
// OPEN MODAL
// ======================================

function openCourseModal() {

    document.getElementById(
        "courseModal"
    ).style.display = "flex";

}


// ======================================
// CLOSE MODAL
// ======================================

function closeCourseModal() {

    document.getElementById(
        "courseModal"
    ).style.display = "none";

}


// ======================================
// CREATE COURSE
// ======================================

async function createCourse() {

    const title =
        document.getElementById("title").value.trim();

    const description =
        document.getElementById("description").value.trim();


    if (!title) {

        alert("Please enter course title.");

        return;

    }


    const token =
        localStorage.getItem("token");


    try {

        const response = await fetch(
            API_URL + "/courses",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },

                body: JSON.stringify({
                    title: title,
                    description: description
                })
            }
        );


        if (!response.ok) {

            const message =
                await response.text();

            alert(
                message ||
                "Unable to create course."
            );

            return;

        }


        alert("Course created successfully!");


        closeCourseModal();


        document.getElementById("title").value = "";

        document.getElementById("description").value = "";


        loadCourses();


    } catch (error) {

        console.error(error);

        alert("Something went wrong.");

    }

}


// ======================================
// DELETE COURSE
// ======================================

async function deleteCourse(id) {

    const confirmDelete =
        confirm(
            "Are you sure you want to delete this course?"
        );


    if (!confirmDelete) {
        return;
    }


    const token =
        localStorage.getItem("token");


    try {

        const response = await fetch(
            API_URL + "/courses/" + id,
            {
                method: "DELETE",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (!response.ok) {

            throw new Error("Delete failed");

        }


        alert("Course deleted successfully.");


        loadCourses();


    } catch (error) {

        console.error(error);

        alert("Unable to delete course.");

    }

}


// ======================================
// LOAD ENROLLMENT COUNT
// ======================================

async function loadEnrollmentCount(courseId) {

    const token =
        localStorage.getItem("token");


    try {

        const response = await fetch(
            API_URL +
            "/enrollments/course/" +
            courseId +
            "/count",
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (!response.ok) {
            return;
        }


        const data =
            await response.json();


        const countElement =
            document.getElementById(
                "count-" + courseId
            );


        if (countElement) {

            countElement.innerText =
                "Enrolled Students: " +
                data.count;

        }


    } catch (error) {

        console.error(error);

    }

}


// ======================================
// VIEW ENROLLED STUDENTS
// ======================================

async function viewStudents(courseId) {

    const token =
        localStorage.getItem("token");


    try {

        const response = await fetch(
            API_URL +
            "/enrollments/course/" +
            courseId,
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (!response.ok) {

            const message =
                await response.text();

            alert(message);

            return;

        }


        const students =
            await response.json();


        if (students.length === 0) {

            alert(
                "No students enrolled in this course yet."
            );

            return;

        }


        let message =
            "Enrolled Students:\n\n";


        students.forEach(
            function (student, index) {

                message +=
                    (index + 1) +
                    ". " +
                    student.fullName +
                    "\n" +
                    "   " +
                    student.email +
                    "\n\n";

            }
        );


        alert(message);


    } catch (error) {

        console.error(error);

        alert("Unable to load students.");

    }

}

// ======================================
// LOGOUT
// ======================================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("fullName");

    localStorage.removeItem("role");

    window.location.href = "login.html";

}
<script src="js/mentor-courses.js"></script>	