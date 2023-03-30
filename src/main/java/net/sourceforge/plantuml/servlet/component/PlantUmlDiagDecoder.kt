package net.sourceforge.plantuml.servlet.component

interface PlantUmlDiagDecoder {
    @JvmInline
    value class Diagram(val content: String)

    fun decode(): Result<Diagram>

}