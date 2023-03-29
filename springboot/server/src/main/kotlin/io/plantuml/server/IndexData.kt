package io.plantuml.server

data class IndexData(
    var decoded: String,
    var showSocialButtons: Boolean,
    var showGithubRibbon: Boolean,
    var hasImg: Boolean,
    var imgurl: String,
    var svgurl: String,
    var txturl: String,
    var pdfurl: String,
    var mapurl: String,
    var hasMap: Boolean,
    var map: String
) {
    val plantumlVersion: String = net.sourceforge.plantuml.version.Version.versionString()!!
    val editorContent: String = net.sourceforge.plantuml.servlet.PlantUmlServlet.stringToHTMLString(decoded)
}