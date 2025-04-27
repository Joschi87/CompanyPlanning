package joschi.CompanyPlanning.lib.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(PlatoonExsitException::class)
    fun handler(ex: PlatoonExsitException): ResponseEntity<String> {
        return ResponseEntity<String>(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(LeaderHasPlatoon::class)
    fun handler(ex: LeaderHasPlatoon): ResponseEntity<String>{
        return ResponseEntity<String>(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NoItemInDatabaseException::class)
    fun handler(ex: NoItemInDatabaseException): ResponseEntity<String>{
        return ResponseEntity<String>(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MissionExsitException::class)
    fun handler(ex: MissionExsitException): ResponseEntity<String>{
        return ResponseEntity<String>(ex.message, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(WrongMissionTypeException::class)
    fun handler(ex: WrongMissionTypeException): ResponseEntity<String>{
        return ResponseEntity<String>(ex.message, HttpStatus.FORBIDDEN)
    }
}