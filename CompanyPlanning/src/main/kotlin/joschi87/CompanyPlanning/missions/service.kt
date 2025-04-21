package joschi87.CompanyPlanning.missions

import joschi87.CompanyPlanning.Platoon.Platoon
import joschi87.CompanyPlanning.lib.exception.CompanyPlanningException
import joschi87.CompanyPlanning.lib.exception.MissionExsitException
import joschi87.CompanyPlanning.lib.exception.WrongMissionTypeException
import joschi87.CompanyPlanning.platoon.Platoonrepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.EntityNotFoundException
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
        val modelFromDatabase = missionRepo.getReferenceById(model.id)
        if (model.text != null || model.platoon != null) {
            modelFromDatabase.text = model.text
            modelFromDatabase.platoon = model.platoon

            if(model.storyMission)
                modelFromDatabase.storyMission = true
        }else{
            createNewMission(model)
        }
        missionRepo.saveAndFlush(modelFromDatabase)
        return ResponseEntity.ok("Mission updated successfully.")
    }

    fun getMissionById(id: UUID): Mission? {
        return missionRepo.findById(id).orElse(null)
    }

    @Transactional
    fun setMissionActive(id: UUID, platoon: Platoon): ResponseEntity<String> {
        if(!checkIfPlatoonInStoryMission(platoon.platoonname))
            return ResponseEntity<String>("Platoon: ${platoon.platoonname} is currently in a story mission", HttpStatus.CONFLICT)
        if(missionRepo.existsById(id) && platoonrepo.existsById(platoon.id)){
           val missionModel = missionRepo.getReferenceById(id)
            val platoonModel = platoonrepo.getReferenceByName(platoon.platoonname)

            platoonModel?.missions?.add(missionModel)
            missionModel.activ = true
            missionModel.platoon = platoonModel
            platoonModel?.timeActiveMission = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            platoonrepo.saveAndFlush(platoonModel?: platoon)
            missionRepo.saveAndFlush(missionModel)

            return ResponseEntity<String>("Mission: ${missionModel.name} are active for ${platoonModel?.platoonname}", HttpStatus.ACCEPTED)
        }
        if(!missionRepo.existsById(id))
            throw MissionExsitException("Mission with the ID: $id doesn\'t exsit")
        if(!platoonrepo.existsById(platoon.id))
            throw MissionExsitException("Platoon with the ID: ${platoon.id} doesn\'t exsit")
        throw CompanyPlanningException("Something goes wrong")
    }

    @Transactional
    fun setMissionInactive(id: UUID): ResponseEntity<String> {
        if(missionRepo.existsById(id) && platoonrepo.existsById(id)){
            val missionModel = missionRepo.getReferenceById(id)

            missionModel.activ = false
            missionModel.platoon = null

            if (missionModel.storyMission)
                missionModel.finished = true

            missionRepo.saveAndFlush(missionModel)

            return ResponseEntity<String>("Mission: ${missionRepo.getReferenceById(id).name} are inactive or the story mission are finished", HttpStatus.ACCEPTED)
        }else{
            if(!missionRepo.existsById(id))
                throw MissionExsitException("Mission with the ID: $id doesn\'t exsit")
            if(!platoonrepo.existsById(id))
                throw MissionExsitException("Platoon with the ID: $id doesn\'t exsit")
        }
        throw CompanyPlanningException("Something goes wrong")
    }

    @Transactional
    fun createStoryMission(model: Mission): ResponseEntity<String>{
        if(!missionRepo.existsByName(model.name.toString())){
            model.storyMission = true
            missionRepo.saveAndFlush(model)
            return ResponseEntity<String>("Story mission ${model.name} saved!", HttpStatus.CREATED)
        }else{
            throw MissionExsitException("Mission with the name: ${model.name} already exist")
        }
    }

    @Transactional
    fun setStoryMissionActive(id: UUID, platoon: Platoon): ResponseEntity<String>{
        if(!checkIfPlatoonInStoryMission(platoon.platoonname))
            return ResponseEntity<String>("Platoon: ${platoon.platoonname} is currently in a story mission", HttpStatus.CONFLICT)
        val mission: Mission
        try {
            mission = missionRepo.getReferenceById(id)
            if(mission.storyMission){
                platoon.missions.add(mission)
                mission.activ = true
                mission.platoon = platoon
                platoon.timeActiveMission = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                missionRepo.saveAndFlush(mission)
                platoonrepo.saveAndFlush(platoon)
                return ResponseEntity<String>("Platoon: ${platoon.platoonname} has the story mission: ${mission.name} as activ mission", HttpStatus.ACCEPTED)
            }else{
                throw WrongMissionTypeException("Mission: ${mission.name} are no story mission")
            }
        }catch (ex : EntityNotFoundException){
            throw MissionExsitException(ex.message)
        }
    }

    fun checkIfPlatoonInStoryMission(name: String): Boolean {
        return platoonrepo.getReferenceByName(name)?.missions?.any { it.storyMission && !it.finished } ?: false
    }
}
