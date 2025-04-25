package joschi87.CompanyPlanning.Missions

import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import jakarta.persistence.*
import joschi87.CompanyPlanning.Platoon.PlatoonModel


@Entity
@Data
class MissionModel() {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String ?= null
    var text: String ?= null
    var finished: Boolean = false
    var storyMission: Boolean = false
    var activ: Boolean = false

    @ManyToOne
    @JoinColumn(name = "platoon_id") // FK in der Mission-Tabelle
    var platoonModel: PlatoonModel? = null

}