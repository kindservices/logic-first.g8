import org.scalajs.linker.interface.ModuleSplitStyle


ThisBuild / organization := "$organisation$"
ThisBuild / name := "$name$"
ThisBuild / version := "0.0.1"
ThisBuild / scalaVersion := "3.4.1"
ThisBuild / scalafmtOnCompile := true
ThisBuild / versionScheme := Some("early-semver")


// Common settings
lazy val commonSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "$pckg;format="lower,package"$.buildinfo",
  // ============== UNCOMMENT THIS LINE WHEN YOUR MODELS COME FROM THE SERVICE.YAML ===============
  //
  // this is our model libraries, generated from the service.yaml and created/publised via 'make packageRestCode'
  // you'll need to uncomment this line once you're using data models generated from the service.yaml
  //
  //
  // libraryDependencies += "$organisation$" %%% "$name$" % "0.0.1",
  // ================================================================================================
  libraryDependencies ++= Seq(
    "dev.zio" %%% "zio" % "$zio_version$",
    "org.scalatest" %%% "scalatest" % "$scalatest_version$" % Test,
    "com.lihaoyi" %%% "upickle" % "$upickle_version$",
    "com.lihaoyi" %%% "sourcecode" % "$sourcecode_version$"
  )
)

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-rewrite",//-rewrite -source 3.4-migration
  "-Xlint",
  "-Xsource:3.4"
)

lazy val app = crossProject(JSPlatform, JVMPlatform).in(file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(commonSettings).
  jvmSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "cask" % "$cask_version$")
  ).
  jsSettings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "$scala_time_version$",
      "com.lihaoyi" %%% "scalatags" % "$scalatags_version$",
      "org.scala-js" %%% "scalajs-dom" % "2.4.0"
    ),
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("$organisation$")))
    },
  )

lazy val root = project.in(file(".")).
  aggregate(app.js, app.jvm).
  settings(
    publish := {},
    publishLocal := {},
  )


ThisBuild / publishMavenStyle := true

val githubUser = "$user_name$"
val githubRepo = "$name$"
ThisBuild / publishTo := Some("GitHub Package Registry" at s"https://maven.pkg.github.com/\$githubUser/\$githubRepo")

sys.env.get("GITHUB_TOKEN") match {
  case Some(token) if token.nonEmpty =>
    ThisBuild / credentials += Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      githubUser,
      token
    )
  case _ =>
    println("\n\t\tGITHUB_TOKEN not set - assuming a local build\n\n")
    credentials ++= Nil
}