package joschi87.CompanyPlanning.Platoon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface Platoonrepo : JpaRepository<PlatoonModel, UUID> {

    fun existsByPlatoonname(name: String): Boolean
    fun existsByLeader(leader: String): Boolean
    fun getReferenceByName(name: String): PlatoonModel?
}