package joschi87.CompanyPlanning.platoon

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/platoon")
class controller @Autowired constructor(var service: service){

    @GetMapping
    fun getAllplattons(){

    }
}