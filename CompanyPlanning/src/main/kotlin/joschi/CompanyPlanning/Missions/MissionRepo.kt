package joschi.CompanyPlanning.Missions


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MissionRepo  : JpaRepository<MissionModel, UUID> {

    fun existsByMissionName(name: String): Boolean
}