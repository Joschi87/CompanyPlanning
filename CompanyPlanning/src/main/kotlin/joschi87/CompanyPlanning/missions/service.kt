package joschi87.CompanyPlanning.missions

import joschi87.CompanyPlanning.Platoon.Platoon
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import joschi87.CompanyPlanning.lib.exception.MissionExsitException
import joschi87.CompanyPlanning.lib.exception.CompanyPlanningException
import joschi87.CompanyPlanning.platoon.Platoonrepo
import java.util.UUID
import javax.transaction.Transactional

@Component
class service @Autowired constructor(
    var missionRepo: missionRepo,
    var platoonrepo: Platoonrepo){


    fun getAllMissions(): MutableList<Mission> {
        return missionRepo.findAll()
    }

    @Transactional
    fun createNewMission(model: Mission):ResponseEntity<String>{
        if(!missionRepo.existsById(model.id) && !missionRepo.existsByName(model.name.toString())){
           missionRepo.saveAndFlush(model)
           if(missionRepo.existsById(model.id)){
                return ResponseEntity<String>("Mission created", HttpStatus.CREATED)
           }
        }else{
            if(missionRepo.existsById(model.id)){
                throw MissionExsitException("Mission with the ID: ${model.id} exsit!")
            }
            if(missionRepo.existsByName(model.name.toString())){
                throw MissionExsitException("Mission with the name: ${model.name} already exsit!")
            }
        }

        throw CompanyPlanningException("Something goes wrong")
    }

    @Transactional
    fun deleteMission(id: UUID):ResponseEntity<String>{
        if(missionRepo.existsById(id)){
            missionRepo.deleteById(id)
            if(!missionRepo.existsById(id)){
                return ResponseEntity<String>("Mission deleted", HttpStatus.OK)
            }
        }else{
            throw MissionExsitException("Mission with the ID: $id doesn\'t exsit")
        }
        throw MissionExsitException("Mission with the ID: $id doesn\'t exsist")
    }

    @Transactional
    fun updateMission(model: Mission): ResponseEntity<String> {
        val modelFromDatabase = missionRepo.findById(model.id).get()

        if (model.text != null || model.platoon != null) {
            modelFromDatabase.text = model.text
            modelFromDatabase.platoon = model.platoon
        }

        missionRepo.save(modelFromDatabase)

        return ResponseEntity.ok("Mission updated successfully.")
    }

    fun getMissionById(id: UUID): Mission? {
        return missionRepo.findById(id).orElse(null)
    }

    @Transactional
    fun setMissionActive(id: UUID, platoon: Platoon): ResponseEntity<String> {
        if(missionRepo.existsById(id) && platoonrepo.existsById(platoon.id)){
           val missionModel = missionRepo.getReferenceById(id)
            val platoonModel = platoonrepo.getReferenceByName(platoon.platoonname)

            platoonModel?.missions?.add(missionModel)
            missionModel.activ = true
            missionModel.platoon = platoonModel

            platoonrepo.saveAndFlush(platoonModel?: platoon)
            missionRepo.saveAndFlush(missionModel)

            return ResponseEntity<String>("Mission: ${missionModel.name} are active for ${platoonModel?.platoonname}", HttpStatus.ACCEPTED)
        }
        throw MissionExsitException("Mission with the ID: $id doesn\'t exsist")
    }
}
