const API_URL = "http://localhost:8080/api";

const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

// ======================================
// CHECK LOGIN AND ADMIN ROLE
// ======================================

if (!token) {
    window.location.href = "login.html";
}

if (role !== "SUPERADMIN") {
    alert("Access denied. Super Admin only.");
    window.location.href = "dashboard.html";
}


// ======================================
// GET ALL USERS
// ======================================

async function loadUsers() {

    const container =
        document.getElementById("usersContainer");

    container.innerHTML =
        "<p>Loading users...</p>";

    try {

        const response = await fetch(
            `${API_URL}/users`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (response.status === 401) {
            logout();
            return;
        }

        if (response.status === 403) {

            container.innerHTML = `
                <div class="error">
                    You don't have permission to view users.
                </div>
            `;

            return;
        }

        if (!response.ok) {
            throw new Error("Failed to load users");
        }

        const users = await response.json();

        displayUsers(users);

    } catch (error) {

        console.error(error);

        container.innerHTML = `
            <div class="error">
                Unable to connect to the Campusly server.
            </div>
        `;
    }
}


// ======================================
// DISPLAY USERS
// ======================================

function displayUsers(users) {

    const container =
        document.getElementById("usersContainer");

    if (users.length === 0) {

        container.innerHTML =
            "<p>No users found.</p>";

        return;
    }

    let html = `
        <div class="users-table-wrapper">

            <table class="users-table">

                <thead>

                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>College ID</th>
                        <th>Action</th>
                    </tr>

                </thead>

                <tbody>
    `;

    users.forEach(user => {

        html += `
            <tr>

                <td>${user.id}</td>

                <td>${escapeHtml(user.fullName)}</td>

                <td>${escapeHtml(user.email)}</td>

                <td>
                    <span class="user-role">
                        ${user.role}
                    </span>
                </td>

                <td>${user.collegeId ?? "-"}</td>

                <td>

                    <button
                        class="delete-btn"
                        onclick="deleteUser(${user.id})">

                        Delete

                    </button>

                </td>

            </tr>
        `;

    });

    html += `
                </tbody>

            </table>

        </div>
    `;

    container.innerHTML = html;
}


// ======================================
// GET USER BY ID
// ======================================

async function getUserById() {

    const id =
        document.getElementById("userId").value;

    if (!id) {

        alert("Please enter a user ID.");

        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/users/${id}`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (response.status === 404) {

            document.getElementById("searchResult")
                .innerHTML =
                `<p class="error">User not found.</p>`;

            return;
        }

        if (response.status === 403) {

            document.getElementById("searchResult")
                .innerHTML =
                `<p class="error">Access denied.</p>`;

            return;
        }

        const user = await response.json();

        displaySingleUser(user);

    } catch (error) {

        console.error(error);

        document.getElementById("searchResult")
            .innerHTML =
            `<p class="error">
                Unable to connect to server.
            </p>`;
    }
}


// ======================================
// GET USER BY EMAIL
// ======================================

async function getUserByEmail() {

    const email =
        document.getElementById("userEmail").value;

    if (!email) {

        alert("Please enter an email.");

        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/users/email/${encodeURIComponent(email)}`,
            {
                method: "GET",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (response.status === 404) {

            document.getElementById("searchResult")
                .innerHTML =
                `<p class="error">User not found.</p>`;

            return;
        }

        if (response.status === 403) {

            document.getElementById("searchResult")
                .innerHTML =
                `<p class="error">Access denied.</p>`;

            return;
        }

        const user = await response.json();

        displaySingleUser(user);

    } catch (error) {

        console.error(error);

        document.getElementById("searchResult")
            .innerHTML =
            `<p class="error">
                Unable to connect to server.
            </p>`;
    }
}


// ======================================
// DISPLAY ONE USER
// ======================================

function displaySingleUser(user) {

    const result =
        document.getElementById("searchResult");

    result.innerHTML = `
        <div class="user-details">

            <h3>User Details</h3>

            <p>
                <strong>ID:</strong>
                ${user.id}
            </p>

            <p>
                <strong>Name:</strong>
                ${escapeHtml(user.fullName)}
            </p>

            <p>
                <strong>Email:</strong>
                ${escapeHtml(user.email)}
            </p>

            <p>
                <strong>Role:</strong>
                ${user.role}
            </p>

            <p>
                <strong>College ID:</strong>
                ${user.collegeId ?? "-"}
            </p>

        </div>
    `;
}


// ======================================
// DELETE USER
// ======================================

async function deleteUser(id) {

    const confirmDelete =
        confirm(
            `Are you sure you want to delete user ${id}?`
        );

    if (!confirmDelete) {
        return;
    }

    try {

        const response = await fetch(
            `${API_URL}/users/${id}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (response.status === 403) {

            alert("You don't have permission to delete users.");

            return;
        }

        if (response.status === 404) {

            alert("User not found.");

            return;
        }

        if (!response.ok) {

            const errorText =
                await response.text();

            alert(errorText || "Failed to delete user.");

            return;
        }

        alert("User deleted successfully.");

        loadUsers();

    } catch (error) {

        console.error(error);

        alert("Unable to connect to Campusly server.");
    }
}


// ======================================
// LOGOUT
// ======================================

function logout() {

    localStorage.clear();

    window.location.href =
        "login.html";
}


// ======================================
// BASIC HTML ESCAPING
// ======================================

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ======================================
// LOAD USERS WHEN PAGE OPENS
// ======================================

document.addEventListener(
    "DOMContentLoaded",
    loadUsers
);