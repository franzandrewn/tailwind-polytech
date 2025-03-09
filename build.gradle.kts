plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.node-gradle.node") version "7.1.0"
}

group = "com.andrewn"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

node {
	download = false
	version = "22.11.0"
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<com.github.gradle.node.npm.task.NpxTask>("tailwind"){
	inputs.files("./src/main/resources/static/styles.css")
	dependsOn("npmInstall")
	command.set("@tailwindcss/cli")
	args.set(listOf("-i", "./src/main/resources/static/styles.css",
		"-o" ,"./src/main/resources/static/output.css", "--watch"))

	outputs.dir("./src/main/resources/static")
}

tasks.register("sync"){
	inputs.files("./src/main/resources/static","./src/main/resources/templates")
	doLast {
		sync {
			from("./src/main/resources/static")
			into("build/resources/main/static")
		}
		sync {
			from("./src/main/resources/templates")
			into("build/resources/main/templates")
		}
	}
	outputs.dir("build/resources/main")
}