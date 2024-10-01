import mill._, scalalib._, coursier.maven.MavenRepository

object $name$ extends ScalaModule {
  def scalaVersion = "3.4.1"

  // Dependencies
  def ivyDeps = Agg(ivy"kindservices::logic-first-jvm:0.7.5")
  def testIvyDeps = Agg(ivy"org.scalatest::scalatest:3.2.17")

  // we expect a github PAT with this particular
  def password = sys.env.getOrElse("GITHUB_TOKEN", sys.error("GITHUB_TOKEN env variable not set to your github PAT"))

  // Custom repositories
  def repositoriesTask = T.task { super.repositoriesTask() ++ Seq(MavenRepository("https://maven.pkg.github.com/kindservices/logic-first", authentication = Some(coursier.core.Authentication("aaronp", password)))) }

  override def resources = T.sources {
    super.resources() ++
      Seq(
        PathRef(os.pwd / "shared" / "src" / "main" / "resources"),
        PathRef(os.pwd / "jvm" / "src" / "main" / "resources")
      )
  }

  override def sources = T.sources {
    super.sources() ++
      Seq(
        PathRef(os.pwd / "shared" / "src" / "main" / "scala"),
        PathRef(os.pwd / "jvm" / "src" / "main" / "scala")
      )
  }

  def mainClass = Some("Main")
}