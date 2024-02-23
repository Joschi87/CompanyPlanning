package joschi87.CompanyPlanning.missions


import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface repo  : JpaRepository<model, UUID> {

    fun existsByName(name: String): Boolean
}