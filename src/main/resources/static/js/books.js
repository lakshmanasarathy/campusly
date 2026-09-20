const API_URL = "/api";

// ===============================
// LOAD BOOKS
// ===============================
document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    loadBooks();
});

// ===============================
// LOAD AVAILABLE BOOKS
// ===============================
async function loadBooks() {
    try {
        const token = localStorage.getItem("token");

        const response = await fetch(
            API_URL + "/books",
            {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Failed to load books");
        }

        const books = await response.json();
        displayBooks(books);

    } catch (error) {
        console.error(error);

        document.getElementById("booksContainer").innerHTML =
            "<p>Unable to load books.</p>";
    }
}

// ===============================
// DISPLAY BOOKS
// ===============================
function displayBooks(books) {
    const container =
        document.getElementById("booksContainer");

    container.innerHTML = "";

    if (books.length === 0) {
        container.innerHTML = `
            <div class="empty-message">
                <h3>No books available</h3>
                <p>Be the first student to sell a book.</p>
            </div>
        `;
        return;
    }

    books.forEach(function (book) {
        const card =
            document.createElement("div");

        card.className = "book-card";

        card.innerHTML = `
            <h3>${book.title}</h3>

            <p>
                <strong>Author:</strong>
                ${book.author || "Not specified"}
            </p>

            <p>${book.description || ""}</p>

            <p class="book-condition">
                <strong>Condition:</strong>
                ${book.condition || "Not specified"}
            </p>

            <p class="book-price">
                ₹${book.price}
            </p>

            <button
                class="buy-btn"
                onclick="buyBook(${book.id})">
                Buy Now
            </button>

            <button
                class="delete-btn"
                onclick="deleteBook(${book.id})">
                Delete
            </button>
        `;

        container.appendChild(card);
    });
}

// ===============================
// OPEN SELL MODAL
// ===============================
function openSellModal() {
    document.getElementById("sellModal").style.display = "flex";
}

// ===============================
// CLOSE SELL MODAL
// ===============================
function closeSellModal() {
    document.getElementById("sellModal").style.display = "none";
}

// ===============================
// SELL BOOK
// ===============================
async function sellBook() {
    const title =
        document.getElementById("title").value.trim();

    const author =
        document.getElementById("author").value.trim();

    const description =
        document.getElementById("description").value.trim();

    const price =
        document.getElementById("price").value;

    const condition =
        document.getElementById("condition").value;

    if (!title || !price || !condition) {
        alert("Please enter title, price and condition.");
        return;
    }

    const token =
        localStorage.getItem("token");

    try {
        const response = await fetch(
            API_URL + "/books",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({
                    title: title,
                    author: author,
                    description: description,
                    price: Number(price),
                    condition: condition
                })
            }
        );

        if (!response.ok) {
            throw new Error("Unable to add book");
        }

        alert("Book added successfully!");

        closeSellModal();
        clearForm();
        loadBooks();

    } catch (error) {
        console.error(error);
        alert("Failed to add book.");
    }
}

// ===============================
// CLEAR FORM
// ===============================
function clearForm() {
    document.getElementById("title").value = "";
    document.getElementById("author").value = "";
    document.getElementById("description").value = "";
    document.getElementById("price").value = "";
    document.getElementById("condition").value = "";
}

// ===============================
// DELETE BOOK
// ===============================
async function deleteBook(id) {
    const confirmDelete =
        confirm("Are you sure you want to delete this book?");

    if (!confirmDelete) {
        return;
    }

    const token =
        localStorage.getItem("token");

    try {
        const response = await fetch(
            API_URL + "/books/" + id,
            {
                method: "DELETE",
                headers: {
                    "Authorization":
                        "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            throw new Error("Delete failed");
        }

        alert("Book deleted successfully.");
        loadBooks();

    } catch (error) {
        console.error(error);
        alert("Unable to delete book.");
    }
}

// ===============================
// BUY BOOK
// ===============================
async function buyBook(id) {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "login.html";
        return;
    }

    try {
        // ===============================
        // CREATE RAZORPAY ORDER
        // ===============================
        const response = await fetch(
            API_URL + "/book-payment/create-order/" + id,
            {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );

        if (!response.ok) {
            const errorMessage = await response.text();

            alert(
                errorMessage ||
                "Unable to create payment order"
            );

            return;
        }

        const data = await response.json();

        console.log("Razorpay order:", data);

        // ===============================
        // RAZORPAY CHECKOUT
        // ===============================
        const options = {
            key: data.key,
            amount: data.amount,
            currency: data.currency,
            name: "Campusly",
            description: "Purchase Book",
            order_id: data.orderId,

            // ===============================
            // PAYMENT SUCCESS
            // ===============================
            handler: async function (paymentResponse) {

                console.log(
                    "Payment successful:",
                    paymentResponse
                );

                try {
                    // ===============================
                    // VERIFY PAYMENT
                    // ===============================
                    const verifyResponse =
                        await fetch(
                            API_URL +
                            "/book-payment/verify",
                            {
                                method: "POST",

                                headers: {
                                    "Content-Type":
                                        "application/json",

                                    "Authorization":
                                        "Bearer " +
                                        token
                                },

                                body: JSON.stringify({
                                    razorpayOrderId:
                                        paymentResponse
                                            .razorpay_order_id,

                                    razorpayPaymentId:
                                        paymentResponse
                                            .razorpay_payment_id,

                                    razorpaySignature:
                                        paymentResponse
                                            .razorpay_signature,

                                    bookId: id
                                })
                            }
                        );

                    // ===============================
                    // CHECK VERIFICATION
                    // ===============================
                    if (!verifyResponse.ok) {
                        const errorMessage =
                            await verifyResponse.text();

                        alert(
                            errorMessage ||
                            "Payment verification failed"
                        );

                        return;
                    }

                    const result =
                        await verifyResponse.json();

                    console.log(
                        "Payment verification:",
                        result
                    );

                    // ===============================
                    // PAYMENT COMPLETED
                    // ===============================
                    alert(
                        "Payment successful!\n\n" +
                        "Book purchased successfully."
                    );

                    loadBooks();

                } catch (error) {
                    console.error(error);

                    alert(
                        "Payment was completed, " +
                        "but verification failed."
                    );
                }
            },

            // ===============================
            // PREFILL
            // ===============================
            prefill: {
                name:
                    localStorage.getItem("fullName") || ""
            },

            // ===============================
            // THEME
            // ===============================
            theme: {
                color: "#3399cc"
            }
        };

        // ===============================
        // OPEN RAZORPAY
        // ===============================
        const razorpay =
            new Razorpay(options);

        razorpay.open();

    } catch (error) {
        console.error(error);

        alert(
            "Something went wrong while starting payment."
        );
    }
}

// ===============================
// LOGOUT
// ===============================
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("fullName");
    localStorage.removeItem("role");

    window.location.href = "login.html";
}