package io.plantuml.server

import net.sourceforge.plantuml.servlet.PlantUmlServlet
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@ServletComponentScan(
	basePackageClasses = [PlantUmlServlet::class]
)
class ServerApplication

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
