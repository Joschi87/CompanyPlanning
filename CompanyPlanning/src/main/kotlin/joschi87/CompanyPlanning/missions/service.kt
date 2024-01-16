package joschi87.CompanyPlanning.missions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class service @Autowired constructor(
    var repo: repo,
    var platoonrepo: repo){


    fun getAllMissions(): MutableList<model> {
        return repo.findAll()
    }

    fun createNewMission(model: model){

    }
}