package joschi.CompanyPlanning.Platoon

import joschi.CompanyPlanning.Missions.MissionModel

import java.util.UUID
import jakarta.persistence.*

@Entity
class PlatoonModel() {

    @Id
    var id: UUID = UUID.randomUUID()

    var platoonname: String = ""
    var leader: String = ""
    var timeActiveMission: String = ""
    var mission: String = ""

}