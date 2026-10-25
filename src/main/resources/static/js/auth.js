const API_URL = "/api";

// ========================================
// REGISTER
// ========================================

const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const fullName = document.getElementById("fullName").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;
        const role = document.getElementById("role").value;
        const collegeId = Number(
            document.getElementById("collegeId").value
        );

        const message = document.getElementById("registerMessage");

        try {
            // STEP 1: REGISTER USER
            const response = await fetch(`${API_URL}/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    fullName: fullName,
                    email: email,
                    password: password,
                    role: role,
                    collegeId: collegeId
                })
            });

            const data = await response.text();

            // STEP 2: HANDLE REGISTRATION FAILURE
            if (!response.ok) {
                message.innerHTML = `
                    <p class="error">${data}</p>
                `;
                return;
            }

            // STEP 3: SEND OTP AFTER REGISTRATION SUCCEEDS
            message.innerHTML = `
                <p class="success">
                    Registration successful. Sending OTP to your email...
                </p>
            `;

            await startRegistrationOtp(email);

        } catch (error) {
            console.error("Registration error:", error);

            message.innerHTML = `
                <p class="error">
                    Cannot connect to Campusly server.
                    Make sure Spring Boot is running.
                </p>
            `;
        }
    });
}


// ========================================
// SEND REGISTRATION OTP
// ========================================

async function startRegistrationOtp(email) {
    const message = document.getElementById("registerMessage");

    try {
        const response = await fetch(`${API_URL}/otp/send`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email
            })
        });

        const data = await response.text();

        if (!response.ok) {
            throw new Error(data || "Unable to send OTP.");
        }

        // SAVE EMAIL FOR OTP VERIFICATION PAGE
        sessionStorage.setItem("otpEmail", email);
        sessionStorage.setItem("otpAction", "register");

        // OPEN OTP PAGE
        window.location.href = "otp-verification.html";

    } catch (error) {
        console.error("OTP sending error:", error);

        if (message) {
            message.innerHTML = `
                <p class="error">
                    Registration succeeded, but OTP sending failed.
                    ${error.message}
                </p>
            `;
        }
    }
}


// ========================================
// LOGIN
// ========================================

const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value;

        const message = document.getElementById("loginMessage");

        try {
            // STEP 1: SEND LOGIN REQUEST
            const response = await fetch(`${API_URL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });

            // READ RESPONSE
            const data = await response.json();

            // STEP 2: HANDLE LOGIN FAILURE
            if (!response.ok) {
                message.innerHTML = `
                    <p class="error">
                        ${data.message || "Invalid email or password."}
                    </p>
                `;
                return;
            }

            /*
             * IMPORTANT:
             * This preserves your existing login behavior.
             * If your backend returns a JWT immediately, this code
             * logs the user in without requiring an OTP.
             *
             * To require OTP for login, the backend must be changed
             * to issue the JWT only after OTP verification.
             */

            // STEP 3: STORE JWT
            localStorage.setItem("token", data.token);

            // STEP 4: STORE FULL NAME
            localStorage.setItem("fullName", data.fullName);

            // STEP 5: STORE ROLE
            localStorage.setItem("role", data.role);

            // STEP 6: DISPLAY SUCCESS MESSAGE
            message.innerHTML = `
                <p class="success">
                    Login successful!
                </p>
            `;

            // STEP 7: REDIRECT BASED ON ROLE
            setTimeout(function () {

                if (data.role === "MENTOR") {

                    window.location.href = "mentor-dashboard.html";

                } else if (data.role === "STUDENT") {

                    window.location.href = "dashboard.html";

                } else {

                    alert("Invalid user role: " + data.role);

                }

            }, 800);

        } catch (error) {
            console.error("Login error:", error);

            message.innerHTML = `
                <p class="error">
                    Cannot connect to Campusly server.
                    Make sure Spring Boot is running.
                </p>
            `;
        }
    });
}