package joschi87.CompanyPlanning.missions

import joschi87.CompanyPlanning.lib.EmptyFormularFieldException
import joschi87.CompanyPlanning.lib.MissionExistsException
import joschi87.CompanyPlanning.lib.NoItemInDatabaseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class service @Autowired constructor(var repo: repo, var platoonrepo: repo){


    fun getAllMissions(): MutableList<model> {
        return repo.findAll()
    }

    fun createNewMission(model: model){
        if(!repo.existsByName(model.name.toString())){
            if(model.text != null){
               repo.saveAndFlush(model)
            }else{
                throw EmptyFormularFieldException("Mission Text have no value")
            }
        }else{
           throw MissionExistsException("Mission ${model.name} exist in database")
        }
    }

    fun assignMissionToPlatoon(id: UUID, idPlatoon: UUID){
        if (repo.existsById(id) && platoonrepo.existsById(idPlatoon)){
            var mission = repo.findById(id)
            var platoon = platoonrepo.findById(idPlatoon)
            
        }else{
            throw NoItemInDatabaseException("Mission not found with ID: $id or platoon not found with ID: $idPlatoon")
        }
    }
}