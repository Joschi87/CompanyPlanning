package joschi.CompanyPlanning.Missions

import java.util.UUID
import jakarta.persistence.*
import joschi.CompanyPlanning.Platoon.PlatoonModel



@Entity
class MissionModel () {

    @Id
    var id: UUID = UUID.randomUUID()

    var missionName: String? = null
    var text: String? = null
    var finished: Boolean = false
    var storyMission: Boolean = false
    var activ: Boolean = false

    @ManyToOne
    @JoinColumn(name = "platoon_id")  // Name der Spalte in der Datenbank
    var platoon: PlatoonModel? = null

}