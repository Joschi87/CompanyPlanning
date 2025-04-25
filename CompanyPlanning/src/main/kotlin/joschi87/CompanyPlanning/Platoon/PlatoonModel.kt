package joschi87.CompanyPlanning.Platoon

import joschi87.CompanyPlanning.Missions.MissionModel
import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import jakarta.persistence.*

@Entity
@Data
class PlatoonModel {

    @Id
    var id: UUID = UUID.randomUUID()

    lateinit var platoonname: String
    lateinit var leader: String
    @OneToMany(mappedBy = "platoon", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var missionModels: MutableList<MissionModel> = mutableListOf()
    lateinit var timeActiveMission: String
}