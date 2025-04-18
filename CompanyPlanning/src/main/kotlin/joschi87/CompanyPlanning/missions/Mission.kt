package joschi87.CompanyPlanning.missions

import joschi87.CompanyPlanning.platoon.model
import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import jakarta.persistence.*
import joschi87.CompanyPlanning.Platoon.Platoon


@Entity
@Data
class Mission {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String ?= null
    var text: String ?= null
    var finished: Boolean = false
    var activ: Boolean = false
    var executingPlatoon: model ?= null

    @ManyToOne
    @JoinColumn(name = "platoon_id") // FK in der Mission-Tabelle
    var platoon: Platoon? = null

    
}