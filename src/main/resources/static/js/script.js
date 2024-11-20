const responseMessages = {
    SUCCESS: "Bravo",
    FAILURE: "Nulllll"
};
//A faire évoluer

const form = document.getElementById("annonceForm");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(form);

    const keywords = [];
    formData.getAll("keywords").forEach(keyword => keywords.push(keyword));

    const data = {
        title: formData.get("title"),
        description: formData.get("description"),
        state: formData.get("state"),
        publicationDate: formData.get("publicationDate"),
        zone: formData.get("zone"),
        isHandDelivery: formData.get("isHandDelivery") === "on",
        keywords: keywords
    };

    const response = await fetch("/annonces", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"

        },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        form.reset();
        resetKeywords();
        loadKeywords();
        updateDate();
        alert("Annonce créée avec succès !");

    } else {
        alert("Erreur lors de la création de l'annonce.");
    }
});

function addKeyword() {
    const keywordInput = document.getElementById("keywordInput");
    const keyword = keywordInput.value.trim();

    if (keyword) {

        const keywordsList = document.getElementById("keywordsList");
        const li = document.createElement("li");
        li.textContent = keyword;
        keywordsList.appendChild(li);
        const keywordsHiddenFields = document.getElementById("keywordsHiddenFields");
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "keywords";
        input.value = keyword;
        keywordsHiddenFields.appendChild(input);
        keywordInput.value = "";
    }
}
function resetKeywords() {
    const keywordsList = document.getElementById("keywordsList");
    const keywordsHiddenFields = document.getElementById("keywordsHiddenFields");


    keywordsList.innerHTML = "";


    keywordsHiddenFields.innerHTML = "";
}
window.addEventListener("load", () => {
    const publicationDateInput = document.getElementById("publicationDate");
    const now = new Date();
    const formattedDate = now.toISOString().slice(0, 16);
    publicationDateInput.value = formattedDate;
});
function updateDate(){
    const publicationDateInput = document.getElementById("publicationDate");
    const now = new Date();
    const formattedDate = now.toISOString().slice(0, 16);
    publicationDateInput.value = formattedDate;
}
async function loadKeywords() {
    try {

        const response = await fetch('/annonces/keywords');


        if (!response.ok) throw new Error('Erreur lors du chargement des mots-clés');

        const keywords = await response.json();

        const keywordsSelect = document.getElementById('keywords');

        keywords.forEach(keyword => {
            const option = document.createElement('option');
            option.value = keyword;
            option.textContent = keyword;
            keywordsSelect.appendChild(option);
        });
    } catch (error) {
        console.error(error);
    }
}
const deleteButtons = document.querySelectorAll(".delete-search");
console.log(deleteButtons);
deleteButtons.forEach(button => {
    button.addEventListener("click", function () {
        const searchId = this.value;
        console.log(searchId);
        fetch(`/annonces/search/${searchId}`, { method: "DELETE" })
            .then(response => {
                if (response.ok) {
                    this.closest("tr").remove();
                    alert(responseMessages.SUCCESS);
                } else {
                    console.error("Failed to delete search");
                }
            })
            .catch(error => console.error(error));
    });
});
document.getElementById('saveButton').addEventListener('click', function () {
    const form = document.getElementById('searchForm');
    const formData = new FormData(form);

    fetch('/annonces/search', {
        method: 'POST',
        body: formData,
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => {
            if (response.status === 200) {
                alert(responseMessages.SUCCESS);
            } else {
                throw new Error('Erreur lors de la requête');
            }
        })
        .catch((error) => {
            alert(responseMessages.FAILURE);
        });
});
window.onload = loadKeywords;

function handleFetch(url, options, successMessage, errorMessage) {
    fetch(url, options)
        .then(response => {
            if (response.ok) {
                if (successMessage) alert(successMessage);
            } else {
                throw new Error(errorMessage);
            }
        })
        .catch(error => {
            if (errorMessage) alert(errorMessage);
            console.error(error);
        });
}

function changeStatus(checkbox, notificationId) {
    const status = checkbox.checked ? 1 : 0;
    handleFetch(`/notifications/${notificationId}/status`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status })
    }, null, 'Failed to update notification status');
}

function changehasNotification() {
    handleFetch('/api/hasNotification', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    }, null, 'Failed to update notification status');
}