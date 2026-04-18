console.log("Contacts.js loaded");

let contactModal;

document.addEventListener("DOMContentLoaded", function () {
    const modalEl = document.getElementById("view_contact_modal");

    if (modalEl) {
        contactModal = new Modal(modalEl);
    }
});

// Modal open
function openContactModal() {
    if (contactModal) contactModal.show();
}

// 🔥 MAIN FUNCTION
async function loadContact(id) {
    console.log("Loading ID:", id);

    try {
        const response = await fetch(`http://localhost:8081/api/contacts/${id}`);
        
        if (!response.ok) {
            throw new Error("API error");
        }

        const data = await response.json();
        console.log("Data:", data);

        // ===== BASIC DETAILS =====
        document.getElementById("contact-name").innerText = data.name ?? "N/A";
        document.getElementById("contact-email").innerText = data.email ?? "N/A";
        document.getElementById("contact-phone").innerText = data.phoneNumber ?? "N/A";
        document.getElementById("contact-address").innerText = data.address ?? "N/A";
        document.getElementById("contact-description").innerText = data.description ?? "No description";

        // ===== IMAGE =====
        const img = document.getElementById("contact-image");
        img.src = data.pictures && data.pictures.trim() !== ""
            ? data.pictures
            : "https://cdn-icons-png.flaticon.com/512/149/149071.png";

        // ===== FAVORITE =====
        const fav = document.getElementById("contact-favorite");
        if (data.favorite === true) {
            fav.classList.remove("hidden");
        } else {
            fav.classList.add("hidden");
        }

        // ===== LINKS =====
        function setLink(id, url) {
            const el = document.getElementById(id);
            if (url && url.trim() !== "") {
                el.href = url;
                el.style.display = "inline";
            } else {
                el.style.display = "none";
            }
        }

        setLink("contact-website", data.websiteLink);
        setLink("contact-github", data.githubLink);
        setLink("contact-linkedin", data.linkedInLink);

        // ===== OPEN MODAL =====
        openContactModal();

    } catch (err) {
        console.error("Error loading contact:", err);
        alert("Contact load nahi hua ❌");
    }
}

async function deleteContact(id) {

    Swal.fire({
        title: "Do you want to delete the contact?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Delete"
    }).then((result) => {

        if (result.isConfirmed) {
            const url = `http://localhost:8081/user/contact/delete/${id}`;
            window.location.replace(url);
        }

    });

}