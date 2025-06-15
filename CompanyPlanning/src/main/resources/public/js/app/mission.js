function loadAllMissionsForModalList(){
    fetch('/mission', {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
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
            document.getElementById('listOfMissions').innerHTML = `<ul class="list-group">
                ${data.map(entry => {
                const safeId = `mission-${entry.id}`;// sichere ID ohne Leerzeichen
                return `
                    <li class="list-group-item d-flex flex-column align-items-start app-mission-list">
                        <div class="d-flex w-100 justify-content-between align-items-center">
                            <div>
                                <!-- <input class="form-check-input me-2" type="checkbox" id="${safeId}-checkbox" /> -->
                                <label class="form-check-label" for="${safeId}-checkbox">${entry.missionName}</label>
                            </div>
                            <button class="btn btn-sm btn-outline-primary" type="button" data-bs-toggle="collapse" data-bs-target="#${safeId}-collapse" aria-expanded="false" aria-controls="${safeId}-collapse">Mehr anzeigen</button>
                        </div>
                        <div class="collapse mt-2 w-100" id="${safeId}-collapse">
                            <div class="card card-body">
                               <strong>ID:</strong> <input type="text" value="${entry.id}" disabled /><br>
                               <strong>Mission Name:</strong> <input type="text" value="${entry.missionName}" id="update-name-${safeId}" required/><br>
                               <strong>Mission Beschreibung:</strong> <textarea class="form-control" id="mission-text-${safeId}" rows="4" placeholder="Gib eine Missionsbeschreibung ein ....">${entry.text}</textarea>
                               <strong>Status Finished?</strong> <input type="text" id="finished-${safeId}" value="">
                               <strong>Status Story Mission?</strong> <input type="text" id="storyMission-${safeId}">
                               <strong>Zugewissener Zug:</strong> <input type="text" id="plattonForMission-${safeId}" value="${entry.platoon}" />
                            </div>
                            <br>
                            <button class=" btn btn-warning app app-update-data" data-bs-dismiss="modal" id="update-button" onclick="updateMission('${safeId}', '${entry.id}')">Update ${entry.platoonname}</button>
                            <button class="btn btn-danger app" data-bs-dismiss="modal" onclick="deleteMission('${entry.id}')">L&ouml;schen</button>
                        </div>
                    </li>`;
            }).join('')}
            </ul>`;
        })
        .catch(err => console.error("Fehler beim Laden der Züge:", err));
}

function updateMission(safeId, uuid){
    const missionName = "update-name-" +safeId;
    const missionText = "mission-text-" + safeId;
    const finishedID = "finished-" + safeId;
    const storyMissionID = "storyMission-" + safeId;
    const platoon = "platoonForMission-" + safeId;

    fetch("/mission",{
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: uuid,
            missionName: document.getElementById(missionName).value,
            missionText: document.getElementById(missionText).value,
            finished: document.getElementById(finishedID).value,
            storyMission: document.getElementById(storyMissionID).value,
            platoon: document.getElementById(platoon).value
        })
    })
    .then(res => {
        if (res.status === 202) {
            document.getElementById('successModalBody').textContent = "Mission: " + document.getElementById(missionName).value + ", wurde erfolgreich aktualisiert.";
            new bootstrap.Modal(document.getElementById('successModal')).show();
        }else{
            document.getElementById('errorModalBody').textContent = "Responsecode: " + res.status `<br/>` + "Fehlermeldung: " + res.text();
            new bootstrap.Modal(document.getElementById('errorModal')).show();
        }
    })

}

function deleteMission(uuid){
    fetch("/mission/" + uuid,{
        method: 'DELETE',
        headers: {
            'Accept': 'application/json'
        }})
        .then(async res => {
            if (res.status === 204) {
                document.getElementById('successModalBody').textContent = "Die Mission wurde erfolgreich gelöscht!";
                new bootstrap.Modal(document.getElementById('successModal')).show();
            }if(res.status === 409){
                res.text().then(errorMessage => {
                    document.getElementById('errorModalBody').innerHTML =
                        "Responsecode: " + res.status + "<br/>" +
                        "Fehlermeldung: " + errorMessage;
                    new bootstrap.Modal(document.getElementById('errorModal')).show();
                });
            }
        })
}