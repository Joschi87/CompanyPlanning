package joschi87.CompanyPlanning.platoon

import joschi87.CompanyPlanning.Platoon.Platoon
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface Platoonrepo : JpaRepository<Platoon, UUID> {

    fun existsByPlatoonname(name: String): Boolean
    fun existsByLeader(leader: String): Boolean
    fun getReferenceByName(name: String): Platoon?
}