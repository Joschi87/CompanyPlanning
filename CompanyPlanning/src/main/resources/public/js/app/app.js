function getAllPlatoonsForMainSite(){
    fetch('/platoon')
        .then(async res => {
            if (!res.ok) {
                const errorText = await res.text();
                document.getElementById('errorModalBody').textContent = res.status + ": " + errorText;
                new bootstrap.Modal(document.getElementById('errorModal')).show();
                throw new Error(`HTTP ${res.status}: ${errorText}`);
            }
            return res.json();
        })
        .then(data => {
            document.getElementById('platoon-cards').innerHTML = data.map(p => `
        <div class="col-md-4 mb-4">
          <div class="card shadow-sm h-100">
            <div class="card-body">
              <h5 class="card-title">${p.name}</h5>
              <h6 class="card-subtitle mb-2 text-muted">Führer: ${p.leader}</h6>
              <p class="card-text">Aktive Zeit: ${p.timeActiveMission}</p>
              <p class="card-text"><strong>Mission:</strong> ${p.activeMission || 'Keine aktive Mission'}</p>
            </div>
          </div>
        </div>
      `).join('');
        })
        .catch(err => console.error("Fehler beim Laden der Züge:", err));
}

function getRequestWithErrorModal(link){
    fetch(link)
        .then(async res => {
            if (!res.ok) {
                const errorText = await res.text();
                document.getElementById('errorModalBody').textContent = res.status + ": " + errorText;
                new bootstrap.Modal(document.getElementById('errorModal')).show();
                throw new Error(`HTTP ${res.status}: ${errorText}`);
            }
            return res.json();
        })
    .then(data => {return data})
}