package joschi.CompanyPlanning.Missions


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MissionRepo  : JpaRepository<MissionModel, UUID> {

    fun existsByName(name: String): Boolean
}