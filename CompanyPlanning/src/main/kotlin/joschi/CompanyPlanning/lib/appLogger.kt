package joschi.CompanyPlanning.lib

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class appLogger(clazz: Class<*>) {
    private val logger: Logger = LoggerFactory.getLogger(clazz)

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun warn(message: String) {
        logger.warn(message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            logger.error(message, throwable)
        } else {
            logger.error(message)
        }
    }
}