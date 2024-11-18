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

            const response = await fetch("/api/annonces", {
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
    async function loadKeywords() {
    try {
        const response = await fetch('/api/annonces/keywords');
        
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
    window.onload=loadKeywords();

    