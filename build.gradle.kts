plugins {
    java
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents:httpclient:4.5.2")
    implementation("org.apache.httpcomponents:httpmime:4.5.2")
    implementation("com.google.code.gson:gson:2.2.2")
    implementation("org.apache.commons:commons-lang3:3.4")
    implementation("javax.ws.rs:jsr311-api:1.1.1")
    implementation("commons-io:commons-io:2.4")
    implementation("commons-validator:commons-validator:1.4.1")

    testImplementation("junit:junit:4.13.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "nl.modelingvalue"
            artifactId = "jira-rest-client"
            version = "3.0"

            from(components["java"])
        }
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}