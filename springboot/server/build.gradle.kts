import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.8.10"
	kotlin("plugin.spring") version "1.8.10"
}

group = "io.plantuml"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

val plantumlVersion = "1.2023.1"
val codemirrorVersion = "5.63.0"

dependencies {
	implementation("net.sourceforge.plantuml:plantuml:${plantumlVersion}")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.webjars.npm:codemirror:${codemirrorVersion}")
	implementation("org.webjars:webjars-locator:0.30")


	implementation("org.webjars:bootstrap:3.3.6")
	implementation("org.webjars:bootstrap-datepicker:1.0.1")
	implementation("org.webjars:jquery:1.9.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets{
	main{
		java{
			srcDir(project.layout.projectDirectory.dir("../../src/main/java"))
		}
		resources{
			srcDir(project.layout.projectDirectory.dir("../../src/main/resources"))
		}
	}
}