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
              <h5 class="card-title">${p.platoonname}</h5>
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

function loadAllPlatoonsForModalList(){
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
            document.getElementById('listOfPlatoons').innerHTML = `<ul class="list-group">
                ${data.map(entry => {
                const safeId = `platoon-${entry.id}`;// sichere ID ohne Leerzeichen
                return `
                    <li class="list-group-item d-flex flex-column align-items-start">
                        <div class="d-flex w-100 justify-content-between align-items-center">
                            <div>
                                <!-- <input class="form-check-input me-2" type="checkbox" id="${safeId}-checkbox" /> -->
                                <label class="form-check-label" for="${safeId}-checkbox">${entry.platoonname}</label>
                            </div>
                            <button class="btn btn-sm btn-outline-primary" type="button" data-bs-toggle="collapse" data-bs-target="#${safeId}-collapse" aria-expanded="false" aria-controls="${safeId}-collapse">Mehr anzeigen</button>
                        </div>
                        <div class="collapse mt-2 w-100" id="${safeId}-collapse">
                            <div class="card card-body">
                                <strong>ID:</strong> <input type="text" value="${entry.id}" disabled/><br>
                                <strong>Name:</strong> <input type="text" value="${entry.platoonname}" id="update-name-${safeId}" required/><br>
                                <strong>Zugf&uuml;hrer:</strong> <input type="text" value="${entry.leader}" id="update-leader-${safeId}" required/></br>
                                <strong>Mission aktiv seit:</strong> ${entry.timeActiveMission || 'Keine aktive Mission'}
                                <strong>Aktive Mission:</strong> ${entry.activeMission || 'Keine aktive Mission'}
                            </div>
                            <br>
                            <button class=" btn btn-warning app app-update-data" id="update-button" data-bs-toggle="modal" data-bs-target="#${safeId}-update" onclick="updatePlatoon('${safeId}', '${entry.id}')">Update ${entry.platoonname}</button>
                        </div>
                    </li>`;
            }).join('')}
            </ul>`;
        })
        .catch(err => console.error("Fehler beim Laden der Züge:", err));
}

function updatePlatoon(safeID, uuid){
    const idPlatoonname = "update-name-" + safeID;
    const idLeader = "update-leader-" + safeID;
    fetch('/platoon',{
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: uuid,
            platoonname: document.getElementById(idPlatoonname).value,
            leader: document.getElementById(idLeader).value
        })
    })
        .then(res => {
            if (res.status === 202) {
                document.getElementById('successModalBody').textContent = "Zug: " + document.getElementById(idPlatoonname).value + ", wurde erfolgreich aktualisiert.";
                new bootstrap.Modal(document.getElementById('successModal')).show();
            }else{
                document.getElementById('errorModalBody').textContent = "Responsecode: " + res.status `<br/>` + "Fehlermeldung: " + res.text();
                new bootstrap.Modal(document.getElementById('errorModal')).show();
            }
        })
}

function reloadPage() {
    location.reload();
}