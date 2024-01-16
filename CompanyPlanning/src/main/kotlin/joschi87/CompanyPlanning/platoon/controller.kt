package joschi87.CompanyPlanning.platoon

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/platoon")
class controller @Autowired constructor(var service: service){

    @GetMapping
    fun getAllPlatoons(): MutableList<model> {
        return service.getAllPlatoons()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewPlatoon(model: model){
        service.createNewPlatoon(model)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePlatoon(id: UUID){
        service.deletePlatoon(id)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updatePlatoon(model: model){
        service.updatePlatoon(model)
    }
}