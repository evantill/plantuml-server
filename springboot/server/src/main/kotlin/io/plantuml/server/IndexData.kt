package io.plantuml.server

import net.sourceforge.plantuml.servlet.utility.HtmlUtils

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
    val editorContent: String = HtmlUtils.htmlEscape(decoded)
}