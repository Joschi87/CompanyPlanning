package joschi87.CompanyPlanning.platoon

import joschi87.CompanyPlanning.lib.LeaderHasPlatoon
import joschi87.CompanyPlanning.lib.NoItemInDatabaseException
import joschi87.CompanyPlanning.lib.PlatoonExsitException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class service @Autowired constructor(var repo: repo){

    fun getAllPlatoons(): MutableList<model> {
        return repo.findAll()
    }

    fun createNewPlatoon(model: model){
        val platoonExisit = repo.existsByPlatoonname(model.platoonname)
        val leaderHasPlatoon = repo.existsByLeader(model.leader)
        if(!platoonExisit && !leaderHasPlatoon){
            repo.saveAndFlush(model)
        }else{
            if(platoonExisit)
                throw PlatoonExsitException("The Platoon with the name: ${model.platoonname} exsist")
            if(leaderHasPlatoon)
                throw LeaderHasPlatoon("${model.leader} has already been assigned a platoon")
        }
    }

    fun deletePlatoon(id: UUID){
        if (repo.existsById(id)){
            repo.deleteById(id)
        }else{
            throw NoItemInDatabaseException("Platoon with ID: ${id} dosen\'t exsist in database")
        }
    }

    fun updatePlatoon(model: model){
        if (repo.existsById(model.id)) {
            val modelFromDatabase = repo.getReferenceById(model.id)
            modelFromDatabase.leader = model.leader
            modelFromDatabase.platoonname = model.platoonname
            repo.saveAndFlush(modelFromDatabase)
        }else{
            throw NoItemInDatabaseException("Platoon ${model.platoonname} with ID (${model.id}) dosen\'t exsist")
        }
    }
}