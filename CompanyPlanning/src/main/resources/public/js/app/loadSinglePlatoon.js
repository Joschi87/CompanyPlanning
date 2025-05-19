async function loadPlatoonForModal(name) {
    console.log("Function call");
    console.log(name);
    try {
        const res = await fetch(`/platoon/${name}`);
        if (!res.ok) {
            const errorText = await res.text();
            document.getElementById('errorModalBody').textContent = res.status + ": " + errorText;
            new bootstrap.Modal(document.getElementById('errorModal')).show();
            throw new Error(`HTTP ${res.status}: ${errorText}`);
        }

        const data = await res.json();

        console.log(data)

        document.getElementById('showSinglePlatoonLabel').textContent = `Zug: ${data.platoonname}`;

        const missions = data.missionModels.map(mission => `
          <li>
            <strong>${mission.name}</strong> – ${mission.text || ""}
          </li>
        `).join('');

        document.getElementById('showSinglePlatoonBody').innerHTML = `
          <p><strong>Führer:</strong> ${data.leader}</p>
          <p><strong>Einsatzzeit:</strong> ${data.timeActiveMission}</p>
          <p><strong>Missionen:</strong></p>
          <ul>${missions}</ul>
        `;

        new bootstrap.Modal(document.getElementById('modalForPlatoon')).show();
    } catch (err) {
        console.error(`Fehler beim Laden des Zuges ${name}`, err);
    }
}