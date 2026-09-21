function displayCourses(courses) {

    const container =
        document.getElementById(
            "coursesContainer"
        );


    container.innerHTML = "";


    if (courses.length === 0) {

        container.innerHTML = `

            <div class="empty-message">

                <h3>
                    No courses yet
                </h3>

                <p>
                    Create your first course.
                </p>

            </div>

        `;

        return;
    }


    courses.forEach(
        async function (course) {

            const card =
                document.createElement(
                    "div"
                );


            card.className =
                "book-card";


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


            loadEnrollmentCount(
                course.id
            );
        }
    );
}

async function loadEnrollmentCount(courseId) {

    const token =
        localStorage.getItem("token");


    try {

        const response =
            await fetch(
                API_URL +
                "/enrollments/course/" +
                courseId +
                "/count",
                {
                    method: "GET",

                    headers: {
                        "Authorization":
                            "Bearer " + token
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