const events = [

    {
        title: "Java Programming Workshop",
        date: "September 15, 2026",
        location: "Computer Science Block",
        description: "Learn Java programming, OOP concepts and problem solving."
    },

    {
        title: "Career Development Seminar",
        date: "September 20, 2026",
        location: "Auditorium",
        description: "Learn about interview preparation, resume building and career opportunities."
    },

    {
        title: "Web Development Bootcamp",
        date: "September 25, 2026",
        location: "Innovation Lab",
        description: "Learn HTML, CSS and JavaScript by building real-world projects."
    },

    {
        title: "Campus Hackathon",
        date: "October 5, 2026",
        location: "Main Campus",
        description: "Participate in an exciting coding hackathon and build innovative solutions."
    },

    {
        title: "Tech Talk",
        date: "October 12, 2026",
        location: "Seminar Hall",
        description: "Join industry professionals and learn about the latest technology trends."
    },

    {
        title: "Student Networking Event",
        date: "October 18, 2026",
        location: "Student Center",
        description: "Meet students, developers, entrepreneurs and industry professionals."
    }

];


const container = document.getElementById("eventsContainer");

if (container) {

    events.forEach(function(event) {

        const card = document.createElement("div");

        card.className = "event-card";

        card.innerHTML = `
            <div class="event-icon">
                📅
            </div>

            <div class="event-date">
                ${event.date}
            </div>

            <h2>
                ${event.title}
            </h2>

            <p>
                ${event.description}
            </p>

            <div class="event-location">
                📍 ${event.location}
            </div>

            <button
                class="primary-btn event-btn"
                onclick="registerEvent('${event.title}')">

                Register

            </button>
        `;

        container.appendChild(card);

    });

}


function registerEvent(eventName) {

    alert(
        eventName +
        "\n\nRegistration feature is coming soon! 🚀"
    );

}


function logout() {

    localStorage.clear();

    window.location.href = "login.html";

}