package joschi87.CompanyPlanning.missions

import lombok.Data
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Data
class model {

    @Id
    var id: UUID = UUID.randomUUID()

    var name: String ?= null
    var text: String ?= null
    var finished: Boolean = false
    var activ: Boolean = false


}