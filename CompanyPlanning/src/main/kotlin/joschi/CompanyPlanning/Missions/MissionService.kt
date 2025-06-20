package joschi.CompanyPlanning.Missions

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import joschi.CompanyPlanning.Platoon.PlatoonModel
import joschi.CompanyPlanning.lib.exception.CompanyPlanningException
import joschi.CompanyPlanning.lib.exception.MissionExsitException
import joschi.CompanyPlanning.lib.exception.WrongMissionTypeException
import joschi.CompanyPlanning.Platoon.Platoonrepo
import joschi.CompanyPlanning.lib.appLogger
import joschi.CompanyPlanning.lib.exception.NoItemInDatabaseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class MissionService @Autowired constructor(
    var missionRepo: MissionRepo,
    var platoonRepo: Platoonrepo){

    private val log = appLogger(this::class.java)

    fun getAllMissions(): MutableList<MissionModel> {
        return missionRepo.findAll().takeIf { it.isNotEmpty() }
            ?: throw NoItemInDatabaseException("No Missions are created")
    }

    @Transactional
    fun createNewMission(model: MissionModel):ResponseEntity<String>{
        if(!missionRepo.existsById(model.id) && !missionRepo.existsByMissionName(model.missionName.toString())){
            log.info(model.missionName.toString())
           missionRepo.saveAndFlush(model)
           if(missionRepo.existsById(model.id)){
                return ResponseEntity<String>("Mission created", HttpStatus.CREATED)
           }
        }else{
            if(missionRepo.existsById(model.id)){
                throw MissionExsitException("Mission with the ID: ${model.id} exsit!")
            }
            if(missionRepo.existsByMissionName(model.missionName.toString())){
                throw MissionExsitException("Mission with the name: ${model.missionName} already exsit!")
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
    fun updateMission(model: MissionModel): ResponseEntity<String> {
        val modelFromDatabase = missionRepo.getReferenceById(model.id)
        if (model.text != null) {
            modelFromDatabase.text = model.text

            if(model.storyMission)
                modelFromDatabase.storyMission = true
        }else{
            createNewMission(model)
        }
        missionRepo.saveAndFlush(modelFromDatabase)
        return ResponseEntity.ok("Mission updated successfully.")
    }

    fun getMissionById(id: UUID): MissionModel? {
        return missionRepo.findById(id).orElse(null)
    }

    @Transactional
    fun setMissionActive(id: UUID, platoon: String): ResponseEntity<String> {
        var platoonModal = platoonRepo.getReferenceByPlatoonname(platoon)
        if(!checkIfPlatoonInStoryMission(platoonModal.platoonname))
            return ResponseEntity<String>("Platoon: ${platoonModal.platoonname} is currently in a story mission", HttpStatus.CONFLICT)
        if(missionRepo.existsById(id) && platoonRepo.existsById(platoonModal.id)){
           val missionModel = missionRepo.getReferenceById(id)
            missionModel.platoon = platoon
            missionModel.activ = true
            platoonModal.missionModels.plusElement(missionModel)
            platoonModal.timeActiveMission = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            platoonRepo.saveAndFlush(platoonModal)
            missionRepo.saveAndFlush(missionModel)

            return ResponseEntity<String>("Mission: ${missionModel.missionName} are active for ", HttpStatus.ACCEPTED)
        }
        if(!missionRepo.existsById(id))
            throw MissionExsitException("Mission with the ID: $id doesn\'t exsit")
        throw CompanyPlanningException("Something goes wrong")
    }

    @Transactional
    fun setMissionInactive(id: UUID): ResponseEntity<String> {
        if(missionRepo.existsById(id) && platoonRepo.existsById(id)){
            val missionModel = missionRepo.getReferenceById(id)
            val platoonModel = platoonRepo.getReferenceByPlatoonname(missionModel.platoon)

            platoonModel.timeActiveMission = ""
            missionModel.activ = false

            if (missionModel.storyMission)
                missionModel.finished = true

            platoonRepo.saveAndFlush(platoonModel)
            missionRepo.saveAndFlush(missionModel)

            return ResponseEntity<String>("Mission: ${missionRepo.getReferenceById(id).missionName} are inactive or the story mission are finished", HttpStatus.ACCEPTED)
        }else{
            if(!missionRepo.existsById(id))
                throw MissionExsitException("Mission with the ID: $id doesn\'t exsit")
            if(!platoonRepo.existsById(id))
                throw MissionExsitException("Platoon with the ID: $id doesn\'t exsit")
        }
        throw CompanyPlanningException("Something goes wrong")
    }

    @Transactional
    fun createStoryMission(model: MissionModel): ResponseEntity<String>{
        if(!missionRepo.existsByMissionName(model.missionName.toString())){
            model.storyMission = true
            missionRepo.saveAndFlush(model)
            return ResponseEntity<String>("Story mission ${model.missionName} saved!", HttpStatus.CREATED)
        }else{
            throw MissionExsitException("Mission with the name: ${model.missionName} already exist")
        }
    }

    @Transactional
    fun setStoryMissionActive(id: UUID, platoon: String): ResponseEntity<String>{
        var platoonModel = platoonRepo.getReferenceByPlatoonname(platoon)
        if(!checkIfPlatoonInStoryMission(platoonModel.platoonname))
            return ResponseEntity<String>("Platoon: ${platoonModel.platoonname} is currently in a story mission", HttpStatus.CONFLICT)
        val missionModel: MissionModel
        try {
            missionModel = missionRepo.getReferenceById(id)
            if(missionModel.storyMission){
                platoonModel.missionModels.plusElement(missionModel)
                missionModel.activ = true
                platoonModel.timeActiveMission = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                missionRepo.saveAndFlush(missionModel)
                platoonRepo.saveAndFlush(platoonModel)
                return ResponseEntity<String>("Platoon: ${platoonModel.platoonname} has the story mission: ${missionModel.missionName} as activ mission", HttpStatus.ACCEPTED)
            }else{
                throw WrongMissionTypeException("Mission: ${missionModel.missionName} are no story mission")
            }
        }catch (ex : EntityNotFoundException){
            throw MissionExsitException(ex.message)
        }
    }

    fun checkIfPlatoonInStoryMission(name: String): Boolean {
        return platoonRepo.getReferenceByPlatoonname(name)?.missionModels?.any { it.storyMission && !it.finished } ?: false
    }
}
