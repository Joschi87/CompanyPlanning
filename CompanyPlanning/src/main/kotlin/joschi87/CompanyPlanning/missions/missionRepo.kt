package joschi87.CompanyPlanning.missions


import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface missionRepo  : JpaRepository<Mission, UUID> {

    fun existsByName(name: String): Boolean
}