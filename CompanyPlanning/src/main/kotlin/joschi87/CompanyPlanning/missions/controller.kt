package joschi87.CompanyPlanning.missions


import joschi87.CompanyPlanning.missions.service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mission")
class controller @Autowired constructor(var service: service){

    @GetMapping
    fun getAllMissions(): MutableList<model> {
        return service.getAllMissions()
    }
}