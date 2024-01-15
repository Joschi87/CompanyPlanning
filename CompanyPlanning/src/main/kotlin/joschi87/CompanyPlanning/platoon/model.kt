package joschi87.CompanyPlanning.platoon

import joschi87.CompanyPlanning.missions.model
import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Data
class model {

    @Id
    private var id: UUID = UUID.randomUUID()

    lateinit var platoonname: String
    lateinit var leader: String
    lateinit var missions: List<model>
}