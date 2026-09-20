const API_URL = "/api";

// ===============================
// REGISTER
// ===============================

const registerForm =
    document.getElementById("registerForm");

if (registerForm) {

    registerForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            const fullName =
                document.getElementById("fullName").value;

            const email =
                document.getElementById("email").value;

            const password =
                document.getElementById("password").value;

            const role =
                document.getElementById("role").value;

            const collegeId =
                Number(
                    document.getElementById("collegeId").value
                );

            const message =
                document.getElementById(
                    "registerMessage"
                );


            try {

                const response =
                    await fetch(
                        `${API_URL}/auth/register`,
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({

                                fullName:
                                    fullName,

                                email:
                                    email,

                                password:
                                    password,

                                role:
                                    role,

                                collegeId:
                                    collegeId

                            })
                        }
                    );


                const data =
                    await response.text();


                if (response.ok) {

                    message.innerHTML =
                        `<p class="success">
                            ${data}
                        </p>`;


                    registerForm.reset();


                    setTimeout(() => {

                        window.location.href =
                            "login.html";

                    }, 1500);


                } else {

                    message.innerHTML =
                        `<p class="error">
                            ${data}
                        </p>`;

                }


            } catch (error) {

                console.error(error);


                message.innerHTML =
                    `<p class="error">

                        Cannot connect to
                        Campusly server.

                        Make sure Spring Boot
                        is running.

                    </p>`;

            }

        }
    );

}


// ===============================
// LOGIN
// ===============================

const loginForm =
    document.getElementById("loginForm");

if (loginForm) {

    loginForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            const email =
                document.getElementById(
                    "loginEmail"
                ).value;


            const password =
                document.getElementById(
                    "loginPassword"
                ).value;


            const message =
                document.getElementById(
                    "loginMessage"
                );


            try {

                // ===============================
                // SEND LOGIN REQUEST
                // ===============================

                const response =
                    await fetch(
                        `${API_URL}/auth/login`,
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({

                                email:
                                    email,

                                password:
                                    password

                            })
                        }
                    );


                // ===============================
                // GET RESPONSE
                // ===============================

                const data =
                    await response.json();


                // ===============================
                // LOGIN SUCCESS
                // ===============================

                if (response.ok) {


                    // ===============================
                    // STORE JWT
                    // ===============================

                    localStorage.setItem(
                        "token",
                        data.token
                    );


                    // ===============================
                    // STORE FULL NAME
                    // ===============================

                    localStorage.setItem(
                        "fullName",
                        data.fullName
                    );


                    // ===============================
                    // STORE ROLE
                    // ===============================

                    localStorage.setItem(
                        "role",
                        data.role
                    );


                    // ===============================
                    // SUCCESS MESSAGE
                    // ===============================

                    message.innerHTML =
                        `<p class="success">

                            Login successful!

                        </p>`;


                    // ===============================
                    // ROLE BASED REDIRECT
                    // ===============================

                    setTimeout(() => {


                        // ===============================
                        // MENTOR
                        // ===============================

                        if (
                            data.role === "MENTOR"
                        ) {

                            window.location.href =
                                "mentor-dashboard.html";

                        }


                        // ===============================
                        // STUDENT
                        // ===============================

                        else if (
                            data.role === "STUDENT"
                        ) {

                            window.location.href =
                                "dashboard.html";

                        }
                        // ===============================
                        // UNKNOWN ROLE
                        // ===============================
                        else {

                            alert(
                                "Invalid user role: "
                                + data.role
                            );

                        }

                    }, 800);


                }


                // ===============================
                // LOGIN FAILED
                // ===============================

                else {

                    message.innerHTML =
                        `<p class="error">

                            ${data}

                        </p>`;

                }


            } catch (error) {

                console.error(error);


                message.innerHTML =
                    `<p class="error">

                        Cannot connect to
                        Campusly server.

                    </p>`;

            }

        }
    );

}