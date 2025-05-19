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



            document.getElementById('listOfPlatoons').innerHTML = ` <ul class="list-group">
                ${data.map(p => `
                    <li class="list-group-item">
                        <input class="form-check-input" type="checkbox" value="" id="${p.id}" />
                        <label class="form-check-label" for="${p.id}">${p.platoonname}</label>
                        <button class="btn btn-outline-primary ms-auto" data-bs-target="#modalForPlatoon${p.platoonname}" data-bs-toggle="modal" >Mehr Anzeigen</button>
                        
                         <div class="modal fade" id="modalForPlatoon${p.platoonname}" tabindex="-1" aria-labelledby="showSinglePlatoonLabel" aria-hidden="true">
                            <div class="modal-dialog app-modal custom-width">
                                <div class="modal-content">
                                    <div class="modal-header bg-success text-white">
                                        <h5 class="modal-title" id="showSinglePlatoonLabel"></h5>
                                    </div>
                                    <div class="modal-body" id="showSinglePlatoonBody">
                                        <p><strong>Führer:</strong> ${data.leader}</p>
                                        <p><strong>Einsatzzeit:</strong> ${data.timeActiveMission}</p>
                                        <p><strong>Missionen:</strong></p>
                                        <ul>${missions}</ul>
                                    </div>
                                </div>
                            </div>
                         </div>
                    </li>
            `
        ).join('')} </ul>`;
        })
        .catch(err => console.error("Fehler beim Laden der Züge:", err));
}

