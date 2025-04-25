package joschi87.CompanyPlanning.lib.pdf

import joschi87.CompanyPlanning.Missions.MissionModel
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import joschi87.CompanyPlanning.Missions.MissionRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class pdfservice @Autowired constructor(
    var repo: MissionRepo
){

    fun uploadMultiplePdfs(files: List<MultipartFile>): ResponseEntity<String> {
        val results = mutableListOf<String>()

        for (file in files) {
            try {
                val text = extractTextFromPdf(file)
                val mission = parseMissionFromText(text)
                repo.save(mission)
                results.add("✅ ${file.originalFilename} -> Mission '${mission.name}' gespeichert")
            } catch (e: Exception) {
                results.add("❌ ${file.originalFilename} -> Fehler: ${e.message}")
            }
        }

        val responseText = results.joinToString("\n")
        return ResponseEntity.ok(responseText)
    }

    private fun extractTextFromPdf(file: MultipartFile): String {
        PDDocument.load(file.inputStream).use { document ->
            val pdfStripper = PDFTextStripper()
            return pdfStripper.getText(document).trim()
        }
    }

    private fun parseMissionFromText(text: String): MissionModel {
        val lines = text.lines()
        val map = mutableMapOf<String, String>()

        for (line in lines) {
            if (line.contains(":")) {
                val (key, value) = line.split(":", limit = 2)
                map[key.trim().lowercase()] = value.trim()
            }
        }

        val name = map["name"] ?: "Unnamed Mission"
        val storyMission = map["storymission"]?.toBooleanStrictOrNull() ?: false
        val finished = map["finished"]?.toBooleanStrictOrNull() ?: false

        val textBody = lines.dropWhile { !it.startsWith("Text:", ignoreCase = true) }
            .drop(1)
            .joinToString("\n")

        val missionModel = MissionModel()
        missionModel.text = text
        missionModel.name = name
        missionModel.storyMission = storyMission

        return missionModel
    }
}