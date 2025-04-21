package joschi87.CompanyPlanning.missions


import joschi87.CompanyPlanning.missions.service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/mission")
class controller @Autowired constructor(var service: service){

    @GetMapping
    fun getAllMissions(): MutableList<Mission> {
        return service.getAllMissions()
    }

    @PostMapping
    fun createMission(mission: Mission): ResponseEntity<String> {
        return service.createNewMission(mission)
    }

    @PutMapping
    fun updateMission(mission: Mission): ResponseEntity<String> {
        return service.updateMission(mission)
    }

    @DeleteMapping
    fun deleteMission(id: UUID): ResponseEntity<String>{
        return service.deleteMission(id)
    }

    @GetMapping
    @RequestMapping("/{id}")
    fun getSingleMission(@PathVariable id : UUID): Mission? {
        return service.getMissionById(id)
    }

    @PostMapping
    @RequestMapping("/story")
    fun createStoryMission(mission: Mission): ResponseEntity<String>{
        return service.createStoryMission(mission)
    }
}