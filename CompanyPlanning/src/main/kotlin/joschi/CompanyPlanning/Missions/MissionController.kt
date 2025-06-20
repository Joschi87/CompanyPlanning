package joschi.CompanyPlanning.Missions


import joschi.CompanyPlanning.Platoon.PlatoonModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/mission")
class MissionController @Autowired constructor(var missionService: MissionService){

    @GetMapping
    fun getAllMissions(): MutableList<MissionModel> {
        return missionService.getAllMissions()
    }

    @PostMapping
    fun createMission(@RequestBody missionModel: MissionModel): ResponseEntity<String> {
        return missionService.createNewMission(missionModel)
    }

    @PutMapping
    fun updateMission(@RequestBody missionModel: MissionModel): ResponseEntity<String> {
        return missionService.updateMission(missionModel)
    }

    @DeleteMapping("/{id}")
    fun deleteMission(@PathVariable id: UUID): ResponseEntity<String>{
        return missionService.deleteMission(id)
    }

    @GetMapping("/{id}")
    fun getSingleMission(@PathVariable id : UUID): MissionModel? {
        return missionService.getMissionById(id)
    }

    @PutMapping("/activ")
    fun setMissionActiv(id: UUID, platoon: String): ResponseEntity<String> {
        return missionService.setMissionActive(id, platoon)
    }

    @PutMapping("/inactiv")
    fun setMissionInactiv(id: UUID): ResponseEntity<String> {
        return missionService.setMissionInactive(id)
    }

    @PostMapping("/story")
    fun createStoryMission(missionModel: MissionModel): ResponseEntity<String>{
        return missionService.createStoryMission(missionModel)
    }

    @PutMapping("/story")
    fun setStoryMissionActiv(id: UUID, platoon: String): ResponseEntity<String> {
        return missionService.setStoryMissionActive(id, platoon)
    }
}