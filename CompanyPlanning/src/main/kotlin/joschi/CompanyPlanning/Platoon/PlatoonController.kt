package joschi.CompanyPlanning.Platoon

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
class PlatoonController @Autowired constructor(var platoonService: PlatoonService){

    @GetMapping
    fun getAllPlatoons(): MutableList<PlatoonModel> {
        return platoonService.getAllPlatoons()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewPlatoon(model: PlatoonModel){
        platoonService.createNewPlatoon(model)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePlatoon(id: UUID){
        platoonService.deletePlatoon(id)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updatePlatoon(model: PlatoonModel){
        platoonService.updatePlatoon(model)
    }
}