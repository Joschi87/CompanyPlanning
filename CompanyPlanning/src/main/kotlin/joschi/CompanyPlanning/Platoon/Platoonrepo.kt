package joschi.CompanyPlanning.Platoon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface Platoonrepo : JpaRepository<PlatoonModel, UUID> {

    fun existsByPlatoonname(name: String): Boolean
    fun existsByLeader(leader: String): Boolean
    fun getReferenceByPlatoonname(name: String): PlatoonModel
}