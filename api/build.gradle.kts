repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":modules:base"))
    implementation(project(":modules:mysql"))
    implementation(project(":modules:kafka"))
    implementation(project(":modules:redis"))
}

