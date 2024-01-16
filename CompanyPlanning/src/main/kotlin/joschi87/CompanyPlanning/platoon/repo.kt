package joschi87.CompanyPlanning.platoon

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface repo : JpaRepository<model, UUID> {

    fun existsByPlatoonname(name: String): Boolean
    fun existsByLeader(leader: String): Boolean
}