package hello.world

object HelloWorldImpl extends HelloWorld {

  override def helloWorld(name: String): String =
    s"${Constants.HelloWorld} [$name]"

}
