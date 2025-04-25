package joschi87.CompanyPlanning.Missions


import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MissionRepo  : JpaRepository<MissionModel, UUID> {

    fun existsByName(name: String): Boolean
}