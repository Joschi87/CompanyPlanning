package joschi87.CompanyPlanning.Platoon

import joschi87.CompanyPlanning.missions.Mission
import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import jakarta.persistence.*

@Entity
@Data
class Platoon {

    @Id
    var id: UUID = UUID.randomUUID()

    lateinit var platoonname: String
    lateinit var leader: String
    @OneToMany(mappedBy = "platoon", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var missions: MutableList<Mission> = mutableListOf()
}