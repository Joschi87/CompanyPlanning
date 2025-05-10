package joschi.CompanyPlanning.Platoon

import joschi.CompanyPlanning.lib.appLogger
import joschi.CompanyPlanning.lib.exception.LeaderHasPlatoon
import joschi.CompanyPlanning.lib.exception.NoItemInDatabaseException
import joschi.CompanyPlanning.lib.exception.PlatoonExsitException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlatoonService @Autowired constructor(var repo: Platoonrepo){

    private val log = appLogger(this::class.java)

    fun getAllPlatoons(): MutableList<PlatoonModel> {
        return repo.findAll().takeIf { it.isNotEmpty() }
            ?: throw NoItemInDatabaseException("No Platoons are created")
    }

    fun createNewPlatoon(model: PlatoonModel){
        val platoonExisit = repo.existsByName(model.name)
        val leaderHasPlatoon = repo.existsByLeader(model.leader)
        if(!platoonExisit && !leaderHasPlatoon){
            log.info("Create Platoon: ${model}")
            repo.saveAndFlush(model)
        }else{
            if(platoonExisit)
                throw PlatoonExsitException("The Platoon with the name: ${model.name} exsist")
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

    fun updatePlatoon(model: PlatoonModel){
        if (repo.existsById(model.id)) {
            val modelFromDatabase = repo.getReferenceById(model.id)
            modelFromDatabase.leader = model.leader
            modelFromDatabase.name = model.name
            repo.saveAndFlush(modelFromDatabase)
        }else{
            throw NoItemInDatabaseException("Platoon ${model.name} with ID (${model.id}) dosen\'t exsist")
        }
    }
}