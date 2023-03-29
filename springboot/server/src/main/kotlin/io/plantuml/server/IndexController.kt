package io.plantuml.server

import jakarta.servlet.ServletRequest
import net.sourceforge.plantuml.api.PlantumlUtils
import net.sourceforge.plantuml.servlet.utility.Configuration
import net.sourceforge.plantuml.servlet.utility.UmlExtractor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class IndexController {

    @GetMapping("/")
    fun index(model: Model,request: ServletRequest): String {
        val data = indexDataOf(request)
        model.set("data", data)
        return "index";
    }

    private fun ServletRequest.attribute(name:String) : String?{
        return getAttribute(name)?.toString()
    }

    private fun ServletRequest.booleanAttribute(name:String) : Boolean?{
        return getAttribute(name) as Boolean?
    }

    private fun dummyIndexData(): IndexData{
        return IndexData(
            decoded = """
                @startuml
                Bob -> Alice : hello
                @enduml
            """.trimIndent(),
            showSocialButtons = true,
            showGithubRibbon = true,
            hasImg = true,
            imgurl = "https://www.plantuml.com/plantuml/png/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80",
//            imgurl = "https://www.w3schools.com/html/workplace.jpg",
            svgurl = "https://www.plantuml.com/plantuml/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80",
            txturl = "https://www.plantuml.com/plantuml/txt/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80",
            pdfurl = "https://www.plantuml.com/plantuml/pdf/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80",
            mapurl = "https://picsum.photos/200/300",
            map="""
                <map name="plantuml_map">
                  <area shape="rect" coords="34,44,270,350" alt="Computer" href="computer.htm">
                  <area shape="rect" coords="290,172,333,250" alt="Phone" href="phone.htm">
                  <area shape="circle" coords="337,300,44" alt="Coffee" href="coffee.htm">
                </map>
            """.trimIndent(),
            hasMap = true
        )
    }
    private fun indexDataOf(request:ServletRequest): IndexData{
        return IndexData(
            decoded = request.attribute("decoded") ?: "",
            showSocialButtons = request.booleanAttribute("showSocialButtons") ?: false,
            showGithubRibbon = request.booleanAttribute("showGithubRibbon") ?: false,
            hasImg = request.booleanAttribute("hasImg") ?: false,
            imgurl = request.attribute("imgurl") ?: "",
            svgurl = request.attribute("svgurl") ?: "",
            txturl = request.attribute("txturl") ?: "",
            pdfurl = request.attribute("pdfurl") ?: "",
            mapurl = request.attribute("mapurl") ?: "",
            map=request.attribute("map") ?: "",
            hasMap = request.booleanAttribute("hasMap") ?: false,
        )
    }
}